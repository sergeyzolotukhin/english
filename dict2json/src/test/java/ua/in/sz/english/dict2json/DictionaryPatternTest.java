package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.in.sz.english.dict2json.DictionaryPatterns.*;

@Slf4j
@Tag("DEV")
@DisplayName("Dictionary pattern development")
class DictionaryPatternTest {

	@Test
	void wordParse() {
		String text = "   aard-wolf ";

		boolean find = Pattern.compile(WORD).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void suffixParse() {
		String text = "    преподаватель   (     ница    )     ";

		boolean find = Pattern.compile(RU_WORD + RU_SUFFIX).matcher(text).find();
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
	void oneMeaningParse() {
		String text = " земляно'й волк ";

		Matcher matcher = Pattern.compile(MEANING).matcher(text);

		Assertions.assertTrue(matcher.find());
		Assertions.assertEquals("земляно'й волк", matcher.group(0));
	}

	@Test
	void customParse() {
		String text = "teratology [,tero 'totao^i] n тератоло'гия, наука, изу-ча'ющая врождённые уро'дства. ";

		Matcher matcher = Pattern.compile(WORD + TRANSCRIPTION + PART_OF_SPEECH +
				MEANING + "\\s*,\\s*" + MEANING + "\\s*,\\s*" + MEANING + END).matcher(text);
		boolean find = matcher.find();

		log.info("group: {}", matcher.group(4));

		Assertions.assertTrue(find, text);
	}

	@Test
	void sentenceParse() {
		String text = " aard-wolf ['a:d,wulfj] n земляно'й волк.";

		boolean find = Pattern.compile(WORD + TRANSCRIPTION + PART_OF_SPEECH + MEANING + END).matcher(text).find();
		Assertions.assertTrue(find, text);
	}

	@Test
	void repeatedMeaningsParse() {
//		String text = "teratology [,tero 'totao^i] n тератоло'гия, наука, изу-ча'ющая врождённые уро'дства. ";
		String text = "teratology [,tero 'totao^i] n изу-ча'ющая врождённые уро'дства. ";

		Matcher matcher = Pattern.compile(WORD + TRANSCRIPTION + PART_OF_SPEECH + MEANING + END).matcher(text);
		boolean find = matcher.find();

		log.info("group: {}", matcher.group(0));

		Assertions.assertTrue(find, text);
	}

	@Test
	void namedCaptureGroupParse() {
		String text = "teratology [terototaoi]  000-111-22-33 уро'дства. ";

		Matcher matcher = Pattern.compile("\\[(?<transcription>[a-zA-Z]+)]\\s*(?<phone>[0-9\\-]+)").matcher(text);
		boolean find = matcher.find();

		log.info("transcription: {}", matcher.group("transcription"));
		log.info("phone: {}", matcher.group("phone"));

		Assertions.assertTrue(find, text);
	}

}
