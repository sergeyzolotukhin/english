package ua.in.sz.english.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.integration.parser.BookFileMessageSource;
import ua.in.sz.english.integration.parser.BookParserEndpoint;
import ua.in.sz.english.integration.parser.TextParserEndpoint;

import java.io.File;

@Slf4j
@Configuration
public class IntegrationConfig {
    private final AppProps appProps;

    public IntegrationConfig(AppProps appProps) {
        this.appProps = appProps;
    }

    @Bean
    public IntegrationFlow bookProcessingFlow() {
        return IntegrationFlows
                .from(bookDirectoryScanner())
                .channel(bookChannel())
                .handle(bookParser())
                .channel(pageChannel())
                .handle(textParser())
                .channel(nullChannel())
                .get();
    }

    private MessageSource<File> bookDirectoryScanner() {
        return new BookFileMessageSource(appProps);
    }

    private MessageChannel bookChannel() {
        return new QueueChannel();
    }

    @Bean
    public BookParserEndpoint bookParser() {
        return new BookParserEndpoint(bookParserTaskExecutor(), appProps);
    }

    private MessageChannel pageChannel() {
        return new QueueChannel();
    }

    @Bean
    public TextParserEndpoint textParser() {
        return new TextParserEndpoint(textParserTaskExecutor(), appProps);
    }

    private NullChannel nullChannel() {
        return new NullChannel();
    }

    // ================================================================================================================
    // infrastructure beans
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

//    @Bean
    private TaskExecutor textParserTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("text-parser-");
        executor.initialize();
        return executor;
    }
}
