package ua.in.sz.english.dict2json;


public final class DictionaryPatterns {
	public static final String WORD = "^\\s*([a-zA-Z\\-]+)\\s*";
	public static final String ABBREVIATION = "\\s*([a-zA-Zа-яёА-ЯЁ]+)\\.\\s*";
	public static final String DEFINITION_NO = "\\s*\\d+\\.\\s+";
	public static final String PART_OF_SPEECH = "\\s*([а-яёА-ЯЁa-zA-Z]+)\\s+";
	public static final String MEANING = "([а-яёА-ЯЁ'\\-]+\\s+)*([а-яёА-ЯЁ'\\-]+)";
	public static final String TRANSCRIPTION = "\\s*\\[([a-zA-Z':, \\^]+)]\\s*";
	public static final String START = "^";
	public static final String END = "\\s*\\.\\s*";

	public static final String RU_WORD = "^\\s*([а-яёА-ЯЁ\\-]+)\\s*";
	public static final String RU_SUFFIX = "\\s*(\\(\\s*[а-яёА-ЯЁ]+\\s*\\))\\s*";
}
