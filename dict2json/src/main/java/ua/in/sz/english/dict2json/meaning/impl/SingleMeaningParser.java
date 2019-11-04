package ua.in.sz.english.dict2json.meaning.impl;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.DictionaryParseException;
import ua.in.sz.english.dict2json.Parser;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.DictionaryPatterns.END;
import static ua.in.sz.english.dict2json.DictionaryPatterns.MEANING;
import static ua.in.sz.english.dict2json.DictionaryPatterns.START;

@Slf4j
public class SingleMeaningParser implements Parser<List<String>> {
	private static final String REGEX = START + "\\s*" + MEANING + "\\s*" + END + "$";

	private final Pattern PATTERN = Pattern.compile(getPattern());

	public String getPattern() {
		return REGEX;
	}

	@Override
	public boolean isSupport(String text) {
		return PATTERN.matcher(text).find();
	}

	@Override
	public List<String> parse(String text) {
		Matcher matcher = PATTERN.matcher(text);

		if (matcher.find()) {
			return Collections.singletonList(matcher.group(1));
		} else {
			throw new DictionaryParseException(text);
		}
	}

	@Override
	public String toString() {
		return getPattern();
	}
}
