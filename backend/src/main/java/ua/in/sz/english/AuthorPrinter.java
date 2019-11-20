package ua.in.sz.english;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorPrinter implements CommandLineRunner {
	@Value("${english.author}")
	private String author;

	@Override
	public void run(String... args) {
		log.info("Author: {}", author);
	}
}
