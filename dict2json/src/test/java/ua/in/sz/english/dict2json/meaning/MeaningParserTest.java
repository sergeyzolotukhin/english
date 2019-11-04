package ua.in.sz.english.dict2json.meaning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MeaningParserTest {
	@Test
	void oneMeaning() {
		String text = "     изу-ча'ющая врождённые уро'дства. ";

		MeaningParser parser = new MeaningParser();
		String meaning = parser.parse(text);

		Assertions.assertEquals("изу-ча'ющая врождённые уро'дства", meaning);
		log.info("Meaning: {}", meaning);
	}

	@Test
	void manyMeaning() {
		String text = "      тератоло'гия, наука, изу-ча'ющая врождённые уро'дства. ";

		MeaningParser parser = new MeaningParser();
		String meaning = parser.parse(text);

		log.info("Meaning: {}", meaning);
	}
}