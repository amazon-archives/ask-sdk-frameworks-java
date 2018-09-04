package com.amazon.ask.mvc.mapper;

import com.amazon.ask.util.ValidationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains details about a parameter in a method
 */
public class MethodParameter {
    protected final Method method;
    protected final int index;
    protected final Class<?> type;
    protected final Annotation[] annotations;

    public static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

    /**
     * Builds a MethodParameter for a specific parameter in a method
     *
     * @param method the method where the parameter is defined
     * @param index index of the parameter
     * @param type type of the parameter
     * @param annotations the annotations of the parameter
     */
    public MethodParameter(Method method, int index, Class<?> type, Annotation[] annotations) {
        ValidationUtils.assertNotNull(method, "method");
        ValidationUtils.assertNotNull(type, "type");
        ValidationUtils.assertNotNull(annotations, "parameterAnnotations");

        this.method = method;
        this.index = index;
        this.type = type;
        this.annotations = annotations;
    }

    /**
     * @param type annotation class
     * @param <T> type of annotation
     * @return returns an annotation of the indicated type, if present
     */
    public <T> Optional<T> findAnnotation(Class<T> type) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(type)) {
                return Optional.of((T) annotation);
            }
        }

        return Optional.empty();
    }

    /**
     * @return the method where the parameter is defined
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @return index of the parameter
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return type of the parameter
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * @return annotations of the parameter
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodParameter that = (MethodParameter) o;
        return index == that.index &&
            Objects.equals(method, that.method) &&
            Objects.equals(type, that.type) &&
            Arrays.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(method, index, type);
        result = 31 * result + Arrays.hashCode(annotations);
        return result;
    }
}
