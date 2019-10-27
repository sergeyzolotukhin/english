package ua.in.sz.english.dict2json.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class WordMultiDefinitionParser {

	private static final String WORD_PATTERN = "^\\s*(\\w*)\\s*";

	private static final String DEFINITION_NO_PATTERN = "(\\d*\\.)\\s*";
	private static final String PART_OF_SPEECH_PATTERN = "(\\S+\\s+)";
	private static final String TRANSCRIPTION_PATTERN = "(\\[.*])\\s*";

	private static final String DESCRIPTION_LIST_PATTERN = "(.*)\\.\\s*$";

	static String STRONG_DEFINITION_NO_PATTERN = "\\s*\\d+\\.\\s+";
	static String STRONG_PART_OF_SPEECH_PATTERN = "\\s*[а-яА-Я]+\\s+";
	static String STRONG_TRANSCRIPTION_PATTERN = "\\s*\\[\\s*[a-zA-Z' ]+\\s*]\\s*";
	static String SINGLE_DESCRIPTION_PATTERN = "(\\s*[а-яА-Я]+\\s*,)*\\s*[а-яА-Я]+\\s*;";
	static String LIST_ITEM_DESCRIPTION_PATTERN = "\\s*\\d+\\)(\\s*[а-яА-Я]+\\s*,)*\\s*[а-яА-Я]+\\s*;";

	private static Pattern DEFINITION_PATTERN = Pattern.compile(
			WORD_PATTERN +
					DEFINITION_NO_PATTERN +
					PART_OF_SPEECH_PATTERN +
					TRANSCRIPTION_PATTERN +
					DESCRIPTION_LIST_PATTERN);

	public static List<WordDefinition> parse(String text) {
		List<WordDefinition> result = new ArrayList<>();

		Matcher matcher = DEFINITION_PATTERN.matcher(text);

		WordDefinition definition = new WordDefinition(text);
		if (matcher.find()) {
			definition.setWord(matcher.group(1));
			definition.setPartOfSpeech(matcher.group(3));
			definition.setTranscription(matcher.group(4));
			definition.setDescriptionText(matcher.group(5));
		}

		result.add(definition);

		return result;
	}
}
