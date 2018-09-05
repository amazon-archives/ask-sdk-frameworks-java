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
import com.amazon.ask.interaction.definition.SkillModel;
import com.amazon.ask.interaction.renderer.SkillModelRenderer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.amazon.ask.interaction.Utils.stringifyLocale;
import static com.amazon.ask.util.ValidationUtils.assertNotEmpty;
import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Generates localized interaction models for a {@link SkillApplication} and writes them
 * to a directory, creating it if necessary.
 */
public class Generator {
    private final ObjectWriter writer;
    private final SkillModelRenderer renderer;
    private final SkillApplication application;
    private final File destdir;
    private final List<Locale> locales;

    /**
     * @param application skill application being generated
     * @param destdir directory to write model artifacts
     * @param locales list of locales to generate models for
     * @throws IllegalArgumentException if application or detdir are null, or if locales is null or unit
     */
    public Generator(SkillApplication application, File destdir, List<Locale> locales) {
        this(
            new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .writer(Utils.PRETTY_PRINTER),
            new SkillModelRenderer(), application, destdir, locales);
    }

    public Generator(ObjectWriter writer, SkillModelRenderer renderer, SkillApplication application, File destdir, List<Locale> locales) {
        this.writer = assertNotNull(writer, "writer");
        this.renderer = assertNotNull(renderer, "renderer");
        this.application = assertNotNull(application, "application");
        this.destdir = assertNotNull(destdir, "destdir");
        this.locales = locales == null || locales.isEmpty()
            ? new ArrayList<>(application.getSkillModel().getInvocationNames().keySet())
            : locales;
    }

    /**
     * Generates localized interaction models for a {@link SkillApplication} and
     * writes them to a directory, creating it if necessary.
     *
     * @throws GeneratorException if there was an error generating or writing the model
     */
    public void generate() throws GeneratorException {
        try {
            SkillModel skillModel = application.getSkillModel();
            if (!destdir.exists()) {
                destdir.mkdirs();
            }
            for (Locale locale : locales) {
                File out = getDestFile(locale.toLanguageTag() + ".json");
                writer.writeValue(out, renderer.render(skillModel, locale));
            }
        } catch (IOException | RuntimeException ex) {
            throw new GeneratorException(ex);
        }
    }

    protected File getDestFile(String name) {
        return new File(destdir, name);
    }
}
