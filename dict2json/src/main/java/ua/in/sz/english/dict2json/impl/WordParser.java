package ua.in.sz.english.dict2json.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@AllArgsConstructor
public class WordParser {

	private static final String WORD_PATTERN = "^([^\\[]*)";
	private static final String TRANSCRIPTION_PATTERN = "(\\[\\S*]\\s*)";
	private static final String PART_OF_SPEECH_PATTERN = "(\\S+\\s+)";
	private static final String DESCRIPTION_LIST_PATTERN = "(.*)";

	private static Pattern DESCRIPTION_NO_PATTERN = Pattern.compile("(\\d+\\s*\\)\\s*)");

	private static Pattern DEFINITION_PATTERN = Pattern.compile(
			WORD_PATTERN + TRANSCRIPTION_PATTERN + PART_OF_SPEECH_PATTERN + DESCRIPTION_LIST_PATTERN);

	public static Word parse(String text) {
		Matcher matcher = DEFINITION_PATTERN.matcher(text);
		if (matcher.find()) {
			String word = matcher.group(1);
			String transcription = matcher.group(2);
			String partOfSpeech = matcher.group(3);

			List<String> descriptions = Arrays.asList(DESCRIPTION_NO_PATTERN.split(matcher.group(4)));

			return new ValidWord(word, text, transcription, partOfSpeech, descriptions);
		}

		return new InvalidWord(text);
	}
}
