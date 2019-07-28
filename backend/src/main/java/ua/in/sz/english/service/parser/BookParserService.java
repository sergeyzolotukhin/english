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
        createBookParseCommand(bookPath, textPath).run();
    }

    public void parseText() {
        createBookParseCommand(textPath, sentencePath).run();
    }

    // ================================================================================================================
    //
    // ================================================================================================================

    public Runnable createBookParseCommand(String bookPath, String textPath) {
        return new BookParseCommand(bookPath, textPath);
    }

    public Runnable createTextParseCommand(String textPath, String sentencePath) {
        return new TextParseCommand(textPath, sentencePath);
    }

    private class TextParseCommand implements Runnable {
        private final TextParser parser;
        private final SentenceWriter writer;

        public TextParseCommand(String textPath, String sentencePath) {
            BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(queueCapacity);
            parser = new TextParser(queue, sentenceModel, textPath);
            writer = new SentenceWriter(queue, sentencePath);
        }

        @Override
        public void run() {
            asyncTaskExecutor.execute(parser);
            asyncTaskExecutor.execute(writer);
        }
    }

    private class BookParseCommand implements Runnable {
        private final BookParser parser;
        private final TextWriter writer;

        public BookParseCommand(String bookPath, String textPath) {
            BlockingQueue<PageDto> queue = new ArrayBlockingQueue<>(queueCapacity);
            parser = new BookParser(queue, bookPath);
            writer = new TextWriter(queue, textPath);
        }

        @Override
        public void run() {
            asyncTaskExecutor.execute(parser);
            asyncTaskExecutor.execute(writer);
        }
    }
}
