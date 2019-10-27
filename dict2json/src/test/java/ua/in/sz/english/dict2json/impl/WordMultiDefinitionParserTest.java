package ua.in.sz.english.dict2json.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.impl.WordMultiDefinitionParser.*;

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
	void definition() {
		String text = " 1. в [f'cebssnt] 1) несуществующий, отсутствующий;   2) рассеянный; ";
		Pattern pattern = Pattern.compile(
				STRONG_DEFINITION_NO_PATTERN +
						STRONG_PART_OF_SPEECH_PATTERN +
						STRONG_TRANSCRIPTION_PATTERN +
						LIST_ITEM_DESCRIPTION_PATTERN +
						LIST_ITEM_DESCRIPTION_PATTERN
		);

		Matcher matcher = pattern.matcher(text);
		Assertions.assertTrue(matcher.find(), "Text not match: [" + text + "]");

		log.info("Text: [" + matcher.group(0) + "]");
	}

	@Test
	void transcription() {
		String text = "  [f'cebssnt]   jkhkhkj   [aeb'sent]  gjhgjgjhg ";
		Pattern pattern = Pattern.compile(STRONG_TRANSCRIPTION_PATTERN);

		Matcher matcher = pattern.matcher(text);
		Assertions.assertTrue(matcher.find(), "Text not match: [" + text + "]");

		log.info("Text: [" + matcher.group(0) + "]");
	}

	@Test
	void singleDescription() {
		String text = "re/7, отлучиться; отсутствовать; to ~ oneself from smth. уклоняться от чего-л.";
		Pattern pattern = Pattern.compile(SINGLE_DESCRIPTION_PATTERN);

		Matcher matcher = pattern.matcher(text);
		Assertions.assertTrue(matcher.find(), "Text not match: " + text);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/description.txt", delimiter = '|')
	void listItemDescription(String text) {
		Pattern pattern = Pattern.compile(LIST_ITEM_DESCRIPTION_PATTERN);

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