package ua.in.sz.english.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.pdf.PdfPageConsumer;
import ua.in.sz.english.service.parser.pdf.PdfPageDto;
import ua.in.sz.english.service.parser.pdf.PdfPageProducer;

import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class BookParserService {
    private final ObjectProvider<PdfPageProducer> pageProducerProvider;
    private final ObjectProvider<PdfPageConsumer> pageConsumerProvider;
    private final ObjectProvider<BlockingQueue<PdfPageDto>> pageQueueProvider;
    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public BookParserService(
            ObjectProvider<PdfPageProducer> pageProducerProvider,
            ObjectProvider<PdfPageConsumer> pageConsumerProvider,
            ObjectProvider<BlockingQueue<PdfPageDto>> pageQueueProvider,
            TaskExecutor parserTaskExecutor) {

        this.pageProducerProvider = pageProducerProvider;
        this.pageConsumerProvider = pageConsumerProvider;
        this.pageQueueProvider = pageQueueProvider;
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void parseBook() {
        BlockingQueue<PdfPageDto> queue = pageQueueProvider.getObject();
        PdfPageProducer producer = pageProducerProvider.getObject(queue);
        PdfPageConsumer consumer = pageConsumerProvider.getObject(queue);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
