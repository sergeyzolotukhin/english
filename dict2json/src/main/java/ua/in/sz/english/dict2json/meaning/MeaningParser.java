package ua.in.sz.english.dict2json.meaning;

import ua.in.sz.english.dict2json.DictionaryParseException;
import ua.in.sz.english.dict2json.Parser;
import ua.in.sz.english.dict2json.meaning.impl.MultiMeaningParser;
import ua.in.sz.english.dict2json.meaning.impl.SingleMeaningParser;
import ua.in.sz.english.dict2json.meaning.impl.UnformattedMeaningParser;

import java.util.Arrays;
import java.util.List;

public class MeaningParser {
	private List<Parser<List<String>>> parsers = getDefaultParsers();

	public List<String> parse(String text) {
		return parsers.stream()
				.filter(p -> p.isSupport(text))
				.map(p -> p.parse(text))
				.findFirst()
				.orElseThrow(() -> new DictionaryParseException(text));
	}

	private List<Parser<List<String>>> getDefaultParsers() {
		return Arrays.asList(
				new SingleMeaningParser(),
				new MultiMeaningParser(),
				new UnformattedMeaningParser()
		);
	}
}
