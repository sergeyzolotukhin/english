package ua.in.sz.english.dict2json.words.impl;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.words.Parser;

@Slf4j
public class UnformattedWordParser implements Parser {

	@Override
	public boolean isSupport(String text) {
		return true;
	}

	@Override
	public Word parse(String text) {
		log.warn("Unformatted text: {}", text);

		Word word = new Word();
		word.setText(text);
		return word;
	}
}