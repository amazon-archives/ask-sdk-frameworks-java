package com.amazon.ask.interaction;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class Locales {
    public static final Locale en_US = Locale.forLanguageTag("en-US");
    public static final Locale en_GB = Locale.forLanguageTag("en-GB");
    public static final Locale fr_FR = Locale.forLanguageTag("fr-FR");
    public static final Locale de_DE = Locale.forLanguageTag("de-DE");

    public static List<Locale> values() {
        return Arrays.asList(en_US, en_GB, fr_FR, de_DE);
    }
}
