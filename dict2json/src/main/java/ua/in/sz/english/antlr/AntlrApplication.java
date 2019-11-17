package ua.in.sz.english.antlr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

@Slf4j
public class AntlrApplication {
	private static final String TEXT =
			"aard-wolf ['a:d,wulfj] n земляной волк.\n";

	public static void main(String[] args) {
		ChatLexer lexer = new ChatLexer(CharStreams.fromString(TEXT));
		ChatParser parser = new ChatParser(new CommonTokenStream(lexer));

		ChatParser.LineContext lineCtx = parser.line();

		log.info("Parsed");
	}
}
