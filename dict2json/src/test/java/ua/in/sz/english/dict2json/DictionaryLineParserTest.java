package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ua.in.sz.english.dict2json.words.WordParser;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;

@Slf4j
class DictionaryLineParserTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/definitions.txt", delimiter = '|')
	void parse(String text) {
		log.info("text: [{}]", text);

		WordParser parser = new WordParser();
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