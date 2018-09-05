/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.build;

import com.amazon.ask.interaction.SkillApplication;
import com.amazon.ask.interaction.Utils;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main CLI program for generating interaction model files for a {@link SkillApplication}
 *
 * --classname  absolute class name of a {@link SkillApplication}
 * --destdir    directory to write artifacts to
 * --locale     locale (e.g. en_US) of model to generate. Can be repeated for multiple locales.
 */
public class GeneratorMain {
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption("c", "classname", true, "name of application class extending " + SkillApplication.class.getName());
        OPTIONS.addOption("d", "destdir", true, "directory of generated interaction model files");
        OPTIONS.addOption("l", "locale", true, "one or more locales (en_US, de_DE, etc) to generate models for");
    }

    private static final CommandLineParser PARSER = new GnuParser();
    private static final HelpFormatter HELP = new HelpFormatter();

    public static void main(String[] args) throws GeneratorException {
        new GeneratorMain(args).run();
    }

    private final String[] args;

    public GeneratorMain(String[] args) {
        this.args = args;
    }

    public void run() throws GeneratorException {
        CommandLine commandLine;
        try {
            commandLine = PARSER.parse(OPTIONS, args);
        } catch (ParseException e) {
            HELP.printHelp("build", OPTIONS);
            throw new RuntimeException(e);
        }

        String classname = commandLine.getOptionValue('c');
        String destdir = commandLine.getOptionValue('d');
        String[] localeStrings = commandLine.getOptionValues('l');

        validateArg(classname, "classname");
        validateArg(destdir, "destdir");

        SkillApplication application;
        try {
            application = (SkillApplication) Class.forName(classname).newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new GeneratorException(ex);
        }

        List<Locale> locales = null;
        if(localeStrings != null && localeStrings.length > 0) {
            locales = Arrays.stream(localeStrings)
                .map(Utils::parseLocale)
                .collect(Collectors.toList());
        }

        generate(application, new File(destdir), locales);
    }

    protected void generate(SkillApplication application, File destdir, List<Locale> locales) throws GeneratorException {
        new Generator(application, destdir, locales).generate();
    }

    private static void validateArg(Object value, String name) {
        if (value == null) {
            HELP.printHelp("GeneratorMain", OPTIONS);
            throw new IllegalArgumentException(name + " is required");
        }
    }
}
