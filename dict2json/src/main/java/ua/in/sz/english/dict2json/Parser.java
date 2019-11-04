package ua.in.sz.english.dict2json;


public interface Parser<T> {
	boolean isSupport(String text);

	T parse(String text);
}
