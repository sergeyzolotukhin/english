package ua.in.sz.english.batch.parser.dictionary;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WordDefinitionTest {

	@Test
	void parse() {
		String text = "abate [эЪеп.] v " +
				"1) ослаблять, уменьшать, умерять; " +
				"2)  снижать (цену, налог и т. п.); " +
				"3) делать скидку; " +
				"4) уменьшаться; ослабева'ть; успокаиваться; утихать (о буре, эпидемии и т. п.); " +
				"5) притуплять (остриё), стёсывать (камень); " +
				"6) юр.  аннулировать, отменять, прекращать; " +
				"7) метал, отпускать (сталь).";

		WordDefinition definition = WordDefinition.parse(text);

		log.info("[{}] -> [{}] -> [{}]",
				definition.getWord(), definition.getTranscription(), definition.getPartOfSpeech());

		definition.getDescriptions().forEach(description -> log.info("\t[{}]", description));
	}
}