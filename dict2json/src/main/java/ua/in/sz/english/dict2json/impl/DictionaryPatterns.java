package ua.in.sz.english.dict2json.impl;


public final class DictionaryPatterns {
	public static final String WORD = "^\\s*([a-zA-Z\\-]+)\\s*";
	public static final String ABBREVIATION = "\\s*([a-zA-Zа-яА-Я]+)\\.\\s*";
	public static final String DEFINITION_NO = "\\s*\\d+\\.\\s+";
	public static final String PART_OF_SPEECH = "\\s*([а-яА-Яa-zA-Z]+)\\s+";
	public static final String MEANING = "\\s*([а-яА-Я' ]+)\\s*";
	public static final String TRANSCRIPTION = "\\s*\\[([a-zA-Z':, ]+)]\\s*";
}
