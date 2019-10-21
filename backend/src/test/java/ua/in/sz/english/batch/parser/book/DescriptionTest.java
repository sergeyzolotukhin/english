package ua.in.sz.english.batch.parser.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
class DescriptionTest {

	private static final String WORD_PATTERN = "^(\\S+\\s*)";
	private static final String TRANSCRIPTION_PATTERN = "(\\[\\S*]\\s*)";
	private static final String PART_OF_SPEECH_PATTERN = "(\\S+\\s+)";
	private static final String DESCRIPTION_LIST_PATTERN = "(.*)";

	private static Pattern DEFINITION_PATTERN = Pattern.compile(
			WORD_PATTERN + TRANSCRIPTION_PATTERN + PART_OF_SPEECH_PATTERN + DESCRIPTION_LIST_PATTERN);

	private static Pattern DESCRIPTION_PATTERN = Pattern.compile("(\\d+[^\\d]+)\\s*");

	@Test
	void parse() {
		String text = "abate [эЪеп.] v " +
				"1) ослаблять, уменьшать, умерять; " +
				"2)  снижать (цену, налог и т. п.); " +
				"3) делать скидку; " +
				"4) уменьшаться; ослабева'ть; успокаиваться; утихать (о буре, эпидемии и т. п.); " +
				"5) притуплять (остриё), стёсывать (камень); " +
				"6) юр.  аннулировать, отменять, прекращать; " +
				"7) метал, отпускать (сталь).";

		Optional<DefinitionDto> definition1 = parseDefinition(text);

		if(definition1.isPresent()) {
			DefinitionDto definition = definition1.get();

			log.info("[{}] -> [{}] -> [{}]",
					definition.getWord(), definition.getTranscription(), definition.getPartOfSpeech());

			definition.getDescriptions().forEach(description -> log.info("\t: [{}]", description));
		} else {
			log.error("I can not parse definition: [{}]", text);
		}
	}

	private Optional<DefinitionDto> parseDefinition(String text) {
		Matcher matcher = DEFINITION_PATTERN.matcher(text);
		if (matcher.find()) {
			String word = matcher.group(1);
			String transcription = matcher.group(2);
			String partOfSpeech = matcher.group(3);
			String description = matcher.group(4);

			return Optional.of(
					DefinitionDto.of(word, transcription, partOfSpeech,
							parseDescriptions(description)));
		} else {
			return Optional.empty();
		}
	}

	private List<String> parseDescriptions(String descriptions) {
		List<String> result = new ArrayList<>();

		Matcher matcher = DESCRIPTION_PATTERN.matcher(descriptions);
		while (matcher.find()) {
			result.add(matcher.group(1));
		}

		return result;
	}

	@Getter
	@AllArgsConstructor(staticName = "of")
	private static class DefinitionDto {
		private final String word;
		private final String transcription;
		private final String partOfSpeech;
		private final List<String> descriptions;
	}
}