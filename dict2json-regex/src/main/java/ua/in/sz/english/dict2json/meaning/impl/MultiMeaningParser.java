package ua.in.sz.english.dict2json.meaning.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ua.in.sz.english.dict2json.DictionaryParseException;
import ua.in.sz.english.dict2json.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static ua.in.sz.english.dict2json.DictionaryPatterns.END;
import static ua.in.sz.english.dict2json.DictionaryPatterns.MEANING;
import static ua.in.sz.english.dict2json.DictionaryPatterns.START;

@Slf4j
public class MultiMeaningParser implements Parser<List<String>> {
	private static final String REGEX = START + "\\s+(\\s*" + MEANING + "\\s*,)+\\s*" + MEANING + "\\s*" + END +"$";

	private final Pattern PATTERN = Pattern.compile(getPattern());

	private static final Pattern MEANING_PATTERN = Pattern.compile(MEANING);

	@Override
	public String getPattern() {
		return REGEX;
	}

	@Override
	public boolean isSupport(String text) {
		return PATTERN.matcher(text).find();
	}

	@Override
	public List<String> parse(String text) {
		List<String> result = new ArrayList<>();

		Matcher matcher = MEANING_PATTERN.matcher(text);

		while (matcher.find()) {
			result.add(matcher.group(1));
		}

		if (isEmpty(result)) {
			throw new DictionaryParseException(text);
		}

		return result;
	}
}
