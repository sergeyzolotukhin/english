package ua.in.sz.english.dict2json.v2;


public interface Parser {
	boolean isSupport(String text);

	Word parse(String text);
}
