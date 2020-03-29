package ua.in.sz.english.dict2json;


public interface Parser<T> {
	String getPattern();

	boolean isSupport(String text);

	T parse(String text);
}
