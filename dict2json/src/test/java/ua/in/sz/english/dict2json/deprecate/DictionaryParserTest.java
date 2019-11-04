package ua.in.sz.english.dict2json.deprecate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.in.sz.english.dict2json.DictionaryParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
class DictionaryParserTest {
	private static final String BASE_PATH = "src/test/resources";

	@Test
	void parse() {
		Path path = Paths.get(BASE_PATH, "dictionary.txt");
		DictionaryParser parser = new DictionaryParser(path);
		List<String> descriptions = parser.parse();

		descriptions.forEach(description -> log.info("{}", description));
	}
}