package ua.in.sz.english.tokenizer.sentence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class SentenceProducer implements Runnable {
    private static final String NLP_MODEL_PATH = "k:/projects/english/src/main/resources/en-sent.bin";
    private static final String NOT_ALNUM = "[^a-zA-Z0-9.\\-; ]+";

    private final String path;
    private final BlockingQueue<SentenceDto> queue;

    @Override
    public void run() {
        log.info("Parse sentence");

        try (InputStream model = new FileInputStream(NLP_MODEL_PATH)) {
            SentenceDetectorME detector = new SentenceDetectorME(new SentenceModel(model));

            String text = new String(Files.readAllBytes(Paths.get(path)));
            for (String sentence : detector.sentDetect(text)) {

                String normalizedSentence = normalizedString(sentence);
                queue.put(new SentenceDto(normalizedSentence));

                if (log.isTraceEnabled()) {
                    log.trace("Sentence: {}", normalizedSentence);
                }
            }

            queue.put(new SentenceDto(SentenceDto.LAST));
        } catch (IOException | InterruptedException e) {
            log.error("Can't sentence parse", e);
        }
    }

    private String normalizedString(String sentence) {
        return StringUtils.normalizeSpace(
                RegExUtils.replacePattern(sentence, NOT_ALNUM, StringUtils.EMPTY));
    }
}
