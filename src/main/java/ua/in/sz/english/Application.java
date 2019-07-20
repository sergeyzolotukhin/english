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

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
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
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public BlockingQueue<SentenceDto> sentenceQueue() {
        return new ArrayBlockingQueue<>(100);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SentenceProducer sentenceProducer(BlockingQueue<SentenceDto> queue, SentenceNormalizer normalizer){
        return new SentenceProducer(queue, normalizer);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SentenceConsumer sentenceConsumer(BlockingQueue<SentenceDto> queue){
        return new SentenceConsumer(queue);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SentenceNormalizer sentenceNormalizer() {
        return new SentenceNormalizer();
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
}
