package com.amazon.ask.mvc.annotation.condition;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.interaction.Utils;
import com.amazon.ask.mvc.annotation.plugin.AutoPredicate;
import com.amazon.ask.mvc.mapper.AnnotationContext;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.*;
import java.util.function.Predicate;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * When added to a handler method, the method will be invoked only if one
 * of the following conditions is met:
 * <ul>
 *     <li>The attribute is in the session, its value is of type String and it is contained in the values list</li>
 *     <li>The attribute is not in the session (or it has a null value), and matchUndefinedValue is set to true</li>
 * </ul>
 */
@Documented
@Target(value = {METHOD, TYPE})
@Retention(value = RUNTIME)
@Repeatable(WhenSessionAttribute.Container.class)
@AutoPredicate(WhenSessionAttribute.Plugin.class)
public @interface WhenSessionAttribute {

    /**
     * @return the attribute name
     */
    String[] path();

    /**
     * @return possible attribute values
     */
    String[] hasValues() default {};

    /**
     * @return if the condition should match a null or missing value
     */
    boolean matchNull() default false;

    /**
     * Adds support for the {@link WhenSessionAttribute} predicate.
     */
    class Plugin implements AutoPredicate.Plugin<WhenSessionAttribute> {
        @Override
        public Predicate<HandlerInput> apply(AnnotationContext context, WhenSessionAttribute annotation) {
            final Set<String> values = new HashSet<>(Arrays.asList(annotation.hasValues()));

            return input -> {
                Map<String, Object> session = input.getRequestEnvelope().getSession().getAttributes();
                String value = null;

                if (session != null) {
                    String[] path = annotation.path();

                    Map map = session;
                    for (int i = 0; i < path.length; i++) {
                        Object o = map.get(path[i]);

                        if (i + 1 < path.length) {
                            // not the last element
                            if (o instanceof Map) {
                                map = (Map) o;
                            } else {
                                break;
                            }
                        } else {
                            if (o instanceof String) {
                                // we found the value
                                value = (String) o;
                            } else {
                                break;
                            }
                        }
                    }
                }

                if (value == null) {
                    return annotation.matchNull();
                }

                return values.contains(value);
            };
        }
    }

    /**
     * Adds support for multiple {@link WhenSessionAttribute} predicates.
     */
    @Documented
    @Target(value = {METHOD, TYPE})
    @Retention(value = RUNTIME)
    @AutoPredicate(WhenSessionAttribute.Container.Plugin.class)
    @interface Container {
        /**
         * @return repeated {@link WhenSessionAttribute} annotations.
         */
        WhenSessionAttribute[] value();

        class Plugin implements AutoPredicate.Plugin<Container> {
            private static final WhenSessionAttribute.Plugin SINGLE = new WhenSessionAttribute.Plugin();
            @Override
            public Predicate<HandlerInput> apply(AnnotationContext context, Container container) {
                return Arrays.stream(container.value())
                    .map(whenSessionAttribute -> SINGLE.apply(context, whenSessionAttribute))
                    .reduce(Predicate::and)
                    .orElse(Utils.TRUE);
            }
        }
    }
}
