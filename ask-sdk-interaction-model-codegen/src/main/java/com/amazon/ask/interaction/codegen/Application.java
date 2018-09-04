package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.model.InteractionModel;
import com.amazon.ask.interaction.model.SkillModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * CLI for generating java code from a skill's localized {@link InteractionModel} json files.
 */
public class Application {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption("h", "help", false, "print this help message");
        OPTIONS.addOption("n", "namespace", true, "java namespace of generated code");
        OPTIONS.addOption("o", "output", true, "output path of generated artifacts");
        OPTIONS.addOption("s", "skill-name", true, "name of generated skill class implementing SkillApplication");
        OPTIONS.addOption("m", "model", true, "locale and model path(s), e.g. -m en_US=path/to/en_US_file.json");
    }

    private static final CommandLineParser PARSER = new GnuParser();
    private static final HelpFormatter HELP = new HelpFormatter();

    public static void main(String[] args) throws IOException {
        CommandLine commandLine;
        try {
            commandLine = PARSER.parse(OPTIONS, args);
        } catch (ParseException e) {
            HELP.printHelp("ask-codegen", OPTIONS);
            throw new RuntimeException(e);
        }

        if (commandLine.hasOption('h')) {
            HELP.printHelp("ask-codegen", OPTIONS);
        } else {
            String namespace = commandLine.getOptionValue('n');
            String output = commandLine.getOptionValue('o');
            String skillName = commandLine.getOptionValue('s');
            String[] models = commandLine.getOptionValues('m');

            validateArg(namespace, "namespace");
            validateArg(output, "output");
            validateArg(skillName, "skillName");
            validateArg(models, "models");
            if (models.length == 0) {
                throw new IllegalArgumentException("You must supply at least once locale's model");
            }

            Generator generator = new Generator(namespace, skillName);
            for (String model : models) {
                String[] parts = model.split("=");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid model parameter: " + model);
                }
                Locale locale = Locale.forLanguageTag(parts[0]);
                File file = new File(parts[1]);
                generator.parseModel(new LocalizedInteractionModel(MAPPER.readValue(file, SkillModel.class), locale));
            }
            generator.generate(new File(output));
        }
    }

    private static void validateArg(Object value, String name) {
        if (value == null) {
            HELP.printHelp("GeneratorMain", OPTIONS);
            throw new IllegalArgumentException(name + " is required");
        }
    }
}
