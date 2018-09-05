/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.data.source;

import com.amazon.ask.interaction.renderer.RenderContext;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * Enumerates candidates like the {@link java.util.ResourceBundle} convention:
 * <ul>
 * <li>name_en_US.js</li>
 * <li>name/en_US.js</li>
 * <li>name_en.js</li>
 * <li>name/en.js</li>
 * <li>name.js</li>
 * <li>name/global.js</li>
 * </ul>
 */
public class LocaleResourceCandidateEnumerator implements ResourceCandidateEnumerator {
    private static final LocaleResourceCandidateEnumerator INSTANCE = new LocaleResourceCandidateEnumerator();

    public static LocaleResourceCandidateEnumerator getInstance() {
        return INSTANCE;
    }

    private LocaleResourceCandidateEnumerator() {}

    @Override
    public Stream<String> enumerate(String name, RenderContext<?> renderContext) {
        Locale locale = renderContext.getLocale();
        if (locale == null) {
            return Stream.of(
                name,
                String.format("%s/global", name));
        }

        final String language = locale.getLanguage();
        final String country = locale.getCountry();

        return Stream.of(
            String.format("%s_%s_%s", name, language, country),
            String.format("%s/%s_%s", name, language, country),
            String.format("%s_%s", name, language),
            String.format("%s/%s", name, language),
            name,
            String.format("%s/global", name));
    }
}
