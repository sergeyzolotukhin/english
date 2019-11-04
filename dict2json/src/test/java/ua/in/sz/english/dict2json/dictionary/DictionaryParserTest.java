package ua.in.sz.english.dict2json.dictionary;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
class DictionaryParserTest {

	private static final String BASE_PATH = "src/test/resources";

	@Test
	void parse() {
		Path path = Paths.get(BASE_PATH, "dictionary-parser.txt");
		DictionaryParser parser = new DictionaryParser(path);
		List<String> descriptions = parser.parse();

		descriptions.forEach(description -> log.info("{}", description));
	}
}