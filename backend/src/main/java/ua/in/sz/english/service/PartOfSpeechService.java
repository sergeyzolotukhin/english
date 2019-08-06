package ua.in.sz.english.service;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.upperCase;

@Slf4j
@Service
public class PartOfSpeechService {
    private static final String NOUN_TAG = "N";
    private static final String VERB_TAG = "V";

    @Value("classpath:en-pos-maxent.bin")
    private Resource partOfSpeechModel;

    public List<String> tagging(List<String> sentences) {
        return sentences.stream()
                .map(this::tagging)
                .collect(Collectors.toList());
    }

    private String tagging(String sentence) {
        try {
            POSModel model = new POSModel(partOfSpeechModel.getInputStream());
            POSTagger tagger = new POSTaggerME(model);

            String[] tokens = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
            String[] tags = tagger.tag(tokens);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                String tag = tags[i];

                sb.append(token).append(StringUtils.SPACE);

                if (isNoun(tag)) {
                    sb.append(tag).append(StringUtils.SPACE);
                }

                if (isVerb(tag)) {
                    sb.append(tag).append(StringUtils.SPACE);
                }
            }

            return sb.toString();
        } catch (IOException e) {
            log.error("Can't tag tokens", e);
        }

        return sentence;
    }

    private boolean isNoun(String tag) {
        return upperCase(tag).startsWith(NOUN_TAG);
    }

    private boolean isVerb(String tag) {
        return upperCase(tag).startsWith(VERB_TAG);
    }
}
