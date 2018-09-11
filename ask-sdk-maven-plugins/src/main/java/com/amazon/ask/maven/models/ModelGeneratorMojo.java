/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.maven.models;

import com.amazon.ask.interaction.Utils;
import com.amazon.ask.interaction.build.Generator;
import com.amazon.ask.interaction.build.GeneratorException;
import com.amazon.ask.interaction.build.SkillModelSupplier;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Maven plugin for rendering a skill's localized interaction model JSON files.
 **
 * This plugin can be added to your project's build process and configured to execute on each build.
 *
 * For example:
 *
 * <pre>
 * {@code
 * <build>
 * ...
 * <plugin>
 *   <groupId>com.amazon.alexalabs</groupId>
 *   <artifactId>ask-sdk-maven-plugins</artifactId>
 *   <version>{version}</version>
 *   <configuration>
 *     <destinationDir>interactionmodel</destinationDir>
 *     <className>com.myskill.ModelSkill</className>
 *     <locales>
 *       <param>en_US</param>
 *     </locales>
 *   </configuration>
 *   <executions>
 *     <execution>
 *       <phase>compile</phase>
 *       <goals>
 *         <goal>build-model</goal>
 *       </goals>
 *     </execution>
 *   </executions>
 * </plugin>
 * ...
 * </build>
 * }
 * </pre>
 *
 * This configures the plugin to run during the compile phase of the build lifecycle.
 *
 * The plugin can also be executed directly from the command line:
 *
 * {@code mvn com.amazon.alexalabs:ask-sdk-maven-plugins:0.1.0-SNAPSHOT:build-model -DdestinationDir=interactionmodel -DclassName=com.myskill.ModelSkill -Dlocales=en_US}
 */
@Mojo(name = "build-model", requiresDependencyResolution = ResolutionScope.RUNTIME)
public class ModelGeneratorMojo extends AbstractMojo {

    @Parameter(property = "className", required = true)
    private String className;

    @Parameter(property = "destinationDir", required = true)
    private File destinationDir;

    @Parameter(property = "locales")
    private List<String> locales = new ArrayList<>();

    @Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Locale> resolvedLocales = locales.stream()
                .map(Utils::parseLocale)
                .collect(Collectors.toList());

        SkillModelSupplier application = resolveSkillApplication(className);
        try {
            generate(application, destinationDir, resolvedLocales);
        } catch (GeneratorException e) {
            throw new MojoFailureException("Exception encountered while generating skill interaction model", e);
        }
    }

    protected void generate(SkillModelSupplier application, File destdir, List<Locale> locales) throws GeneratorException {
        new Generator(application, destdir, locales).generate();
    }

    private SkillModelSupplier resolveSkillApplication(String className) throws MojoExecutionException {
        List<String> classpathElements;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            }
            Thread currentThread = Thread.currentThread();
            ClassLoader projectClassLoader = new URLClassLoader(projectClasspathList.toArray(new URL[0]),
                    currentThread.getContextClassLoader());
            currentThread.setContextClassLoader(projectClassLoader);
            return (SkillModelSupplier) projectClassLoader.loadClass(className).newInstance();
        } catch (ReflectiveOperationException | DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Could not resolve skill application", e);
        }
    }

}
