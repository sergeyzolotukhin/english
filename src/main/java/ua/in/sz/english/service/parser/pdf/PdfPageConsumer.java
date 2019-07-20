package ua.in.sz.english.service.parser.pdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PdfPageConsumer implements Runnable {
    private final BlockingQueue<PdfPageDto> queue;
    private final String path;

    @Override
    public void run() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            doConsume(writer);
        } catch (InterruptedException | IOException e) {
            log.error("Interrupted", e);
        }
    }

    private void doConsume(BufferedWriter writer) throws InterruptedException, IOException {
        while (true) {
            PdfPageDto page = queue.take();

            if (PdfPageDto.LAST == page.getPageNo()) {
                log.info("End parse pdf book: {}", page.getBookTitle());
                break;
            }

            writer.write(page.getText());

            if (log.isTraceEnabled()) {
                log.trace("Receive page: {}", page.getPageNo());
            }
        }
    }
}
