package ua.in.sz.english.service.tokenizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.tokenizer.sentence.SentenceConsumer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceDto;
import ua.in.sz.english.service.tokenizer.sentence.SentenceNormalizer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class SentenceParserService {
    @Value("${sentence.path}")
    private String sentencePath;
    @Value("${page.path}")
    private String pagePath;

    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public SentenceParserService(TaskExecutor parserTaskExecutor) {
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void processSentence() {
        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(100);
        SentenceNormalizer normalizer = new SentenceNormalizer();
        SentenceProducer producer = new SentenceProducer(queue, normalizer, pagePath);
        SentenceConsumer consumer = new SentenceConsumer(queue, sentencePath);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
