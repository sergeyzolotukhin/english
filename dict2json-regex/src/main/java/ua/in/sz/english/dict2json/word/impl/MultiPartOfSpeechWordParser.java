package ua.in.sz.english.dict2json.word.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.DictionaryParseException;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.DictionaryPatterns.DEFINITION_NO;
import static ua.in.sz.english.dict2json.DictionaryPatterns.PART_OF_SPEECH;
import static ua.in.sz.english.dict2json.DictionaryPatterns.TRANSCRIPTION;
import static ua.in.sz.english.dict2json.DictionaryPatterns.WORD;

@Slf4j
@Getter
@NoArgsConstructor
public class MultiPartOfSpeechWordParser implements Parser<Word> {

	private static final String REGEX = WORD + DEFINITION_NO + PART_OF_SPEECH + TRANSCRIPTION;

	private final Pattern PATTERN = Pattern.compile(getPattern());

	private static final Pattern DEFINITION_NO_SPLIT = Pattern.compile("(?=(\\d+\\.))");
	private static final Pattern DEFINITION = Pattern.compile("(\\d+)\\.\\s*(.*)");
	private static final Pattern WORD_DEFINITION = Pattern.compile(WORD + "(.*)");

	public String getPattern() {
		return REGEX;
	}

	@Override
	public boolean isSupport(String text) {
		return PATTERN.matcher(text).find();
	}

	@Override
	public Word parse(String text) {
		Matcher matcher = WORD_DEFINITION.matcher(text);

		Word word = new Word();
		if (matcher.find()) {
			word.setText(matcher.group(0));
			word.setWord(matcher.group(1));

			word.setDefinitions(parseDefinitions(matcher.group(2)));

			return word;
		} else {
			throw new DictionaryParseException(text);
		}
	}

	private List<Definition> parseDefinitions(String text) {
		List<Definition> definitions = new ArrayList<>();

		for (String definitionText : DEFINITION_NO_SPLIT.split(text)) {
			Matcher matcher = DEFINITION.matcher(definitionText);

			if (matcher.find()) {
				Definition def = new Definition();
				def.setNo(Integer.parseInt(matcher.group(1)));
				def.setText(matcher.group(2));
				definitions.add(def);
			} else {
				throw new DictionaryParseException(text);
			}
		}

		return definitions;
	}
}
