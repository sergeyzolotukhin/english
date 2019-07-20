package ua.in.sz.english.service.tokenizer.sentence;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class DefaultSentenceNormalizer implements Function<String, String> {
    private static final String NOT_ALNUM = "[^a-zA-Z0-9.\\-; ]+";

    @Override
    public String apply(String sentence) {
        return StringUtils.normalizeSpace(
                RegExUtils.replacePattern(sentence, NOT_ALNUM, StringUtils.EMPTY));
    }
}
