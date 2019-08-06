package ua.in.sz.english.batch.parser.text;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import ua.in.sz.english.batch.queue.Event;
import ua.in.sz.english.batch.queue.Events;
import ua.in.sz.english.batch.queue.Queue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Setter
@RequiredArgsConstructor
public class TextParser implements Runnable {
    private static final String NOT_ALNUM = "[^a-zA-Z0-9.\\-; ]+";

    private final Queue<Event> queue;
    private final Resource sentenceModel;
    private final String path;

    @Override
    public void run() {
        try (InputStream model = sentenceModel.getInputStream()) {
            log.debug("Start parse text: {}", path);

            queue.put(Events.START_EVENT);

            doParse(model);

            queue.put(Events.END_EVENT);

            log.debug("End parse text: {}", path);
        } catch (IOException | InterruptedException e) {
            log.error("Can't parse text", e);
        }
    }

    private void doParse(InputStream model) throws IOException, InterruptedException {
        SentenceDetectorME detector = new SentenceDetectorME(new SentenceModel(model));

        String text = new String(Files.readAllBytes(Paths.get(path)));
        for (String sentence : detector.sentDetect(text)) {

            String normalizedSentence = normalize(sentence);
            queue.put(new SentenceDto(path, normalizedSentence));

            if (log.isTraceEnabled()) {
                log.trace("Sentence: {}", normalizedSentence);
            }
        }
    }

    private String normalize(String sentence) {
        return StringUtils.normalizeSpace(
                RegExUtils.replacePattern(sentence, NOT_ALNUM, StringUtils.EMPTY));
    }
}
