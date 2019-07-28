package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.BookParserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AdminService {
    @Value("${book.dir.path}")
    private String bookDirPath;
    @Value("${text.dir.path}")
    private String textDirPath;
    @Value("${sentence.dir.path}")
    private String sentenceDirPath;
    @Value("${parser.queue.capacity:20}")
    private int queueCapacity;

    private final TaskExecutor asyncCommandExecutor;
    private final BookParserService bookParserService;

    public AdminService(TaskExecutor asyncCommandExecutor,
                        BookParserService bookParserService) {
        this.asyncCommandExecutor = asyncCommandExecutor;
        this.bookParserService = bookParserService;
    }

    public void indexBook() {
        Path bookDir = Paths.get(this.bookDirPath);
        if (!Files.exists(bookDir)) {
            log.info("Book directory not exist {}", bookDir);
            return;
        }

        try (DirectoryStream<Path> books = Files.newDirectoryStream(bookDir, "*.pdf")) {

            createEmptyDirectory(textDirPath);
            createEmptyDirectory(sentenceDirPath);

            for (Path book : books) {
                String bookPath = book.toString();
                String textPath = toTextFileName(bookPath);
                String sentencePath = toSentenceFileName(bookPath);

                Runnable bookParseCommand = bookParserService.createBookParseCommand(bookPath, textPath);
                Runnable textParseCommand = bookParserService.createTextParseCommand(textPath, sentencePath);

                CompletableFuture.runAsync(bookParseCommand, asyncCommandExecutor)
                        .thenRunAsync(textParseCommand, asyncCommandExecutor);
            }
        } catch (IOException e) {
            log.error("Can't read books", e);
        }
    }

    private void createEmptyDirectory(String path) throws IOException {
        Path textDir = Paths.get(path);
        if (Files.exists(textDir)) {
            FileUtils.cleanDirectory(textDir.toFile());
        } else {
            Files.createDirectory(textDir);
        }
    }

    private String toTextFileName(String bookPath) {
        return this.textDirPath + File.separator + FilenameUtils.getBaseName(bookPath) + ".txt";
    }

    private String toSentenceFileName(String bookPath) {
        return this.sentenceDirPath + File.separator + FilenameUtils.getBaseName(bookPath) + ".txt";
    }
}
