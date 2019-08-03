package ua.in.sz.english.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import ua.in.sz.english.AppProps;
import ua.in.sz.english.integration.parser.BookFileMessageSource;
import ua.in.sz.english.service.parser.book.PageDto;

import java.io.File;

@Slf4j
@Configuration
public class IntegrationConfig {
    private final AppProps appProps;

    public IntegrationConfig(AppProps appProps) {
        this.appProps = appProps;
    }

    @Bean
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
        return new BookFileMessageSource(appProps);
    }

    @Bean
    @ServiceActivator(inputChannel = "pageChannel")
    public MessageHandler fileWritingMessageHandler() {
        return new MessageHandler() {
            private int count = 0;

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                PageDto payload = (PageDto) message.getPayload();

                count++;

                if (count % 100 == 0) {
                    log.info("Page found {}", count);
                }
            }
        };
    }
}
