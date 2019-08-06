package ua.in.sz.english.batch.parser.book;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.Assert;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Queue;
import ua.in.sz.english.batch.queue.Queues;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class BookProcessor implements ItemWriter<File> {
    private final AppProps props;
    private final AsyncListenableTaskExecutor executor;

    public BookProcessor(AsyncListenableTaskExecutor executor, AppProps props) {
        this.executor = executor;
        this.props = props;
    }

    @Override
    public void write(List<? extends File> files) throws Exception {
        for (File file : files) {
            execute(file);
        }
    }

    private void execute(File book) throws Exception {
        log.debug("Start parse book: {}", book);

        Assert.notNull(book, "Book is null");

        String bookName = FilenameUtils.getBaseName(book.toString());

        String bookPath = book.toString();
        String textPath = toTextFileName(bookName);

        Queue<Event> queue = Queues.createQueue(props.getBookParseQueueCapacity());
        BookParser parser = new BookParser(queue, bookPath);
        TextWriter writer = new TextWriter(queue, textPath);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor)).get();

        log.debug("End parse book: {}", book);
    }

    private String toTextFileName(String name) {
        return this.props.getTextDirPath() + File.separator + name + ".txt";
    }
}
