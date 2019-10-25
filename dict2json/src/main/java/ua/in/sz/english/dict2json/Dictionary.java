package ua.in.sz.english.dict2json;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class Dictionary {
	public static void main(String[] args) {
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine commandLine = parser.parse(options(), args);

			String result = doExecute(commandLine);

			Optional.ofNullable(result).ifPresent(System.out::println);
		} catch (ParseException e) {
			System.out.println(e.getMessage());

			new HelpFormatter().printHelp("dict2json -f <file name>", options());
		}
	}

	private static String doExecute(CommandLine commandLine) {
		List<String> args = Arrays.stream(commandLine.getArgs()).collect(Collectors.toList());

		List<String> opts = Arrays.stream(commandLine.getOptions())
				.map(o -> String.join("=", o.getOpt(), o.getValue()))
				.collect(Collectors.toList());

		return String.format("parse dictionary: %s; %s",
				String.join(",", args),
				String.join(",", opts));
	}

	private static Options options() {
		return new Options()
				.addOption(Option.builder("f").longOpt("file").required()
						.hasArg().argName("file name")
						.desc("input text file name").build())
				.addOption(Option.builder("o").longOpt("output")
						.hasArg().argName("file name")
						.desc("output json file name").build());
	}

}
