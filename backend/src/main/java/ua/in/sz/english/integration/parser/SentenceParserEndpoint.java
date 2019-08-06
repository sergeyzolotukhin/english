package ua.in.sz.english.integration.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.service.index.build.SentenceIndexDto;
import ua.in.sz.english.service.index.build.SentenceIndexWriter;
import ua.in.sz.english.service.index.build.SentenceReader;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SentenceParserEndpoint {
    private final AppProps props;
    private final TaskExecutor executor;

    public SentenceParserEndpoint(TaskExecutor executor, AppProps props) {
        this.executor = executor;
        this.props = props;
    }

    public void parse(File sentence) throws ExecutionException, InterruptedException {
        log.info("Start parse sentence: {}", sentence);

        BlockingQueue<SentenceIndexDto> queue = new ArrayBlockingQueue<>(props.getSentenceParseQueueCapacity());
        SentenceReader parser = new SentenceReader(queue, sentence.getPath());
        SentenceIndexWriter writer = new SentenceIndexWriter(queue, props.getIndexDirPath());

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor))
        .get();

        log.info("End parse sentence: {}", sentence);
    }
}
