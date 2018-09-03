package com.amazon.ask.models.data.source;

import com.amazon.ask.models.renderer.RenderContext;

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
