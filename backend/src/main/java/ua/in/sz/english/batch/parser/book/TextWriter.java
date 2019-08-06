package ua.in.sz.english.batch.parser.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
public class TextWriter implements Runnable {
    private final Queue<Event> queue;
    private final String path;

    @Override
    public void run() {
        try (Writer writer = Files.newBufferedWriter(Paths.get(path))) {
            log.debug("Start write text: {}", path);

            doConsume(writer);

            log.debug("End write text: {}", path);
        } catch (InterruptedException | IOException e) {
            log.error("Interrupted", e);
        }
    }

    private void doConsume(Writer writer) throws InterruptedException, IOException {
        while (true) {
            Event event = queue.take();

            if (Events.END_EVENT.equals(event)) {
                break;
            }

            if (event instanceof PageDto) {
                doHandle(writer, (PageDto) event);
            }
        }
    }

    private void doHandle(Writer writer, PageDto page) throws IOException {
        writer.write(page.getText());

        if (log.isTraceEnabled()) {
            log.trace("Receive page: {}", page.getPageNo());
        }
    }
}
