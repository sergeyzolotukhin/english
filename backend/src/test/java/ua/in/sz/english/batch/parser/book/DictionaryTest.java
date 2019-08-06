package ua.in.sz.english.batch.parser.book;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
class DictionaryTest {

	private static final String DICTIONARY_FILE_NAME = "dictionary.txt";
	private static final String BASE_PATH = "src/test/resources";
	private static final String WORK_PATH = "work";

	private static final String DESCRIPTION_START = "^[a-zA-Z].*";
	private static final String DESCRIPTION_END = ".*\\.$";
	private static final String PAGE_TAG = "^[a-zA-Z]*$";
	private static final String PAGE_NUMBER = "^[0-9]*$";

	@Test
	void parse() {
		try {
			String text = new String(Files.readAllBytes(Paths.get(BASE_PATH, DICTIONARY_FILE_NAME)));

			List<String> lines = Arrays.asList(text.split("\n"));

			List<String> result1 = removePageHeaderFotter(lines);

			List<String> result = splitDescription(result1);

			Files.write(Paths.get(WORK_PATH, "dictionary-1.txt"), result);

			log.info("Text reader: {}", result.size());
			result.stream().limit(2)
					.forEach(s -> log.info("Text reader: {}", s));
		} catch (IOException e) {
			log.error("I can't parse dictionary", e);
		}
	}

	private List<String> splitDescription(List<String> result1) {
		List<String> result = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < result1.size() - 1; i++) {
			String current = StringUtils.trim(result1.get(i));
			String next = StringUtils.trim(result1.get(i + 1));

			sb.append(normalize(current));

			if (current.matches(DESCRIPTION_END) && next.matches(DESCRIPTION_START)) {
				result.add(sb.toString());

				sb = new StringBuilder();
			}
		}

		result.add(sb.toString());
		return result;
	}

	private List<String> removePageHeaderFotter(List<String> split) {
		List<String> result1 = new ArrayList<>();

		for (String s : split) {
			String current = StringUtils.trim(s);
			if (current.matches(PAGE_TAG) || current.matches(PAGE_NUMBER)) {
				continue;
			}
			result1.add(current);
		}
		return result1;
	}

	private String normalize(String current) {
		return current.replaceAll("\n", " ").trim();
	}
}
