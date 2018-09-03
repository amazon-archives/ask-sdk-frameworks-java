package com.amazon.ask.mvc.view.resolver.candidate;

import com.amazon.ask.model.RequestEnvelope;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Given a locale "en-US" from the request, a suffix of ".js" and a view name "view", the following locations will be tried:
 * <ul>
 * <li>view_en_US.js</li>
 * <li>view/en_US.js</li>
 * <li>view_en.js</li>
 * <li>view/en.js</li>
 * <li>view.js</li>
 * <li>view/global.js</li>
 * </ul>
 */
public class LocaleViewCandidateEnumerator implements ViewCandidateEnumerator {
    private static final Pattern LOCALE_PARSER = Pattern.compile("^([a-z]{2})\\-([A-Z]{2})$");

    @Override
    public Stream<String> enumerate(String viewName, RequestEnvelope requestEnvelope) {
        String locale = requestEnvelope.getRequest().getLocale();
        if (locale == null) {
            return Stream.of(
                viewName,
                String.format("%s/global", viewName));
        }
        Matcher matcher = LOCALE_PARSER.matcher(locale);

        if (matcher.matches()) {
            final String language = matcher.group(1);
            final String country = matcher.group(2);

            return Stream.of(
                String.format("%s_%s_%s", viewName, language, country),
                String.format("%s/%s_%s", viewName, language, country),
                String.format("%s_%s", viewName, language),
                String.format("%s/%s", viewName, language),
                viewName,
                String.format("%s/global", viewName));
        } else {
            throw new IllegalArgumentException("Invalid locale: " + locale);
        }
    }
}
