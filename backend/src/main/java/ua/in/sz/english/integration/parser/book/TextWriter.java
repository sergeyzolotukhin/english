package ua.in.sz.english.integration.parser.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class TextWriter implements Runnable {
    private final BlockingQueue<PageDto> queue;
    private final String path;

    @Override
    public void run() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            log.debug("Start write text: {}", path);

            doConsume(writer);

            log.debug("End write text: {}", path);
        } catch (InterruptedException | IOException e) {
            log.error("Interrupted", e);
        }
    }

    private void doConsume(BufferedWriter writer) throws InterruptedException, IOException {
        while (true) {
            PageDto page = queue.take();

            if (PageDto.LAST == page.getPageNo()) {
                break;
            }

            writer.write(page.getText());

            if (log.isTraceEnabled()) {
                log.trace("Receive page: {}", page.getPageNo());
            }
        }
    }
}
