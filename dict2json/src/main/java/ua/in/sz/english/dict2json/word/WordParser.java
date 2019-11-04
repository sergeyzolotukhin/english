package ua.in.sz.english.dict2json.word;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.DictionaryParseException;
import ua.in.sz.english.dict2json.Parser;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.word.impl.SinglePartOfSpeechWordParser;
import ua.in.sz.english.dict2json.word.impl.UnformattedWordParser;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
public class WordParser {

	private List<Parser<Word>> parsers = getDefaultParsers();

	public Word parse(String text) {
		return parsers.stream()
				.filter(p -> p.isSupport(text))
				.map(p -> p.parse(text))
				.findFirst()
				.orElseThrow(() -> new DictionaryParseException(text));
	}

	private List<Parser<Word>> getDefaultParsers() {
		return Arrays.asList(
				new SinglePartOfSpeechWordParser(),
//				new MultiPartOfSpeechWordParser(),
				new UnformattedWordParser()
		);
	}
}
