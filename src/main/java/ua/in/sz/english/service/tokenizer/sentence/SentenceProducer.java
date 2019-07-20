package ua.in.sz.english.service.tokenizer.sentence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class SentenceProducer implements Runnable {
    private static final String NLP_MODEL_PATH = "k:/projects/english/src/main/resources/en-sent.bin";

    private final BlockingQueue<SentenceDto> queue;
    private final Function<String, String> normalizer;
    private final String path;

    @Override
    public void run() {
        log.info("Parse text book: {}", path);

        try (InputStream model = new FileInputStream(NLP_MODEL_PATH)) {
            SentenceDetectorME detector = new SentenceDetectorME(new SentenceModel(model));

            String text = new String(Files.readAllBytes(Paths.get(path)));
            for (String sentence : detector.sentDetect(text)) {

                String normalizedSentence = normalizer.apply(sentence);
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
}
