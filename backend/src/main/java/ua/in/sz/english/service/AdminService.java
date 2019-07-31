package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.AppProperties;
import ua.in.sz.english.service.index.build.SentenceIndexDto;
import ua.in.sz.english.service.index.build.SentenceIndexWriter;
import ua.in.sz.english.service.index.build.SentenceReader;
import ua.in.sz.english.service.parser.book.BookParser;
import ua.in.sz.english.service.parser.book.PageDto;
import ua.in.sz.english.service.parser.book.TextWriter;
import ua.in.sz.english.service.parser.text.SentenceDto;
import ua.in.sz.english.service.parser.text.SentenceWriter;
import ua.in.sz.english.service.parser.text.TextParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AdminService {
    private final AppProperties appProperties;
    @Value("${parser.queue.capacity:20}")
    private int queueCapacity;
    @Value("classpath:en-sent.bin")
    private Resource sentenceModel;

    private final TaskExecutor asyncTaskExecutor;

    @Autowired
    public AdminService(AppProperties appProperties, TaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
        this.appProperties = appProperties;
    }

    public void indexBook() {
        Path bookDir = Paths.get(this.appProperties.getBookDirPath());
        if (!Files.exists(bookDir)) {
            log.info("Book directory not exist {}", bookDir);
            return;
        }

        try (DirectoryStream<Path> books = Files.newDirectoryStream(bookDir, "*.pdf")) {
            createEmptyDirectory(appProperties.getTextDirPath());
            createEmptyDirectory(appProperties.getSentenceDirPath());

            SentenceIndexWriter indexWriter = writeIndex();
            CompletableFuture.runAsync(indexWriter, asyncTaskExecutor);

            int count = 0;
            for (Path book : books) {
                count++;

                String bookName = FilenameUtils.getBaseName(book.toString());

                parseBook(book, bookName)
                        .thenRun(() -> parseText(bookName)
                                .thenRun(() -> readSentence(bookName, indexWriter.getQueue())));
            }

            indexWriter.expectedCount(count);
        } catch (IOException e) {
            log.error("Can't read books", e);
        }
    }

    // ================================================================================================================
    // private method
    // ================================================================================================================

    private CompletableFuture<Void> parseBook(Path book, String bookName) {
        BlockingQueue<PageDto> bookQueue = new ArrayBlockingQueue<>(queueCapacity);

        String bookPath = book.toString();
        String textPath = toTextFileName(bookName);

        BookParser parser = new BookParser(bookQueue, bookPath);
        TextWriter writer = new TextWriter(bookQueue, textPath);

        return CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, asyncTaskExecutor),
                CompletableFuture.runAsync(writer, asyncTaskExecutor));
    }

    private CompletableFuture<Void> parseText(String bookName) {
        BlockingQueue<SentenceDto> textQueue = new ArrayBlockingQueue<>(queueCapacity);

        String textPath = toTextFileName(bookName);
        String sentencePath = toSentenceFileName(bookName);

        TextParser parser = new TextParser(textQueue, sentenceModel, textPath);
        SentenceWriter writer = new SentenceWriter(textQueue, sentencePath);

        return CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, asyncTaskExecutor),
                CompletableFuture.runAsync(writer, asyncTaskExecutor));
    }

    private void readSentence(String bookName, BlockingQueue<SentenceIndexDto> queue) {
        String sentencePath = toSentenceFileName(bookName);
        SentenceReader reader = new SentenceReader(queue, sentencePath);

        CompletableFuture.runAsync(reader);
    }

    private void createEmptyDirectory(String path) throws IOException {
        Path textDir = Paths.get(path);
        if (Files.exists(textDir)) {
            FileUtils.cleanDirectory(textDir.toFile());
        } else {
            Files.createDirectory(textDir);
        }
    }

    private SentenceIndexWriter writeIndex() {
        BlockingQueue<SentenceIndexDto> queue = new ArrayBlockingQueue<>(queueCapacity);
        return new SentenceIndexWriter(queue, appProperties.getIndexDirPath());
    }

    private String toTextFileName(String name) {
        return this.appProperties.getTextDirPath() + File.separator + name + ".txt";
    }

    private String toSentenceFileName(String name) {
        return this.appProperties.getSentenceDirPath() + File.separator + name + ".txt";
    }
}
