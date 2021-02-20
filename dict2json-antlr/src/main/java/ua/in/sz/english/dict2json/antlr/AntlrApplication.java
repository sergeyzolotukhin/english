package ua.in.sz.english.dict2json.antlr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.nio.charset.Charset;
import java.util.List;

// -Dfile.encoding=UTF-16
@Slf4j
public class AntlrApplication {
	private static final String TEXT = "" +
			"aard-wolf ['a:d,wulfj] n земляной волк.\n" +
			"abashment [s'beejrmnt] n смушение смуще'ние, замешательство.\n";

	public static void main(String[] args) {
		Charset charset = Charset.defaultCharset();
		log.info("The charset is [{}]", charset);
		System.out.println("Сергей =>" + TEXT);
		log.info("The text is [{}]", TEXT);

		DictionaryLexer lexer = new DictionaryLexer(CharStreams.fromString(TEXT));
		DictionaryParser parser = new DictionaryParser(new CommonTokenStream(lexer));

		List<DictionaryParser.DefinitionContext> definitions = parser.dictionary().definition();

		for (DictionaryParser.DefinitionContext definition : definitions) {
			String word = definition.word().getText();
			String partOfSpeech = definition.partOfSpeech().getText();
			String meaning = definition.meaning().getText();

			log.info("Word [{}] is part of speech [{}] and it have meaning [{}]",
					word, partOfSpeech, meaning);
		}
	}
}
