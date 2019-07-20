package ua.in.sz.english.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.pdf.PdfPageConsumer;
import ua.in.sz.english.service.parser.pdf.PdfPageDto;
import ua.in.sz.english.service.parser.pdf.PdfPageProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class BookParserService {
    @Value("${book.path}")
    private String bookPath;
    @Value("${page.path}")
    private String pagePath;

    private final ObjectProvider<PdfPageProducer> pageProducerProvider;
    private final ObjectProvider<PdfPageConsumer> pageConsumerProvider;
    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public BookParserService(
            ObjectProvider<PdfPageProducer> pageProducerProvider,
            ObjectProvider<PdfPageConsumer> pageConsumerProvider,
            TaskExecutor parserTaskExecutor) {

        this.pageProducerProvider = pageProducerProvider;
        this.pageConsumerProvider = pageConsumerProvider;
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void parseBook() {
        BlockingQueue<PdfPageDto> queue = new ArrayBlockingQueue<>(100);
        PdfPageProducer producer = pageProducerProvider.getObject(queue, bookPath);
        PdfPageConsumer consumer = pageConsumerProvider.getObject(queue, pagePath);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
