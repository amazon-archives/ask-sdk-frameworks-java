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

import com.amazon.ask.interaction.SkillApplication;
import com.amazon.ask.interaction.TypeReflector;
import com.amazon.ask.interaction.model.InteractionModelEnvelope;
import com.amazon.ask.interaction.renderer.SkillModelRenderer;
import com.amazon.ask.interaction.types.slot.AmazonLiteral;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.list.USCity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeneratorTest {
    private final SkillModelRenderer renderer = new SkillModelRenderer();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIsomorphism() throws Exception {
        // Generate java files for interaction models
        String path = "target/temp/generated-src/testIsomorphism";

        Application.main(new String[] {
            "--namespace", "com.example",
            "--output", path,
            "--skill-name", "PetSkill",
            "--model", "en-US=models/en-US.json",
            "--model", "de-DE=models/de-DE.json"
        });

        // Compile generated code
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File typeFile = new File(path + "/src/main/java/com/example/slots/PetType.java");
        File intentFile = new File(path + "/src/main/java/com/example/intents/PetTypeIntent.java");
        File skillFile = new File(path + "/src/main/java/com/example/PetSkill.java");
        File javaDir = new File(path + "/src/main/java");
        File resourcesDir = new File(path + "/src/main/resources");
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(typeFile, intentFile, skillFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, files);
        assertTrue(task.call());
        fileManager.close();

        // Load the generated code
        URLClassLoader classLoader = new URLClassLoader(new URL[]{
            javaDir.toURI().toURL(),
            resourcesDir.toURI().toURL()
        });
        Class<?> clazz = classLoader.loadClass("com.example.PetSkill");
        SkillApplication application =  (SkillApplication) clazz.newInstance();

        // Render the interaction model and ensure it equals the original interaction model
        for (Locale locale : Arrays.asList(Locale.forLanguageTag("en-US"), Locale.forLanguageTag("de-DE"))) {
            InteractionModelEnvelope expectedModel = mapper.readValue(new File("models/" + locale.toLanguageTag() + ".json"), InteractionModelEnvelope.class);
            InteractionModelEnvelope actualModel = renderer.render(application.getSkillModel(), locale);
            assertEquals(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedModel),
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualModel));
        }
    }

    @Test
    public void testBeanNames() throws Exception {
        // Generate java files for interaction models
        String path = "target/temp/generated-src/testBeanNames";

        Application.main(new String[] {
            "--namespace", "com.example",
            "--output", path,
            "--skill-name", "PetSkill",
            "--model", "en-US=models/bean-name-en-US.json"
        });

        // Compile generated code
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File intentFile = new File(path + "/src/main/java/com/example/intents/BeanIntent.java");
        File javaDir = new File(path + "/src/main/java/");
        File resourcesDir = new File(path + "/src/main/resources/");

        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(intentFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, files);
        assertTrue(task.call());
        fileManager.close();

        // Load the generated code
        URLClassLoader classLoader = new URLClassLoader(new URL[]{
            javaDir.toURI().toURL(),
            resourcesDir.toURI().toURL()
        });
        Class clazz = classLoader.loadClass("com.example.intents.BeanIntent");
        TypeReflector<?> reflector = new TypeReflector<Object>(clazz);
        assertEquals(3, reflector.getPropertyDescriptors().size());

        PropertyDescriptor standardName = reflector.getPropertyDescriptorIndex().get("standardName");
        PropertyDescriptor capitalFirstLetter = reflector.getPropertyDescriptorIndex().get("capitalFirstLetter");
        PropertyDescriptor URL = reflector.getPropertyDescriptorIndex().get("URL");

        assertEquals(AmazonLiteral.class, standardName.getPropertyType());
        assertEquals(AmazonDate.class, capitalFirstLetter.getPropertyType());
        assertEquals(USCity.class, URL.getPropertyType());
    }
}
