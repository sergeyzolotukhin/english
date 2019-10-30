package ua.in.sz.english.dict2json.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@Slf4j
class WordParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/v2-definitions.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		Parser parser = new MultiPartOfSpeechParser();

		boolean support = parser.isSupport(text);
		Assertions.assertTrue(support, "Not supported text");

		Word word = parser.parse(text);

		print(word);

//		Assertions.assertEquals(2, words.size(), "Invalid words:" + text);
	}

	private void print(Word definition) {
		log.info("word: {}", definition.getWord());
	}
}