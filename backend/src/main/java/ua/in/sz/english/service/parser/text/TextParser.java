package ua.in.sz.english.service.parser.text;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Setter
@RequiredArgsConstructor
public class TextParser implements Runnable {
    private static final String NOT_ALNUM = "[^a-zA-Z0-9.\\-; ]+";

    private final BlockingQueue<SentenceDto> queue;
    private final Resource sentenceModel;
    private final String path;

    @Override
    public void run() {
        log.info("Parse text book: {}", path);

        try (InputStream model = sentenceModel.getInputStream()) {
            SentenceDetectorME detector = new SentenceDetectorME(new SentenceModel(model));

            String text = new String(Files.readAllBytes(Paths.get(path)));
            for (String sentence : detector.sentDetect(text)) {

                String normalizedSentence = normalize(sentence);
                queue.put(new SentenceDto(path, normalizedSentence));

                if (log.isTraceEnabled()) {
                    log.trace("Sentence: {}", normalizedSentence);
                }
            }

            queue.put(new SentenceDto(path, SentenceDto.LAST));
        } catch (IOException | InterruptedException e) {
            log.error("Can't sentence parse", e);
        }
    }

    private String normalize(String sentence) {
        return StringUtils.normalizeSpace(
                RegExUtils.replacePattern(sentence, NOT_ALNUM, StringUtils.EMPTY));
    }
}
