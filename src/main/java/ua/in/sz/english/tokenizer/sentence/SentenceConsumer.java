package ua.in.sz.english.tokenizer.sentence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SentenceConsumer implements Runnable {
    private final String path;
    private final BlockingQueue<SentenceDto> queue;

    @Override
    public void run() {
        try {
            while (true) {
                SentenceDto sentence = queue.take();

                if (SentenceDto.LAST.equals(sentence.getSentence())) {
                    log.info("End process sentences");
                    break;
                }

                if (log.isTraceEnabled()) {
                    log.trace("Sentence: {}", sentence.getSentence());
                }
            }
        } catch (InterruptedException e) {
            log.error("Can't process sentences", e);
        }
    }
}
