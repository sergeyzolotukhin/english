package ua.in.sz.english.dict2json.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidWord implements Word {
	private final String text;
}
