package slda.hadoop;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.CharUtils;

/**
 * @author Khoa
 * Parsing the options from commandline argument in a similar way to bash.
 */
public class SimpleOptions {
	private static final CommandLineParser PARSER = new GnuParser();
	private Options opts = null;
	private CommandLine cmdline = null;
	
	public SimpleOptions(String options) {
		opts = new Options();
		for(int i=0; i<options.length(); i++) {
			char option = options.charAt(i);
			if(CharUtils.isAsciiAlpha(option)) {
				OptionBuilder opt = OptionBuilder.withArgName(String.valueOf(option))
						.withLongOpt(String.valueOf(option));
				//advance to next char for special control
				if(i < options.length() - 1) {
					char nextChar = options.charAt(i+1);
					if(":".contains(String.valueOf(nextChar))) {
						switch(nextChar) {
							case ':': //Has an argument
								opt.hasArg();
								break;
							default:
								//Do nothing if does not recognize
								break;
						}
						i++;
					} 
				}
				opts.addOption(opt.create());
			} else {
				throw new RuntimeException("Error creating options");
			}
		}
	}
	
	public SimpleOptions parse(String[] cmdArgs) {
		try {
			cmdline = PARSER.parse(opts, cmdArgs);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing the arguments", e);
		}
		
		return this;
	}
	
	public String getOption(String option) {
		return cmdline.getOptionValue(option);
	}
	
	public String getOption(char option) {
		return cmdline.getOptionValue(option);
	}
	
	public boolean hasOption(char option) {
		return cmdline.hasOption(option);
	}
	
	public boolean hasOption(String option) {
		return cmdline.hasOption(option);
	}
	
	public Map<String, String> getOptions() {
		Map<String, String> allOpts = new HashMap<String, String>();
		for(Option opt: cmdline.getOptions()) {
			allOpts.put(opt.getOpt(), opt.getValue());
		}
		return allOpts;
	}
	
	public String[] getOptionValues(String option) {
		return cmdline.getOptionValues(option);
	}
}
