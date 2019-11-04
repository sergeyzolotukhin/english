package ua.in.sz.english.dict2json.word.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.Parser;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;

@Slf4j
class SinglePartOfSpeechWordParserTest {

	@Test
	void oneMeaning() {
		String text = "aard-wolf ['a:d,wulfj] n земляно'й волк.";

		Parser<Word> parser = new SinglePartOfSpeechWordParser();

		Assertions.assertTrue(parser.isSupport(text));

		Word word = parser.parse(text);

		Assertions.assertEquals("aard-wolf", word.getWord());
		Assertions.assertEquals(1, word.getDefinitions().size());

		Definition definition = word.getDefinitions().get(0);
		Assertions.assertEquals("'a:d,wulfj", definition.getTranscription());
		Assertions.assertEquals("n", definition.getPartOfSpeech());
		Assertions.assertEquals("земляно'й волк", definition.getMeanings().get(0));
	}

	@Test
	void manyMeaning() {
		String text = "teratology [,tero 'totao^i] n тератоло'гия, наука, изу-ча'ющая врождённые уро'дства. ";

		Parser<Word> parser = new SinglePartOfSpeechWordParser();
		Word word = parser.parse(text);

		Assertions.assertEquals("teratology", word.getWord());
		Assertions.assertEquals(3, word.getDefinitions().size());

		Assertions.assertEquals("тератоло'гия", word.getDefinitions().get(0).getText());
		Assertions.assertEquals("наука", word.getDefinitions().get(1).getText());
		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", word.getDefinitions().get(2).getText());
	}
}