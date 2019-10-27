package ua.in.sz.english.dict2json.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;

@Slf4j
class WordMultiDefinitionParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/word-multi-definition.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		List<WordDefinition> definitions = WordMultiDefinitionParser.parse(text);

		definitions.forEach(this::print);

		Assertions.assertEquals(2, definitions.size(), "Invalid definitions:" + text);
	}

	private void print(WordDefinition definition) {
		log.info("word: {}", definition.getWord());

		log.info("partOfSpeech: {}", definition.getPartOfSpeech());
		log.info("transcription: {}", definition.getTranscription());
		log.info("descriptions: {}", definition.getDescriptionText());
	}
}