package ua.in.sz.english.dict2json.meaning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.Parser;
import ua.in.sz.english.dict2json.meaning.impl.MultiMeaningParser;

import java.util.List;

@Slf4j
class MeaningParserTest {
	@Test
	void oneMeaning() {
		String text = "     изу-ча'ющая врождённые уро'дства     .";

		MeaningParser parser = new MeaningParser();
		List<String> meanings = parser.parse(text);

		Assertions.assertEquals(1, meanings.size());
		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", meanings.get(0));

		log.info("Meaning: {}", meanings);
	}

	@Test
	void manyMeaning() {
		String text = "      тератоло'гия, наука, изу-ча'ющая врождённые уро'дства   .";

		MeaningParser parser = new MeaningParser();
		List<String> meanings = parser.parse(text);

		Assertions.assertEquals(3, meanings.size());
		Assertions.assertEquals("тератоло'гия", meanings.get(0));
		Assertions.assertEquals("наука", meanings.get(1));
		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", meanings.get(2));
	}
}