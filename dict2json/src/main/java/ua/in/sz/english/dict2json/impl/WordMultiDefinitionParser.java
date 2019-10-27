package ua.in.sz.english.dict2json.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@AllArgsConstructor
public class WordMultiDefinitionParser {

	static final String WORD_PATTERN = "^\\s*(\\w*)\\s*";

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

	private static Pattern MULTI_DEFINITION_PATTERN = Pattern.compile(
			WORD_PATTERN +
					STRONG_DEFINITION_NO_PATTERN + STRONG_PART_OF_SPEECH_PATTERN + STRONG_TRANSCRIPTION_PATTERN);

	public static boolean isSupport(String text) {
		return MULTI_DEFINITION_PATTERN.matcher(text).find();
	}

	private static Pattern p1 = Pattern.compile("(?=(\\d+\\.))");

	public static List<WordDefinition> parse(String text) {
		List<WordDefinition> result = new ArrayList<>();

		Matcher matcher = Pattern.compile(WORD_PATTERN + "(.*)").matcher(text);

		WordDefinition definition = new WordDefinition(text);
		if (matcher.find()) {
			String word = matcher.group(1);
			definition.setWord(word);

			log.info("word: {}", word);

			String descriptionText = matcher.group(2);
			String[] split = p1.split(descriptionText);
			for (String s : split) {
				log.info("split: [{}]", s);
			}
		}

		result.add(definition);

		return result;
	}
}
