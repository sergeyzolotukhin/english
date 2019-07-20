package ua.in.sz.english.tokenizer.sentence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SentenceConsumer implements Runnable {
    private final String path;
    private final BlockingQueue<SentenceDto> queue;

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
            SentenceDto sentence = queue.take();

            if (SentenceDto.LAST.equals(sentence.getText())) {
                log.info("End parse text book: {}", path);
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
