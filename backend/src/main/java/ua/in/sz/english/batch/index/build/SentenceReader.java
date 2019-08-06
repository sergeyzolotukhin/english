package ua.in.sz.english.batch.index.build;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Queue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ua.in.sz.english.batch.queue.Events.END_EVENT;
import static ua.in.sz.english.batch.queue.Events.START_EVENT;

@Slf4j
@Setter
@RequiredArgsConstructor
public class SentenceReader implements Runnable {
    private final Queue<Event> queue;
    private final String path;

    @Override
    public void run() {
        try {
            log.debug("Start read sentences: {}", path);

            queue.put(START_EVENT);

            doRead();

            queue.put(END_EVENT);

            log.debug("End read sentences: {}", path);
        } catch (IOException | InterruptedException e) {
            log.error("Can't sentence read", e);
        }
    }

    private void doRead() throws IOException, InterruptedException {
        for (String sentence : Files.readAllLines(Paths.get(path))) {
            queue.put(new SentenceIndexDto(path, sentence));

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", sentence);
            }
        }
    }
}
