package ua.in.sz.english.batch.parser.book;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class PlainTextWriter implements ConsumerWriter<PageDto> {
	private final String path;
	private Writer writer;

	public PlainTextWriter(String path) {
		this.path = path;
	}

	@Override
	@SneakyThrows
	public void start() {
		writer = Files.newBufferedWriter(Paths.get(path));
	}

	@Override
	@SneakyThrows
	public void accept(PageDto page) {
		writer.write(page.getText());
	}

	@Override
	@SneakyThrows
	public void stop() {
		writer.close();
	}
}
