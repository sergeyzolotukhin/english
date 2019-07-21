package ua.in.sz.english.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import ua.in.sz.english.service.parser.book.PageDto;
import ua.in.sz.english.service.parser.book.TextWriter;
import ua.in.sz.english.service.parser.book.PdfBookParser;
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

    private final TaskExecutor parserTaskExecutor;

    @Autowired
    public BookParserService(TaskExecutor parserTaskExecutor) {
        this.parserTaskExecutor = parserTaskExecutor;
    }

    public void parseBook() {
        BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(100);
        PdfBookParser bookParser = new PdfBookParser(queue, bookPath);
        TextWriter textWriter = new TextWriter(queue, textPath);

        parserTaskExecutor.execute(bookParser);
        parserTaskExecutor.execute(textWriter);
    }

    public void parseText() {
        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(100);
        TextParser producer = new TextParser(queue, sentenceModel, textPath);
        SentenceWriter consumer = new SentenceWriter(queue, sentencePath);

        parserTaskExecutor.execute(producer);
        parserTaskExecutor.execute(consumer);
    }
}
