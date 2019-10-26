package ua.in.sz.english.dict2json.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@Slf4j
class WordParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/word.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		Word word = WordParser.parse(text);

		log.info("word:[{}]", word.getWord());
		log.info("description:");

		CollectionUtils.emptyIfNull(word.getDescriptions()).forEach(d -> log.info("\t{}", d));

		Assertions.assertNotNull(word);
	}
}