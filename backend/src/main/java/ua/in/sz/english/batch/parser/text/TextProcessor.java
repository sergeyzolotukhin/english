package ua.in.sz.english.batch.parser.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Queue;
import ua.in.sz.english.batch.queue.Queues;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TextProcessor implements ItemWriter<File> {
    private final AppProps props;
    private final TaskExecutor executor;

    @Value("classpath:en-sent.bin")
    private Resource sentenceModel;

    public TextProcessor(TaskExecutor executor, AppProps props) {
        this.executor = executor;
        this.props = props;
    }

    @Override
    public void write(List<? extends File> files) throws Exception {
        for (File file : files) {
            parse(file);
        }
    }

    private void parse(File file) throws ExecutionException, InterruptedException {
        log.debug("Start parse text: {}", file);

        Queue<Event> queue = Queues.createQueue(props.getTextParseQueueCapacity());

        String bookName = FilenameUtils.getBaseName(file.toString());
        String sentencePath = toSentenceFileName(bookName);

        TextParser parser = new TextParser(queue, sentenceModel, file.getPath());
        SentenceWriter writer = new SentenceWriter(queue, sentencePath);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor)).get();

        log.debug("End parse text: {}", file);
    }

    private String toSentenceFileName(String name) {
        return this.props.getSentenceDirPath() + File.separator + name + ".txt";
    }
}
