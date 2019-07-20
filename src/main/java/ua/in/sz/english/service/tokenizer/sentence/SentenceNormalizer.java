package ua.in.sz.english.service.tokenizer.sentence;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SentenceNormalizer implements Function<String, String> {
    private static final String NOT_ALNUM = "[^a-zA-Z0-9.\\-; ]+";

    @Override
    public String apply(String sentence) {
        return StringUtils.normalizeSpace(
                RegExUtils.replacePattern(sentence, NOT_ALNUM, StringUtils.EMPTY));
    }
}
