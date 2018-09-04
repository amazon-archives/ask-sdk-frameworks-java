package com.amazon.ask.interaction.codegen;

import com.amazon.ask.interaction.model.SkillModel;
import com.amazon.ask.interaction.SkillApplication;
import com.amazon.ask.interaction.renderer.SkillModelRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
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
        Application.main(new String[] {
            "--namespace", "com.example",
            "--output", "target/temp/generated-src/",
            "--skill-name", "PetSkill",
            "--model", "en-US=models/en-US.json",
            "--model", "de-DE=models/de-DE.json"
        });

        // Compile generated code
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File typeFile = new File("target/temp/generated-src/com/example/slots/PetType.java");
        File intentFile = new File("target/temp/generated-src/com/example/intents/PetTypeIntent.java");
        File skillFile = new File("target/temp/generated-src/com/example/PetSkill.java");
        File codeDir = new File("target/temp/generated-src/");
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(typeFile, intentFile, skillFile);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, files);
        assertTrue(task.call());
        fileManager.close();

        // Load the generated code
        URLClassLoader classLoader = new URLClassLoader(new URL[]{codeDir.toURI().toURL()});
        Class<?> clazz = classLoader.loadClass("com.example.PetSkill");
        SkillApplication application =  (SkillApplication) clazz.newInstance();

        // Render the interaction model and ensure it equals the original interaction model
        for (Locale locale : Arrays.asList(Locale.forLanguageTag("en-US"), Locale.forLanguageTag("de-DE"))) {
            SkillModel expectedModel = mapper.readValue(new File("models/" + locale.toLanguageTag() + ".json"), SkillModel.class);
            SkillModel actualModel = renderer.render(application.getSkillModel(), locale);
            assertEquals(
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedModel),
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualModel));
        }
    }
}
