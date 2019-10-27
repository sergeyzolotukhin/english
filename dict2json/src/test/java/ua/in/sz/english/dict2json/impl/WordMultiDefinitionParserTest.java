package ua.in.sz.english.dict2json.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Test
	void group() {
		String text = "1) one; 2) two;";
		Pattern pattern = Pattern.compile("(?=(\\d+\\)\\s+\\w+;))");

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			log.info("Group: {}", matcher.group(1));
		}
	}

	@Test
	void description() {
		String text = "1) несуществующий;";
		Pattern pattern = Pattern.compile("\\d+\\)\\s+[а-яА-Я]+;");

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			log.info("description: {}", matcher.group(0));
		}
	}

	@Test
	void descriptionList() {
		String text = "1) несуществующий, отсутствующий;";
		Pattern pattern = Pattern.compile("\\d+\\)\\s+([а-яА-Я]+,)*\\s+[а-яА-Я]+;");

		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			log.info("description: {}", matcher.group(0));
		}
	}

	private void print(WordDefinition definition) {
		log.info("word: {}", definition.getWord());

		log.info("partOfSpeech: {}", definition.getPartOfSpeech());
		log.info("transcription: {}", definition.getTranscription());
		log.info("descriptions: {}", definition.getDescriptionText());
	}
}