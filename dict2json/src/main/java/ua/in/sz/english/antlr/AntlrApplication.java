package ua.in.sz.english.antlr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

@Slf4j
public class AntlrApplication {
	private static final String TEXT = "Serhij SAYS: /red/message/\n";

	public static void main(String[] args) {
		ChatLexer lexer = new ChatLexer(CharStreams.fromString(TEXT));
		ChatParser parser = new ChatParser(new CommonTokenStream(lexer));

		ParseTreeWalker walker = new ParseTreeWalker();
		ChatBaseListener listener = new ColorListener();
		walker.walk(listener, parser.chat());
	}

	private static final class ColorListener extends ChatBaseListener {
		@Override
		public void exitColor(ChatParser.ColorContext ctx) {
			log.info("Exit color: {}", ctx.WORD().getText());
		}
	}
}
