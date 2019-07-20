package ua.in.sz.english.parser.pdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class PdfPageConsumer implements Runnable {
    private final String path;
    private final BlockingQueue<PdfPageDto> queue;

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
                log.info("End process book: {}", page.getBookTitle());
                break;
            }

            writer.write(page.getText());

            if (log.isTraceEnabled()) {
                log.trace("Receive page: {}", page.getPageNo());
            }
        }
    }
}
