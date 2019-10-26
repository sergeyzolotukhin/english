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
public class Word {
	private final String word;
	private String transcription;
	private String partOfSpeech;
	private List<String> descriptions;
}
