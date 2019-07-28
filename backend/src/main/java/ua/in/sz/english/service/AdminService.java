package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.book.BookParser;
import ua.in.sz.english.service.parser.book.PageDto;
import ua.in.sz.english.service.parser.book.TextWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
CompletableFuture
 */
@Slf4j
@Service
public class AdminService {
    @Value("${book.dir.path}")
    private String bookDirPath;
    @Value("${text.dir.path}")
    private String textPath;
    @Value("${parser.queue.capacity:20}")
    private int queueCapacity;

    private final TaskExecutor asyncTaskExecutor;
    private final TaskExecutor asyncCommandExecutor;

    public AdminService(TaskExecutor asyncCommandExecutor,
                        TaskExecutor asyncTaskExecutor) {
        this.asyncCommandExecutor = asyncCommandExecutor;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public void indexBook() {
        try (
                DirectoryStream<Path> books = Files.newDirectoryStream(
                        Files.createDirectories(Paths.get(bookDirPath)), "*.pdf")
        ) {
            Files.createDirectories(Paths.get(textPath));

            for (Path book : books) {
                final String path = book.toString();
                String baseName = FilenameUtils.getBaseName(path);
                final String text = textPath + File.separator + baseName + ".txt";

                BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(queueCapacity);
                BookParser parser = new BookParser(queue, path);
                TextWriter writer = new TextWriter(queue, text);

                asyncTaskExecutor.execute(parser);
                asyncTaskExecutor.execute(writer);
            }
        } catch (IOException e) {
            log.error("Can't read books", e);
        }
    }
}
