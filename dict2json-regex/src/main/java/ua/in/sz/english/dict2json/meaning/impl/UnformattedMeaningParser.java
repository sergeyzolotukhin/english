package ua.in.sz.english.dict2json.meaning.impl;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.Parser;

import java.util.Collections;
import java.util.List;

@Slf4j
public class UnformattedMeaningParser implements Parser<List<String>> {
	@Override
	public String getPattern() {
		return ".*";
	}

	@Override
	public boolean isSupport(String text) {
		return true;
	}

	@Override
	public List<String> parse(String text) {
		log.warn("Unformatted text: {}", text);
		return Collections.singletonList(text);
	}
}
