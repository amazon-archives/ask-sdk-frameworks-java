/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction;

import com.amazon.ask.interaction.annotation.type.Intent;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import static org.junit.Assert.*;

public class TypeReflectorTest {
    @Test
    public void testSet() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        assertEquals(1, underTest.getPropertyDescriptors().size());

        TestClass instance = new TestClass();
        assertNull(instance.getValue());
        underTest.set(instance, "value", "test");
        assertEquals("test", instance.getValue());
    }

    @Test
    public void testGet() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        assertEquals(1, underTest.getPropertyDescriptors().size());

        TestClass instance = new TestClass();

        assertNull(instance.getValue());
        assertNull(underTest.get(instance, "value"));
        instance.setValue("test");
        assertEquals("test", underTest.get(instance, "value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownProperty() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        TestClass instance = new TestClass();
        underTest.get(instance, "unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUnknownProperty() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        TestClass instance = new TestClass();
        underTest.set(instance, "unknown", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIllegalCast() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        TestClass instance = new TestClass();
        underTest.set(instance, "value", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPrivate() {
        TypeReflector<TestPrivateSetter> underTest = new TypeReflector<>(TestPrivateSetter.class);
        TestPrivateSetter instance = new TestPrivateSetter();
        underTest.set(instance, "value", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrivate() {
        TypeReflector<TestPrivateGetter> underTest = new TypeReflector<>(TestPrivateGetter.class);
        TestPrivateGetter instance = new TestPrivateGetter();
        underTest.set(instance, "value", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullBean() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        underTest.get(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullBean() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        underTest.set(null, "value", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullName() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        underTest.get(new TestClass(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullName() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);
        underTest.set(new TestClass(), null, "test");
    }

    @Test
    public void testInstantiate() {
        TypeReflector<TestClass> underTest = new TypeReflector<>(TestClass.class);

        TestClass left = new TestClass();
        TestClass right = underTest.instantiate();
        assertEquals(left, right);
        assertNotSame(left, right);
    }

    @Test(expected = IllegalStateException.class)
    public void testInstantiationFailure() {
        TypeReflector<TestPrivate> underTest = new TypeReflector<>(TestPrivate.class);
        underTest.instantiate();
    }

    @Test
    public void testIgnoreIfNoGetter() {
        assertEquals(0, new TypeReflector<>(NoGetter.class).getPropertyDescriptors().size());
    }

    @Test
    public void testIgnoreIfNoSetter() {
        assertEquals(0, new TypeReflector<>(NoSetter.class).getPropertyDescriptors().size());
    }

    @Test
    public void testGetAnnotation() {
        assertEquals("Test", new TypeReflector<>(TestClass.class).getAnnotation(Intent.class).value());
    }

    @Test
    public void testGetPropertyAnnotation() {
        assertEquals("Test", new TypeReflector<>(TestClass.class).getAnnotation("value", PropertyAnnotation.class).value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMissingPropertyAnnotation() {
        new TypeReflector<>(TestClass.class).getAnnotation("missing", PropertyAnnotation.class);
    }

    @Test
    public void testReifyProperty() {
        TypeReflector<TestGeneric> reflector = new TypeReflector<>(
            TypeFactory.defaultInstance().constructParametricType(TestGeneric.class, String.class));

        assertEquals(String.class, reflector.reifyPropertyType("value"));
        assertEquals(String.class, reflector.reifyPropertyType(reflector.getPropertyDescriptors().get(0)));
    }

    @Test
    public void testReifyNonAbstractProperty() {
        assertEquals(String.class, new TypeReflector<>(TestClass.class).reifyPropertyType("value"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReifyMissingProperty() {
        new TypeReflector<>(TestClass.class).reifyPropertyType("missing");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface PropertyAnnotation {
        String value();
    }

    @Intent("Test")
    public static class TestClass {
        @PropertyAnnotation("Test")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass testClass = (TestClass) o;
            return Objects.equals(value, testClass.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    private static class TestPrivate {
    }

    public static class TestPrivateGetter {
        private String value;

        private String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TestPrivateSetter {
        private String value;

        public String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }
    }

    public static class NoGetter {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class NoSetter {
        private String value;

        public String getValue() {
            return value;
        }
    }

    public static class TestGeneric<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
