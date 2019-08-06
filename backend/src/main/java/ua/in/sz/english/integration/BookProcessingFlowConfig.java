package ua.in.sz.english.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.integration.parser.BookFileSource;
import ua.in.sz.english.integration.parser.book.BookProcessor;
import ua.in.sz.english.integration.index.build.SentenceProcessor;
import ua.in.sz.english.integration.parser.text.TextProcessor;

@Slf4j
@Configuration
public class BookProcessingFlowConfig {
    private final AppProps appProps;

    public BookProcessingFlowConfig(AppProps appProps) {
        this.appProps = appProps;
    }

    @Bean
    public IntegrationFlow bookProcessingFlow() {
        return IntegrationFlows
                .from(bookSource())
                .channel(bookChannel())
                .handle(bookProcessor())
                .channel(pageChannel())
                .handle(textProcessor())
                .channel(sentenceChannel())
                .handle(sentenceProcessor())
                .get();
    }

    private BookFileSource bookSource() {
        return new BookFileSource(appProps);
    }

    @Bean
    public BookProcessor bookProcessor() {
        return new BookProcessor(bookParserTaskExecutor(), appProps);
    }

    @Bean
    public TextProcessor textProcessor() {
        return new TextProcessor(textParserTaskExecutor(), appProps);
    }

    @Bean
    public SentenceProcessor sentenceProcessor() {
        return new SentenceProcessor(sentenceParserTaskExecutor(), appProps);
    }

    // ================================================================================================================
    // Channels
    // ================================================================================================================

    private MessageChannel sentenceChannel() {
        return new QueueChannel();
    }

    private MessageChannel bookChannel() {
        return new QueueChannel();
    }

    private MessageChannel pageChannel() {
        return new QueueChannel();
    }

    // ================================================================================================================
    // infrastructure
    // ================================================================================================================

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(500).get();
    }

    @Bean
    public TaskExecutor bookParserTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("book-parser-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskExecutor textParserTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("text-parser-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskExecutor sentenceParserTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("sentence-parser-");
        executor.initialize();
        return executor;
    }

}
