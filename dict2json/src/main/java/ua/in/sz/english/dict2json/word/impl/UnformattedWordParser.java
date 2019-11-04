package ua.in.sz.english.dict2json.word.impl;

import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.Parser;

@Slf4j
public class UnformattedWordParser implements Parser<Word> {

	@Override
	public String getPattern() {
		return ".*";
	}

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
