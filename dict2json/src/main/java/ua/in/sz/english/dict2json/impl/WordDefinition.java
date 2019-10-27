package ua.in.sz.english.dict2json.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@RequiredArgsConstructor
public class WordDefinition {
	private final String text;

	private String word;
	private String transcription;
	private String partOfSpeech;
	private String descriptionText;

	public boolean isValid() {
		return StringUtils.isNotBlank(word)
				&& StringUtils.isNotBlank(transcription)
				&& StringUtils.isNotBlank(partOfSpeech)
				&& StringUtils.isNotBlank(descriptionText);
	}
}
