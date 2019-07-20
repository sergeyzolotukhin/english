package ua.in.sz.english;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ua.in.sz.english.service.parser.pdf.PdfPageConsumer;
import ua.in.sz.english.service.parser.pdf.PdfPageDto;
import ua.in.sz.english.service.parser.pdf.PdfPageProducer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceConsumer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceDto;
import ua.in.sz.english.service.tokenizer.sentence.SentenceNormalizer;
import ua.in.sz.english.service.tokenizer.sentence.SentenceProducer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Slf4j
@SpringBootApplication
public class Application {
    private static final String PDF_BOOK_PATH = "e:/_book/_development/_book/domain-driven-design-distilled.pdf";
    private static final String TEXT_BOOK_PATH = "K:/projects/english/work/book.log";
    private static final String SENTENCE_BOOK_PATH = "K:/projects/english/work/sentence.log";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        pdfToText();
//        textToSentence();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public BlockingQueue<PdfPageDto> pageQueue() {
        return new ArrayBlockingQueue<>(100);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PdfPageProducer pageProducer(BlockingQueue<PdfPageDto> queue){
        return new PdfPageProducer(queue);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public PdfPageConsumer pageConsumer(BlockingQueue<PdfPageDto> queue){
        return new PdfPageConsumer(queue);
    }

    @Bean
    public TaskExecutor parserTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("page-parser-");
        executor.initialize();
        return executor;
    }

    private static void textToSentence() {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        Function<String, String> normalizer = new SentenceNormalizer();
        BlockingQueue<SentenceDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new SentenceProducer(TEXT_BOOK_PATH, queue, normalizer));
        consumerPool.submit(new SentenceConsumer(SENTENCE_BOOK_PATH, queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

    private static void pdfToText() {
        ExecutorService producerPool = Executors.newFixedThreadPool(1);
        ExecutorService consumerPool = Executors.newFixedThreadPool(1);

        BlockingQueue<PdfPageDto> queue = new ArrayBlockingQueue<>(100);
        producerPool.submit(new PdfPageProducer(queue));
        consumerPool.submit(new PdfPageConsumer(queue));

        producerPool.shutdown();
        consumerPool.shutdown();
    }

}
