package ua.in.sz.english.dict2json.meaning.impl;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.Parser;

@Slf4j
public class MultiMeaningParser implements Parser<String> {

	@Override
	public boolean isSupport(String text) {
		return false;
	}

	@Override
	public String parse(String text) {
		return null;
	}
}
