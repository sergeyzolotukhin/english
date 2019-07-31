package ua.in.sz.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ua.in.sz.english.service.parser.book.PageDto;

import java.io.File;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String INPUT_DIR = "e:/_book/_development/!book";
    private static final String OUTPUT_DIR = "K:/projects/english/work/output";
    private static final String FILE_PATTERN = "*.pdf";

    @Bean
    public TaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("async-worker-");
        executor.initialize();
        return executor;
    }

    @Bean("bookChannel")
    public MessageChannel bookChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel pageChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "bookChannel",
            poller = @Poller(fixedDelay = "5000", taskExecutor = "asyncTaskExecutor"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(INPUT_DIR));
//        sourceReader.setLoggingEnabled(true);
        sourceReader.setUseWatchService(true);
        sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
        return sourceReader;
    }

    @Bean
    @ServiceActivator(inputChannel = "pageChannel")
    public MessageHandler fileWritingMessageHandler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                PageDto payload = (PageDto)message.getPayload();

                log.info("Page found {}", payload.getPageNo());
            }
        };
    }
}
