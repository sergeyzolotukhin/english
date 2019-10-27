package ua.in.sz.english.dict2json.impl;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ValidWord implements Word {
	private final String word;
	private final String text;

	private String transcription;
	private String partOfSpeech;
	private List<String> descriptions;
}
