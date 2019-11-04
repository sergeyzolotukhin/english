package ua.in.sz.english.dict2json.deprecate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.Parser;
import ua.in.sz.english.dict2json.model.Definition;
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.words.impl.SinglePartOfSpeechWordParser;

@Slf4j
class SinglePartOfSpeechWordParserTest {

	@Test
	void oneMeaning() {
		String text = "aard-wolf ['a:d,wulfj] n земляно'й волк.";

		Parser<Word> parser = new SinglePartOfSpeechWordParser();
		Word word = parser.parse(text);

		print(word);
	}

	@Test
	void manyMeaning() {
		String text = "teratology [,tero 'totao^i] n тератоло'гия, наука, изу-ча'ющая врождённые уро'дства. ";

		Parser<Word> parser = new SinglePartOfSpeechWordParser();
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