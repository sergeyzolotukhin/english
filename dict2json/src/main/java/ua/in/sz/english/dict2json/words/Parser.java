package ua.in.sz.english.dict2json.words;


import ua.in.sz.english.dict2json.model.Word;

public interface Parser {
	boolean isSupport(String text);

	Word parse(String text);
}
