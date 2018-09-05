/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.codegen;

import java.util.regex.Pattern;

/**
 *
 */
public class Utils {
    private static final Pattern AMAZON_PATTERN = Pattern.compile("^AMAZON\\.[A-Za-z][A-Za-z_]*[A-Za-z]$");
    public static boolean isAmazon(String typeName) {
        return AMAZON_PATTERN.matcher(typeName).matches();
    }

    private static final Pattern JAVA_NAMESPACE_PATTERN = Pattern.compile("([a-z][a-z_0-9]*\\.)*[a-z][a-z_0-9]*$", Pattern.CASE_INSENSITIVE);
    public static String validateNamespace(String namespace) {
        if (namespace == null || !JAVA_NAMESPACE_PATTERN.matcher(namespace).matches()) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        return namespace;
    }

    private static final Pattern JAVA_NAME_PATTERN = Pattern.compile("^[a-z][a-z_0-9]*$", Pattern.CASE_INSENSITIVE);
    public static String validateName(String name) {
        if (name == null || !JAVA_NAMESPACE_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid skill name: " + name);
        }
        return name;
    }
}
