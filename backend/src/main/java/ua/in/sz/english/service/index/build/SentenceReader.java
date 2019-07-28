package ua.in.sz.english.service.index.build;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.in.sz.english.service.parser.text.SentenceDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Setter
@RequiredArgsConstructor
public class SentenceReader implements Runnable {
    private final BlockingQueue<SentenceIndexDto> queue;
    private final String path;

    @Override
    public void run() {
        log.info("Read sentences: {}", path);

        try {
            for (String sentence : Files.readAllLines(Paths.get(path))) {
                queue.put(new SentenceIndexDto(path, sentence));

                if (log.isTraceEnabled()) {
                    log.trace("Sentence: {}", sentence);
                }
            }

            queue.put(new SentenceIndexDto(StringUtils.EMPTY, SentenceDto.LAST));
        } catch (IOException | InterruptedException e) {
            log.error("Can't sentence read", e);
        }
    }
}
