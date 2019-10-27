package ua.in.sz.english.dict2json.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class WordDefinitionParser {

	private static final String WORD_PATTERN = "^\\s*(\\S*[ -]*\\S*)\\s*";
	private static final String TRANSCRIPTION_PATTERN = "(\\[.*])\\s*";
	private static final String PART_OF_SPEECH_PATTERN = "(\\S+\\s+)";
	private static final String DESCRIPTION_LIST_PATTERN = "(.*)\\.\\s*$";

	private static Pattern DEFINITION_PATTERN = Pattern.compile(
			WORD_PATTERN + TRANSCRIPTION_PATTERN + PART_OF_SPEECH_PATTERN + DESCRIPTION_LIST_PATTERN);

	public static WordDefinition parse(String text) {
		Matcher matcher = DEFINITION_PATTERN.matcher(text);

		WordDefinition definition = new WordDefinition(text);
		if (matcher.find()) {
			definition.setWord(matcher.group(1));
			definition.setTranscription(matcher.group(2));
			definition.setPartOfSpeech(matcher.group(3));
			definition.setDescriptionText(matcher.group(4));
		}

		return definition;
	}
}
