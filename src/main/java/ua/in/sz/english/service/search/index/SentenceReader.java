package ua.in.sz.english.service.search.index;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.service.parser.text.SentenceDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Setter
@RequiredArgsConstructor
public class SentenceReader implements Runnable {
    private final BlockingQueue<IndexDto> queue;
    private final String path;

    @Override
    public void run() {
        log.info("Read sentences: {}", path);

        try {
            for (String sentence : Files.readAllLines(Paths.get(path))) {
                queue.put(new IndexDto(path, sentence));

                if (log.isTraceEnabled()) {
                    log.trace("Sentence: {}", sentence);
                }
            }

            queue.put(new IndexDto(path, SentenceDto.LAST));
        } catch (IOException | InterruptedException e) {
            log.error("Can't sentence read", e);
        }
    }
}
