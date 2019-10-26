package ua.in.sz.english.dict2json.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class WordParserTest {

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

		Word word = WordParser.parse(text);
		Assertions.assertNotNull(word);
	}
}