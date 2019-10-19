package ua.in.sz.english.batch.parser.book;


import java.util.function.Consumer;

public interface ConsumerWriter<T> extends Consumer<T> {
	void start();

	void stop();
}
