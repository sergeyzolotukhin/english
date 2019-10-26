package ua.in.sz.english.dict2json.impl;

public class BaseDictionaryException extends RuntimeException {
	BaseDictionaryException(String message) {
		super(message);
	}

	BaseDictionaryException(String message, Throwable t) {
		super(message, t);
	}
}
