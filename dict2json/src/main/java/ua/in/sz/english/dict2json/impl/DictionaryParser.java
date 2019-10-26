package ua.in.sz.english.dict2json.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DictionaryParser {
	private static final String DESCRIPTION_START = "^[a-zA-Z].*";
	private static final String DESCRIPTION_END = ".*\\.$";

	private static final String PAGE_TAG = "^[a-zA-Z]*$";
	private static final String PAGE_NUMBER = "^[0-9]*$";

	private final String fileName;

	public List<String> parse() {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));

			List<String> result1 = removePageHeaderFotter(lines);
			return splitDescription(result1);
		} catch (IOException e) {
			throw new BaseDictionaryException(e.getMessage(), e);
		}
	}

	//=================================================================================================================
	// private methods
	//=================================================================================================================

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
