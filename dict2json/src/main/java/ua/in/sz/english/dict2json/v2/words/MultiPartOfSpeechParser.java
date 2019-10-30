package ua.in.sz.english.dict2json.v2.words;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.in.sz.english.dict2json.v2.Parser;
import ua.in.sz.english.dict2json.v2.model.Definition;
import ua.in.sz.english.dict2json.v2.model.Word;

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
@NoArgsConstructor
public class MultiPartOfSpeechParser implements Parser {

	private static final String MULTI_DEFINITION_PATTERN = WORD + DEFINITION_NO + PART_OF_SPEECH + TRANSCRIPTION;

	private static final Pattern MULTI_DEFINITION = Pattern.compile(MULTI_DEFINITION_PATTERN);
	private static final Pattern DEFINITION_NO_SPLIT = Pattern.compile("(?=(\\d+\\.))");
	private static final Pattern DEFINITION = Pattern.compile("(\\d+)\\.\\s*(.*)");
	private static final Pattern WORD_DEFINITION = Pattern.compile(WORD + "(.*)");

	public boolean isSupport(String text) {
		return MULTI_DEFINITION.matcher(text).find();
	}

	public Word parse(String text) {
		Matcher matcher = WORD_DEFINITION.matcher(text);

		Word word = new Word(text);
		if (matcher.find()) {
			word.setWord(matcher.group(1));
			word.setDefinitions(parseDefinitions(matcher.group(2)));

			return word;
		} else {
			throw new IllegalStateException(text);
		}
	}

	private List<Definition> parseDefinitions(String text) {
		List<Definition> definitions = new ArrayList<>();

		for (String definitionText : DEFINITION_NO_SPLIT.split(text)) {
			Matcher matcher = DEFINITION.matcher(definitionText);

			if (matcher.find()) {
				int no = Integer.parseInt(matcher.group(1));
				Definition def = new Definition(no, matcher.group(2));
				definitions.add(def);
			} else {
				throw new IllegalStateException(text);
			}
		}

		return definitions;
	}
}
