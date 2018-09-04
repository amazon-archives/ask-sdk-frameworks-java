package com.amazon.ask.models;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains static helper methods add no better home.
 */
public class Utils {
    public static final Predicate<HandlerInput> TRUE = input -> true;
    public static final Predicate<HandlerInput> FALSE = input -> false;

    public static final DefaultPrettyPrinter PRETTY_PRINTER = new DefaultPrettyPrinter();
    static {
        PRETTY_PRINTER.indentArraysWith(new DefaultPrettyPrinter.Indenter() {
            @Override
            public void writeIndentation(JsonGenerator jsonGenerator, int i) throws IOException {
                jsonGenerator.writeRaw('\n');
                for (int j = 0; j < i; j++) {
                    jsonGenerator.writeRaw("  ");
                }
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
    }

    public static <K, V> Map<K, Set<V>> unmodifiableMapOfSets(Map<K, Set<V>> map) {
        return map.entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> Collections.unmodifiableSet(e.getValue())));
    }

    public static <K, V> void mergeMapOfSets(Map<K, Set<V>> from, Map<K, Set<V>> into) {
        for (Map.Entry<K, Set<V>> entry : from.entrySet()) {
            if (into.containsKey(entry.getKey())) {
                into.get(entry.getKey()).addAll(entry.getValue());
            } else {
                into.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public static Locale parseLocale(String string) {
        Locale locale = Locale.forLanguageTag(string.replaceAll("_", "-"));
        if (!locale.toLanguageTag().matches("[a-z]{2}-[A-Z]{2}")) {
            throw new IllegalArgumentException("invalid locale: " + string);
        }

        return locale;
    }

    public static String stringifyLocale(Locale locale) {
        return locale.toLanguageTag().replaceAll("-", "_");
    }

    /**
     * Traverse a class's hierarchy, streaming all classes down to {@link Object}
     *
     * @param clazz class to traverse
     * @return stream of all whole class hierarchy except {@link Object}.
     */
    public static Stream<Class<?>> getSuperclasses(Class<?> clazz) {
        if (clazz == Object.class) {
            return Stream.of(clazz);
        } else {
            return Stream.concat(Stream.of(clazz), getSuperclasses(clazz.getSuperclass()));
        }
    }

    // Recurse through the class's hierarchy until we find the sought-after annotation
    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotationClass) {
        while(clazz != Object.class) {
            T annotation = clazz.getAnnotation(annotationClass);
            if(annotation != null) {
                return annotation;
            }
            if (clazz == Object.class) {
                return null;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * Quietly try to instantiate a class
     *
     * @param clazz to instantiate
     * @return instance
     * @throws IllegalStateException if there is an error instantiating the class
     */
    public static <T> T instantiate(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Validate a condition is true or throw
     *
     * @param condition that must be true
     * @param msg of error to throw
     * @throws IllegalArgumentException
     */
    public static void checkArgument(boolean condition, String msg) {
        if (!condition) {
            throw new IllegalArgumentException(msg);
        }
    }
}
