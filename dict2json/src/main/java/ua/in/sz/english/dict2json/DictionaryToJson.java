package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections4.CollectionUtils;
import ua.in.sz.english.dict2json.impl.BaseDictionaryException;
import ua.in.sz.english.dict2json.impl.DictionaryParser;
import ua.in.sz.english.dict2json.impl.Word;
import ua.in.sz.english.dict2json.impl.WordParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DictionaryToJson {
	public static void main(String[] args) {
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options(), args);

			String result = doExecute(commandLine);

			System.out.println(result);
		} catch (ParseException e) {
			System.out.println(e.getMessage());

			new HelpFormatter().printHelp("dict2json <input text file name>", options());
		} catch (BaseDictionaryException e) {
			log.error(e.getMessage(), e);
		}
	}

	private static String doExecute(CommandLine commandLine) throws ParseException {
		List<String> args = commandLine.getArgList();
		if (CollectionUtils.size(args) <= 0) {
			throw new MissingArgumentException("No input text file name");
		}

		if (CollectionUtils.size(args) > 1) {
			throw new MissingArgumentException("Multiply input text file names is unsupported");
		}

		try {
			Path path = Paths.get(args.get(0));
			DictionaryParser dictionaryParser = new DictionaryParser(path);
			List<String> words = dictionaryParser.parse();

			List<Word> words1 = words.stream().map(WordParser::parse).collect(Collectors.toList());

			words1.stream().limit(30).map(DictionaryToJson::format).forEach(log::info);

			return "OK";
		} catch (BaseDictionaryException e) {
			log.error(e.getMessage(), e);

			return "KO";
		}
	}

	private static String format(Word word) {
		return word.getWord() + " - " +
				(word.getDescriptions() != null && word.getDescriptions().size() > 0
						? word.getDescriptions().get(0) :
						"null");
	}

	private static Options options() {
		return new Options()
				.addOption(Option.builder("o").longOpt("output")
						.hasArg().argName("file name")
						.desc("output json file name").build());
	}

}
