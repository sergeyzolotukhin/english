package ua.in.sz.english.integration.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.service.parser.book.BookParser;
import ua.in.sz.english.service.parser.book.PageDto;
import ua.in.sz.english.service.parser.book.TextWriter;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class BookParserEndpoint {

    private final AppProps props;
    private final TaskExecutor executor;

    public BookParserEndpoint(TaskExecutor executor, AppProps props) {
        this.executor = executor;
        this.props = props;
    }

    public File parse(File book) throws ExecutionException, InterruptedException {
        log.info("Start parse book: {}", book);

        Assert.notNull(book, "Book is null");

        String bookName = FilenameUtils.getBaseName(book.toString());

        String bookPath = book.toString();
        String textPath = toTextFileName(bookName);

        BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(props.getBookParseQueueCapacity());
        BookParser parser = new BookParser(queue, bookPath);
        TextWriter writer = new TextWriter(queue, textPath);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor)
        ).get();

        log.info("End parse book: {}", book);
        return new File(textPath);
    }

    private String toTextFileName(String name) {
        return this.props.getTextDirPath() + File.separator + name + ".txt";
    }
}
