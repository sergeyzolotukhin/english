package ua.in.sz.english.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.book.PageDto;
import ua.in.sz.english.service.parser.book.TextWriter;
import ua.in.sz.english.service.parser.book.BookParser;
import ua.in.sz.english.service.parser.text.SentenceDto;
import ua.in.sz.english.service.parser.text.SentenceWriter;
import ua.in.sz.english.service.parser.text.TextParser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class BookParserService {
    @Value("${book.path}")
    private String bookPath;
    @Value("${text.path}")
    private String textPath;
    @Value("${sentence.path}")
    private String sentencePath;
    @Value("classpath:en-sent.bin")
    private Resource sentenceModel;
    @Value("${parser.queue.capacity:20}")
    private int queueCapacity;

    private final TaskExecutor asyncTaskExecutor;

    @Autowired
    public BookParserService(TaskExecutor asyncTaskExecutor) {
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    public void parseBook() {
        BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(queueCapacity);
        BookParser parser = new BookParser(queue, bookPath);
        TextWriter writer = new TextWriter(queue, textPath);

        asyncTaskExecutor.execute(parser);
        asyncTaskExecutor.execute(writer);
    }

    public void parseText() {
        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(queueCapacity);
        TextParser parser = new TextParser(queue, sentenceModel, textPath);
        SentenceWriter writer = new SentenceWriter(queue, sentencePath);

        asyncTaskExecutor.execute(parser);
        asyncTaskExecutor.execute(writer);
    }
}
