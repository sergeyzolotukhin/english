package ua.in.sz.english.dict2json.meaning.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.Parser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultiMeaningParserTest {

	@Test
	void parse() {
		String text = "      тератоло'гия, наука, изу-ча'ющая врождённые уро'дства   .";

		Parser<List<String>> parser = new MultiMeaningParser();

		Assertions.assertTrue(parser.isSupport(text));

		List<String> meanings = parser.parse(text);

		Assertions.assertEquals(3, meanings.size());
		Assertions.assertEquals("тератоло'гия", meanings.get(0));
		Assertions.assertEquals("наука", meanings.get(1));
		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", meanings.get(2));
	}
}