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
		String text = "123)     несуществующий    ;";
		Pattern pattern = Pattern.compile("\\d+\\)(\\s*[а-яА-Я]+\\s*,)*\\s*[а-яА-Я]+\\s*;");

		Matcher matcher = pattern.matcher(text);
		Assertions.assertTrue(matcher.find(), "Text not match: " + text);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/description.txt", delimiter = '|')
	void descriptionList(String text) {
		Pattern pattern = Pattern.compile(WordMultiDefinitionParser.DESCRIPTIONS_PATTERN);

		Matcher matcher = pattern.matcher(text);
		Assertions.assertTrue(matcher.find(), "Text not match: [" + text + "]");
	}

	private void print(WordDefinition definition) {
		log.info("word: {}", definition.getWord());

		log.info("partOfSpeech: {}", definition.getPartOfSpeech());
		log.info("transcription: {}", definition.getTranscription());
		log.info("descriptions: {}", definition.getDescriptionText());
	}
}