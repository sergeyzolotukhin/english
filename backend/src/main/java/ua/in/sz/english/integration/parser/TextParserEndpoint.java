package ua.in.sz.english.integration.parser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.service.parser.text.SentenceDto;
import ua.in.sz.english.service.parser.text.SentenceWriter;
import ua.in.sz.english.service.parser.text.TextParser;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TextParserEndpoint {
    private final AppProps props;
    private final TaskExecutor executor;

    @Value("classpath:en-sent.bin")
    private Resource sentenceModel;

    public TextParserEndpoint(TaskExecutor executor, AppProps props) {
        this.executor = executor;
        this.props = props;
    }

    public File parse(File text) throws ExecutionException, InterruptedException {
        log.info("Start parse text: {}", text);

        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(props.getTextParseQueueCapacity());

        String bookName = FilenameUtils.getBaseName(text.toString());
        String sentencePath = toSentenceFileName(bookName);

        TextParser parser = new TextParser(queue, sentenceModel, text.getPath());
        SentenceWriter writer = new SentenceWriter(queue, sentencePath);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(parser, executor),
                CompletableFuture.runAsync(writer, executor))
        .get();

        log.info("End parse text: {}", text);
        return new File(sentencePath);
    }

    private String toSentenceFileName(String name) {
        return this.props.getSentenceDirPath() + File.separator + name + ".txt";
    }
}
