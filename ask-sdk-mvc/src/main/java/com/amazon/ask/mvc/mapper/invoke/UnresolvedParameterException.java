package com.amazon.ask.mvc.mapper.invoke;

import java.lang.reflect.Method;

/**
 * Thrown when one of the specified parameters in an MVC controller cannot be resolved
 */
public class UnresolvedParameterException extends RuntimeException {
    private final int   index;
    private final Class type;

    public UnresolvedParameterException(int index, Class type, Method method) {
        super(String.format("Unable to resolve parameter at index '%d' of type '%s' in '%s:%s'", index, type.getName(), method.getDeclaringClass().getName(), method.getName()));
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public Class getType() {
        return type;
    }
}
