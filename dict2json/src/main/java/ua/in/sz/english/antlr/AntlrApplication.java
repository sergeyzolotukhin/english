package ua.in.sz.english.antlr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Optional;

@Slf4j
public class AntlrApplication {
	private static final String TEXT =
			"Serhij SAYS: /red/ message from serhij \n" +
			"Kolij SAYS: message from kolij \n";

	public static void main(String[] args) {
		ChatLexer lexer = new ChatLexer(CharStreams.fromString(TEXT));
		ChatParser parser = new ChatParser(new CommonTokenStream(lexer));

		ChatParser.ChatContext chat = parser.chat();

		for (ChatParser.LineContext lineCtx : chat.line()) {
			String name = lineCtx.name().WORD().getText();

			String color = Optional.ofNullable(lineCtx.color().WORD()).map(ParseTree::getText).orElse("None");

			log.info("Name: {}, color: {}", name, color);

			for (TerminalNode word : lineCtx.message().WORD()) {
				log.info("Word: {}", word.getText());
			}
		}
	}
}
