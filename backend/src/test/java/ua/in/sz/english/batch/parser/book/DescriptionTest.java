package ua.in.sz.english.batch.parser.book;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
class DescriptionTest {

	private static final int TRANSCRIPTION = 2;
	private static final int PART_OF_SPEECH = 6;
	private static final int DEFINITION = 7;

	private static Pattern DESCRIPTION_PATTERN = Pattern.compile(
			// word
			"^(\\S+)\\s*" +
					// transcription
					"((\\[)(\\S*)(]))\\s*" +
					// part of speech
					"(\\S+)\\s*" +
					// definition
					"([^;]+\\s*)+"
	);

	@Test
	void parse() {
		String description = "abate [эЪеп.] v " +
				"1) ослаблять, уменьшать, умерять; " +
				"2)  снижать (цену, налог и т. п.); " +
				"3) делать скидку; " +
				"4) уменьшаться; ослабева'ть; успокаиваться; утихать (о буре, эпидемии и т. п.); " +
				"5) притуплять (остриё), стёсывать (камень); " +
				"6) юр.  аннулировать, отменять, прекращать; " +
				"7) метал, отпускать (сталь).";

		Matcher matcher = DESCRIPTION_PATTERN.matcher(description);
		if (matcher.find()) {
			log.info("group count:[{}]", matcher.groupCount());

			log.info("word:[{}]", matcher.group(1));
			log.info("transcription:[{}]", matcher.group(TRANSCRIPTION + 2));
			log.info("part:[{}]", matcher.group(PART_OF_SPEECH));

			log.info("definition:[{}]", matcher.group(DEFINITION));
		} else {
			log.error("Not working");
		}
	}
}