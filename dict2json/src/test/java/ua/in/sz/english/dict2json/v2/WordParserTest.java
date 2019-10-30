package ua.in.sz.english.dict2json.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@Slf4j
class WordParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/word-definition.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		Word word = WordParser.parse(text);

		print(word);

		Assertions.assertTrue(true, "Invalid word: " + word.getText());
	}

	private void print(Word definition) {
		log.info("word: {}", definition.getWord());
//		log.info("transcription: {}", definition.getTranscription());
//		log.info("partOfSpeech: {}", definition.getPartOfSpeech());
//		log.info("descriptions: {}", definition.getDescriptionText());
	}
}