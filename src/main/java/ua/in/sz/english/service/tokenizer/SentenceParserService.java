package ua.in.sz.english.service.tokenizer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.tokenizer.sentence.SentenceConsumer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceDto;
import ua.in.sz.english.service.tokenizer.sentence.SentenceNormalizer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceProducer;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class SentenceParserService {
    private final ObjectProvider<SentenceProducer> pageProducerProvider;
    private final ObjectProvider<SentenceNormalizer> sentenceNormalizerObjectProvider;
    private final ObjectProvider<SentenceConsumer> pageConsumerProvider;
    private final ObjectProvider<BlockingQueue<SentenceDto>> pageQueueProvider;
    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public SentenceParserService(
            ObjectProvider<SentenceProducer> pageProducerProvider,
            ObjectProvider<SentenceConsumer> pageConsumerProvider,
            ObjectProvider<BlockingQueue<SentenceDto>> pageQueueProvider,
            ObjectProvider<SentenceNormalizer> sentenceNormalizerObjectProvider,
            TaskExecutor parserTaskExecutor) {

        this.pageProducerProvider = pageProducerProvider;
        this.pageConsumerProvider = pageConsumerProvider;
        this.pageQueueProvider = pageQueueProvider;
        this.sentenceNormalizerObjectProvider = sentenceNormalizerObjectProvider;
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void processSentence() {
        BlockingQueue<SentenceDto> queue = pageQueueProvider.getObject();
        SentenceNormalizer normalizer = sentenceNormalizerObjectProvider.getObject();
        SentenceProducer producer = pageProducerProvider.getObject(queue, normalizer);
        SentenceConsumer consumer = pageConsumerProvider.getObject(queue);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
