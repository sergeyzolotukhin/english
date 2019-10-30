package ua.in.sz.english.dict2json.v2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.v2.model.Word;
import ua.in.sz.english.dict2json.v2.words.MultiPartOfSpeechParser;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
public class WordParser {

	private List<Parser> parsers = getDefaultParsers();

	public Word parse(String text) {
		return parsers.stream()
				.filter(p -> p.isSupport(text))
				.map(p -> p.parse(text))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException(text));
	}

	private List<Parser> getDefaultParsers() {
		return Arrays.asList(
				new MultiPartOfSpeechParser()
		);
	}
}
