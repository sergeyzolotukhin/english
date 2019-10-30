package ua.in.sz.english.dict2json;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DictionaryParser {
	private static final String DESCRIPTION_START = "^[a-zA-Z].*";
	private static final String DESCRIPTION_END = ".*\\.$";

	private static final String PAGE_TAG = "^[a-zA-Z]*$";
	private static final String PAGE_NUMBER = "^[0-9]*$";

	private final Path filePath;

	public List<String> parse() {
		try {
			List<String> lines = Files.readAllLines(filePath);

			return splitDescription(normalize(lines));
		} catch (IOException e) {
			throw new DictionaryParseException(e.getMessage(), e);
		}
	}

	//=================================================================================================================
	// private methods
	//=================================================================================================================

	private List<String> splitDescription(List<String> lines) {
		List<String> result = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < lines.size() - 1; i++) {
			String current = StringUtils.trim(lines.get(i));
			String next = StringUtils.trim(lines.get(i + 1));

			sb.append(normalize(current));

			if (current.matches(DESCRIPTION_END) && next.matches(DESCRIPTION_START)) {
				result.add(sb.toString());

				sb = new StringBuilder();
			}
		}

		result.add(sb.toString());
		return result;
	}

	private List<String> normalize(List<String> rawLines) {
		return rawLines.stream()
				.filter(skip(PAGE_NUMBER))
				.filter(skip(PAGE_TAG))
				.collect(Collectors.toList());
	}

	private Predicate<String> skip(String pattern) {
		return line -> !line.matches(pattern);
	}

	private String normalize(String text) {
		return text.replaceAll("\n", " ").trim();
	}
}
