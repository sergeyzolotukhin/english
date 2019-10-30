package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.words.Parser;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.words.impl.SinglePartOfSpeechWordParser;

@Slf4j
class WordParserTest {

	@Test
	void parse() {
		String text = "aard-wolf ['a:d,wulfj] n земляно'й волк.";

		Parser parser = new SinglePartOfSpeechWordParser();
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