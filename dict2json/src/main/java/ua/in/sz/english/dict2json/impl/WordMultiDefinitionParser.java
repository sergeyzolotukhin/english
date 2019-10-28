package ua.in.sz.english.dict2json.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.DEFINITION_NO;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.PART_OF_SPEECH;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.TRANSCRIPTION;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.WORD;

@Slf4j
@Getter
@AllArgsConstructor
public class WordMultiDefinitionParser {

	private static final Pattern MULTI_DEFINITION_PATTERN =
			Pattern.compile(WORD + DEFINITION_NO + PART_OF_SPEECH + TRANSCRIPTION);
	private static final Pattern p1 = Pattern.compile("(?=(\\d+\\.))");

	public static boolean isSupport(String text) {
		return MULTI_DEFINITION_PATTERN.matcher(text).find();
	}

	public static List<WordDefinition> parse(String text) {
		List<WordDefinition> result = new ArrayList<>();

		Matcher matcher = Pattern.compile(WORD + "(.*)").matcher(text);

		WordDefinition definition = new WordDefinition(text);
		if (matcher.find()) {
			String word = matcher.group(1);
			definition.setWord(word);

			log.info("word: {}", word);

			String definitionText = matcher.group(2);
			String[] definitions = p1.split(definitionText);
			for (String s : definitions) {
				log.info("def: {}", s);
			}
		}

		result.add(definition);

		return result;
	}
}
