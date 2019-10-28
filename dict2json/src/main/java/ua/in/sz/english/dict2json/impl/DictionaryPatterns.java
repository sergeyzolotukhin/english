package ua.in.sz.english.dict2json.impl;


public final class DictionaryPatterns {
	public static final String WORD = "^\\s*(\\w*)\\s*";
	public static final String DEFINITION_NO = "\\s*\\d+\\.\\s+";
	public static final String PART_OF_SPEECH = "\\s*[а-яА-Я]+\\s+";
	public static final String TRANSCRIPTION = "\\s*\\[\\s*[a-zA-Z' ]+\\s*]\\s*";
}
