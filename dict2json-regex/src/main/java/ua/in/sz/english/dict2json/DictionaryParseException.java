package ua.in.sz.english.dict2json;


import java.io.IOException;

public class DictionaryParseException extends RuntimeException {
	public DictionaryParseException(String text) {
		super(text);
	}

	public DictionaryParseException(String message, IOException e) {
		super(message, e);
	}
}
