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
import ua.in.sz.english.dict2json.model.Word;
import ua.in.sz.english.dict2json.words.WordParser;

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
		} catch (DictionaryParseException e) {
			log.error(e.getMessage(), e);
		}
	}

	private static String doExecute(CommandLine commandLine) throws ParseException {
		String inputFilePath = getInputFilePath(commandLine);

		DictionaryParser dictionaryParser = new DictionaryParser(Paths.get(inputFilePath));
		List<String> lines = dictionaryParser.parse();

		WordParser wordParser = new WordParser();

		List<Word> definitions = lines.stream()
				.map(wordParser::parse)
				.collect(Collectors.toList());

		long validCount = definitions.stream().count();

		log.info("Valid word {} from total {}", validCount, definitions.size());

		definitions.stream()
//					.filter(definition -> definition.isValid())
				.limit(200)
				.map(DictionaryToJson::format)
				.forEach(log::info);

		return "OK";
	}

	private static String getInputFilePath(CommandLine commandLine) throws MissingArgumentException {
		List<String> args = commandLine.getArgList();

		if (CollectionUtils.size(args) <= 0) {
			throw new MissingArgumentException("No input text file name");
		}

		if (CollectionUtils.size(args) > 1) {
			throw new MissingArgumentException("Multiply input text file names is unsupported");
		}

		return args.get(0);
	}

	private static String format(Word word) {
		return word.getText();
	}

	private static Options options() {
		return new Options()
				.addOption(Option.builder("o").longOpt("output")
						.hasArg().argName("file name")
						.desc("output json file name").build());
	}

}
