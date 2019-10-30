package ua.in.sz.english.dict2json.v2.words;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.v2.Parser;
import ua.in.sz.english.dict2json.v2.model.Definition;
import ua.in.sz.english.dict2json.v2.model.Word;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.MEANING;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.PART_OF_SPEECH;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.TRANSCRIPTION;
import static ua.in.sz.english.dict2json.impl.DictionaryPatterns.WORD;

@Slf4j
@Getter
@NoArgsConstructor
public class SinglePartOfSpeechWordParser implements Parser {

	private static final String MULTI_DEFINITION_PATTERN = WORD + TRANSCRIPTION + PART_OF_SPEECH + MEANING + "\\.";

	private static final Pattern MULTI_DEFINITION = Pattern.compile(MULTI_DEFINITION_PATTERN);
	private static final Pattern DEFINITION_NO_SPLIT = Pattern.compile("(?=(\\d+\\.))");
	private static final Pattern DEFINITION = Pattern.compile("(\\d+)\\.\\s*(.*)");
	private static final Pattern WORD_DEFINITION = Pattern.compile(WORD + TRANSCRIPTION + PART_OF_SPEECH  + MEANING + "\\.");

	public boolean isSupport(String text) {
		return MULTI_DEFINITION.matcher(text).find();
	}

	public Word parse(String text) {
		Matcher matcher = MULTI_DEFINITION.matcher(text);

		Word word = new Word();
		if (matcher.find()) {
			word.setText(matcher.group(0));
			word.setWord(matcher.group(1));

			Definition def = new Definition();
			def.setTranscription(matcher.group(2));
			def.setPartOfSpeech(matcher.group(3));
			def.setMeanings(Collections.singletonList(matcher.group(4)));

			return word;
		} else {
			throw new IllegalStateException(text);
		}
	}
}
