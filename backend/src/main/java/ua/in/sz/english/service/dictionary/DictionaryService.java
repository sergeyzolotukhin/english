package ua.in.sz.english.service.dictionary;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.unmodifiableMap;

@Slf4j
@Service
public class DictionaryService {
	private static Pattern WORD_PATTERN = Pattern.compile("^(\\S+)(.*)");

	@Value("classpath:dictionary.txt")
	private Resource dictionary;

	private Map<String, String> dictionaryCache;

	@PostConstruct
	private void setup() throws IOException {
		log.trace("Dictionary loading");

		List<String> lines = loadDictionary();
		dictionaryCache = unmodifiableMap(parseDictionary(lines));

		log.trace("Dictionary loaded");
	}

	public String definition(String word) {
		Assert.hasText(word, "Empty word");

		String key = normalizeWord(word);

		return Optional.ofNullable(dictionaryCache.get(key))
				.orElseThrow(WordNotFoundException::new);
	}

	// ================================================================================================================
	// private methods
	// ================================================================================================================

	private List<String> loadDictionary() throws IOException {
		return IOUtils.readLines(dictionary.getInputStream(), StandardCharsets.UTF_8);
	}

	private Map<String, String>  parseDictionary(List<String> lines) {
		Map<String, String> result = new HashMap<>();

		for (String line : lines) {
			Pair<String, String> pair = parseDefinition(line);

			String word = pair.getKey();
			String description = pair.getValue();

			result.put(word, description);
		}

		return result;
	}

	private Pair<String, String> parseDefinition(String line) {
		try {
			Matcher matcher = WORD_PATTERN.matcher(line);
			Assert.isTrue(matcher.find(), String.format("Incorrect line [%s]", line));

			String word = normalizeWord(matcher.group(1));
			String description = matcher.group(2);

			return Pair.of(word, description);
		} catch (IllegalStateException e) {
			log.error("Definition parsing exception: [{}]", line);

			throw e;
		}
	}

	private String normalizeWord(String word) {
		return StringUtils.lowerCase(StringUtils.trim(word));
	}
}
