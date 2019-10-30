package ua.in.sz.english.dict2json.v2;


import ua.in.sz.english.dict2json.v2.model.Word;

public interface Parser {
	boolean isSupport(String text);

	Word parse(String text);
}
