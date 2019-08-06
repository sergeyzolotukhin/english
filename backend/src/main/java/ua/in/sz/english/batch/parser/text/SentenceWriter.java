package ua.in.sz.english.batch.parser.text;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
public class SentenceWriter implements Runnable {
    private final Queue<Event> queue;
    private final String path;

    @Override
    public void run() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            log.debug("Start write sentence: {}", path);

            doConsume(writer);

            log.debug("End write sentence: {}", path);
        } catch (InterruptedException | IOException e) {
            log.error("Can't write sentence", e);
        }
    }

    private void doConsume(BufferedWriter writer) throws InterruptedException, IOException {
        while (true) {
            Event event = queue.take();

            if (Events.END_EVENT.equals(event)) {
                break;
            }

            if (event instanceof SentenceDto) {
                doHandle(writer, (SentenceDto) event);
            }
        }
    }

    private void doHandle(BufferedWriter writer, SentenceDto sentence) throws IOException {
        writer.write(sentence.getText());
        writer.newLine();

        if (log.isTraceEnabled()) {
            log.trace("Sentence: {}", sentence.getText());
        }
    }
}
