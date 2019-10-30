package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.DictionaryPatterns.*;

@Slf4j
@Tag("DEV")
@DisplayName("Dictionary pattern development")
class PatternTest {

	@Test
	void wordParse() {
		String text = "   aard-wolf ";

		boolean find = Pattern.compile(WORD).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void transcriptionParse() {
		String text = " ['a:d,wulfj] ";

		boolean find = Pattern.compile(TRANSCRIPTION).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void partOfSpeechParse() {
		String text = " n ";

		boolean find = Pattern.compile(PART_OF_SPEECH).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void abbreviationParse() {
		String text = " imp. ";

		boolean find = Pattern.compile(ABBREVIATION).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void meaningParse() {
		String text = " земляно'й волк ";

		boolean find = Pattern.compile(MEANING).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void customParse() {
		String text = ".";

		boolean find = Pattern.compile("\\.").matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void sentenceParse() {
		String text = " aard-wolf ['a:d,wulfj] n земляно'й волк.";

		boolean find = Pattern.compile(WORD + TRANSCRIPTION + PART_OF_SPEECH + MEANING + "\\.").matcher(text).find();
		Assertions.assertTrue(find, text);
	}
}
