package ua.in.sz.english.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.batch.index.IndexFactory;
import ua.in.sz.english.batch.index.build.SentenceProcessor;
import ua.in.sz.english.batch.log.LogJobExecutionListener;
import ua.in.sz.english.batch.parser.BookSource;
import ua.in.sz.english.batch.parser.book.BookProcessor;
import ua.in.sz.english.batch.parser.text.TextProcessor;

import java.io.File;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final AppProps appProps;
    private final JobBuilderFactory jobFactory;
    private StepBuilderFactory stepFactory;

    public BatchConfig(JobBuilderFactory jobFactory, StepBuilderFactory stepFactory, AppProps appProps) {
        this.jobFactory = jobFactory;
        this.stepFactory = stepFactory;
        this.appProps = appProps;
    }

    @Bean
    public Job bookSentenceIndexing() {
        return jobFactory.get("Book sentence indexing")
                .incrementer(new RunIdIncrementer())
                .listener(logJobListener())
                .flow(bookToText())
                .next(textToSentence())
                .next(sentenceToIndex())
                .end()
                .build();
    }

    @Bean
    public Step bookToText() {
        return stepFactory.get("Book to text translation")
                .<File, File>chunk(10)
                .reader(bookReader())
                .writer(bookParser())
                .build();
    }

    @Bean
    public Step textToSentence() {
        return stepFactory.get("Text to sentence translation")
                .<File, File>chunk(10)
                .reader(textReader())
                .writer(textParser())
                .build();
    }

    @Bean
    public Step sentenceToIndex() {
        return stepFactory.get("Sentence indexing")
                .<File, File>chunk(10)
                .reader(sentenceReader())
                .writer(sentenceParser())
                .build();
    }

    @Bean
    @StepScope
    public BookSource bookReader() {
        return new BookSource(appProps.getBookDirPath());
    }

    @Bean
    @StepScope
    public BookProcessor bookParser() {
        return new BookProcessor(bookParserTaskExecutor(), appProps);
    }

    @Bean
    @StepScope
    public BookSource textReader() {
        return new BookSource(appProps.getTextDirPath());
    }

    @Bean
    @StepScope
    public TextProcessor textParser() {
        return new TextProcessor(bookParserTaskExecutor(), appProps);
    }

    @Bean
    @StepScope
    public BookSource sentenceReader() {
        return new BookSource(appProps.getSentenceDirPath());
    }

    @Bean
    @StepScope
    public SentenceProcessor sentenceParser() {
        return new SentenceProcessor(bookParserTaskExecutor(), indexWriter(), appProps);
    }

    @Bean
    public IndexWriter indexWriter() {
        return IndexFactory.createIndexWriter(appProps.getIndexDirPath());
    }

    private LogJobExecutionListener logJobListener() {
        return new LogJobExecutionListener();
    }

    @Bean
    public AsyncListenableTaskExecutor bookParserTaskExecutor() {
        return createTaskExecutor("book-parser-");
    }

    private AsyncListenableTaskExecutor createTaskExecutor(String prefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix(prefix);
        executor.initialize();
        return executor;
    }
}
