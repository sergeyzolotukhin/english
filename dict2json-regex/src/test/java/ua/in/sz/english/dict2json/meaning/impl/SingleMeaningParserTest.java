package ua.in.sz.english.dict2json.meaning.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.Parser;

import java.util.List;

class SingleMeaningParserTest {

	@Test
	void parse() {
		String text = "     изу-ча'ющая врождённые уро'дства     .";

		Parser<List<String>> parser = new SingleMeaningParser();

		Assertions.assertTrue(parser.isSupport(text));

		List<String> meanings = parser.parse(text);

		Assertions.assertEquals(1, meanings.size());
		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", meanings.get(0));
	}
}