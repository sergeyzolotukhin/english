package ua.in.sz.english.dict2json.word;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;

@Slf4j
@Disabled
class WordParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/word-parser.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		WordParser parser = new WordParser();
		Word word = parser.parse(text);

		print(word);
	}

	// ================================================================================================================
	// private methods
	// ================================================================================================================

	private void print(Word definition) {
		log.info("word: {}", definition.getWord());

		for (Definition def : definition.getDefinitions()) {
			log.info("\t {}. {}", def.getNo(), def.getText());
		}
	}
}