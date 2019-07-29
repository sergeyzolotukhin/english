package ua.in.sz.english.service.parser.text;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SentenceWriter implements Runnable {
    private final BlockingQueue<SentenceDto> queue;
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
            SentenceDto sentence = queue.take();

            if (SentenceDto.LAST.equals(sentence.getText())) {
                break;
            }

            writer.write(sentence.getText());
            writer.newLine();

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", sentence.getText());
            }
        }
    }
}
