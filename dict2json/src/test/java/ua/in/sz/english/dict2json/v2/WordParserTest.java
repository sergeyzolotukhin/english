package ua.in.sz.english.dict2json.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

@Slf4j
class WordParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/word-multi-definition.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		final boolean support = WordParser.isSupport(text);
		Assertions.assertTrue(support, "Not supported text");

		List<Word> words = WordParser.parse(text);

		words.forEach(this::print);

//		Assertions.assertEquals(2, words.size(), "Invalid words:" + text);
	}

	private void print(Word definition) {
		log.info("word: {}", definition.getWord());
	}
}