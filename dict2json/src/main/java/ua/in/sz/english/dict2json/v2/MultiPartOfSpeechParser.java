package ua.in.sz.english.dict2json.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.DEFINITION_NO;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.PART_OF_SPEECH;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.TRANSCRIPTION;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.WORD;

@Slf4j
@Getter
@AllArgsConstructor
public class MultiPartOfSpeechParser implements Parser {

	private static final String MULTI_DEFINITION_PATTERN = WORD + DEFINITION_NO + PART_OF_SPEECH + TRANSCRIPTION;

	private static final Pattern MULTI_DEFINITION = Pattern.compile(MULTI_DEFINITION_PATTERN);
	private static final Pattern DEFINITION_NO_SPLIT = Pattern.compile("(?=(\\d+\\.))");
	private static final Pattern WORD_DEFINITION = Pattern.compile(WORD + "(.*)");

	public boolean isSupport(String text) {
		return MULTI_DEFINITION.matcher(text).find();
	}

	public Word parse(String text) {
		Matcher matcher = WORD_DEFINITION.matcher(text);

		Word definition = new Word(text);
		if (matcher.find()) {
			String word = matcher.group(1);
			definition.setWord(word);

			log.info("word: {}", word);

			String definitionText = matcher.group(2);
			String[] definitions = DEFINITION_NO_SPLIT.split(definitionText);
			for (String s : definitions) {
				log.info("def: {}", s);
			}

			return definition;
		}

		throw new  IllegalStateException(text);
	}
}
