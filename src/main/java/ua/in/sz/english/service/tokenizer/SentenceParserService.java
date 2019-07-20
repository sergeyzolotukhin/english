package ua.in.sz.english.service.tokenizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.pdf.PdfPageDto;
import ua.in.sz.english.service.tokenizer.sentence.SentenceConsumer;
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

    private final ObjectProvider<SentenceProducer> pageProducerProvider;
    private final ObjectProvider<SentenceConsumer> pageConsumerProvider;
    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public SentenceParserService(
            ObjectProvider<SentenceProducer> pageProducerProvider,
            ObjectProvider<SentenceConsumer> pageConsumerProvider,
            TaskExecutor parserTaskExecutor) {

        this.pageProducerProvider = pageProducerProvider;
        this.pageConsumerProvider = pageConsumerProvider;
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void processSentence() {
        BlockingQueue<PdfPageDto> queue = new ArrayBlockingQueue<>(100);
        SentenceProducer producer = pageProducerProvider.getObject(queue, pagePath);
        SentenceConsumer consumer = pageConsumerProvider.getObject(queue, sentencePath);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
