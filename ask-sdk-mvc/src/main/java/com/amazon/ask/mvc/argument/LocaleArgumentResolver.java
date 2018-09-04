package com.amazon.ask.mvc.argument;

import com.amazon.ask.mvc.plugin.ArgumentResolver;
import com.amazon.ask.mvc.mapper.ArgumentResolverContext;

import java.util.Locale;
import java.util.Optional;

public class LocaleArgumentResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext input) {
        if (input.parameterTypeEquals(Locale.class)) {
            return Optional.of(Locale.forLanguageTag(input.unwrapRequest().getLocale()));
        }
        return Optional.empty();
    }
}
