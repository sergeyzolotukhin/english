package ua.in.sz.english.dict2json.impl;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Word {
	private final String word;
	private final String transcription;
	private final String partOfSpeech;
	private final List<String> descriptions;
}
