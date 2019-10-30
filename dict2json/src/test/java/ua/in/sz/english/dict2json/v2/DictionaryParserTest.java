package ua.in.sz.english.dict2json.v2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ua.in.sz.english.dict2json.v2.model.Definition;
import ua.in.sz.english.dict2json.v2.model.Word;

@Slf4j
class DictionaryParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/v2-definitions.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		DictionaryParser parser = new DictionaryParser();
		Word word = parser.parse(text);

		print(word);
	}

	private void print(Word definition) {
		log.info("word: {}", definition.getWord());

		for (Definition def : definition.getDefinitions()) {
			log.info("\t {}. {}", def.getNo(), def.getText());
		}
	}
}