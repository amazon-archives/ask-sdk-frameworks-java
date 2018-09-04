package com.amazon.ask.interaction;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Caches a type's reflection information and exposes a dynamic interface to its properties.
 */
public class TypeReflector<T> {
    private final JavaType javaType;
    private final Map<String, PropertyDescriptor> propertyDescriptorIndex;
    private final List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
    private final Map<String, BiConsumer<T, Object>> setters = new HashMap<>();
    private final Map<String, Function<T, Object>> getters = new HashMap<>();

    public TypeReflector(Class<T> clazz) {
        this(TypeFactory.defaultInstance().constructSimpleType(clazz, new JavaType[]{}));
    }

    public TypeReflector(JavaType javaType) {
        this.javaType = assertNotNull(javaType, "javaType");
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(javaType.getRawClass()).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                    continue; // TODO: Warn? Error?
                }

                this.propertyDescriptors.add(descriptor);
                this.setters.put(descriptor.getName(), makeSetter(descriptor));
                this.getters.put(descriptor.getName(), makeGetter(descriptor));
            }
        } catch (IntrospectionException ex) {
            throw new IllegalArgumentException("Could not introspect bean: " + javaType.getTypeName(), ex);
        }

        this.propertyDescriptorIndex = propertyDescriptors.stream().collect(Collectors.toMap(
            PropertyDescriptor::getName,
            p -> p
        ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeReflector<?> that = (TypeReflector<?>) o;
        return Objects.equals(javaType, that.javaType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(javaType);
    }

    public JavaType getJavaType() {
        return javaType;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getTypeClass() {
        return (Class<T>) javaType.getRawClass();
    }

    private Function<T, Object> makeGetter(PropertyDescriptor descriptor) {
        Method readMethod = descriptor.getReadMethod();

        return (intent) -> {
          try {
              return readMethod.invoke(intent);
          } catch (IllegalAccessException | InvocationTargetException ex) {
              throw new IllegalStateException(ex);
          }
        };
    }

    // TODO: Bean Utils calls MethodUtils.getAccessibleMethod. Do we need that?
    // TODO: Throw checked reflective exceptions or wrap?
    private BiConsumer<T, Object> makeSetter(PropertyDescriptor descriptor) {
        Method writeMethod = descriptor.getWriteMethod();

        return (intent, value) -> {
            try {
                Object[] values = new Object[]{value};
                writeMethod.invoke(intent, values);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
                String msg = String.format("Failed to set property '%s' on %s to %s. Expected a %s.",
                    descriptor.getName(),
                    getTypeClass().getName(),
                    value == null ? "null" : value.getClass().getName(),
                    descriptor.getPropertyType().getName());

                throw new IllegalArgumentException(msg, ex);
            }
        };
    }

    public T instantiate() {
        return Utils.instantiate(getTypeClass());
    }

    public List<PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }

    public Map<String, PropertyDescriptor> getPropertyDescriptorIndex() {
        return propertyDescriptorIndex;
    }

    public Object get(T bean, String name) {
        validate(bean, name);
        Function<T, Object> getter = getters.get(name);
        if (getter == null) {
            throw new IllegalArgumentException("No getter specified for property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        return getter.apply(bean);
    }

    public void set(T bean, String name, Object value) {
        validate(bean, name);
        BiConsumer<T, Object> setter = setters.get(name);
        if (setter == null) {
            throw new IllegalArgumentException("No setter specified for property '" + name + "' on bean class '" + bean.getClass() + "'");
        }
        setter.accept(bean, value);
    }

    private void validate(T bean, String name) {
        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        } else if (name == null) {
            throw new IllegalArgumentException("No property name specified");
        }
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return getTypeClass().getAnnotation(annotationClass);
    }

    public <A extends Annotation> A getAnnotation(PropertyDescriptor propertyDescriptor, Class<A> annotationClass) {
        return getAnnotation(propertyDescriptor.getName(), annotationClass);
    }
    public <A extends Annotation> A getAnnotation(String propertyName, Class<A> annotationClass) {
        try {
            PropertyDescriptor propertyDescriptor = propertyDescriptorIndex.get(propertyName);
            if (propertyDescriptor == null) {
                throw new NoSuchFieldException(propertyName + " property is missing");
            }
            Field field = findField(propertyDescriptor.getName());
            A annotation = field.getAnnotation(annotationClass);
            if (annotation == null) {
                annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
            }
            if (annotation == null) {
                annotation = propertyDescriptor.getWriteMethod().getAnnotation(annotationClass);
            }
            return annotation;
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private Field findField(String propertyName) throws NoSuchFieldException {
        Class clazz = getTypeClass();
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(propertyName + " property is missing");
    }

    /**
     * Determine the type of a potentially generic property.
     *
     * @param property name of property
     * @return class of property type
     * @throws IllegalArgumentException if property does not exist on this type, or if the property cannot be resolved.
     */
    public Class reifyPropertyType(String property) {
        PropertyDescriptor propertyDescriptor = propertyDescriptorIndex.get(property);
        if (propertyDescriptor == null) {
            throw new IllegalArgumentException("Property '" + property + "' does not exist.");
        }
        return reifyPropertyType(propertyDescriptor);
    }

    /**
     * Determine the type of a generic property.
     *
     * @param property name of property
     * @return class of property type
     * @throws IllegalArgumentException if the generic property is not a {@link TypeVariable}
     */
    public Class reifyPropertyType(PropertyDescriptor property) {
        Type type = property.getReadMethod().getGenericReturnType();
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> pt = (TypeVariable<?>) type;
            return javaType.getBindings().findBoundType(pt.getTypeName()).getRawClass();
        } else {
            return property.getPropertyType();
//            throw new IllegalArgumentException("Can not reify property '" + property.getName() + "' type '" + type + "'");
        }
    }
}
