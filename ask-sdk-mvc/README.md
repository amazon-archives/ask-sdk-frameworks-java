## ASK SDK Model-View-Controller (MVC) Framework

The Alexa Skills Kit Model-View-Controller (MVC) Framework extends the [ASK SDK v2 for Java](https://github.com/alexa/alexa-skills-kit-sdk-for-java), adapting the [model-view-controller pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) to skill development by adding features for mapping requests to controller methods, and rendering responses with view scripts and templates such as Nashorn JavaScript and [Apache FreeMarker](https://freemarker.apache.org/). It also integrates with the [ASK Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper), enabling you to manage both your interaction model and skill business logic as pluggable code packages.

For background on building Alexa skills on the Alexa developer portal, see [Build Skills with the Alexa Skills Kit](https://developer.amazon.com/docs/ask-overviews/build-skills-with-the-alexa-skills-kit.html). For a list of resources related to the ASK SDK for Java, see [Alexa Skills Kit SDK for Java](https://developer.amazon.com/docs/sdk/alexa-skills-kit-sdk-for-java.html).

## Index

* [Samples](#samples)
* [Your Skill Class](#your-skill-class)
* [Configuration](#configuration)
   * [Dependencies](#dependencies)
   * [Interaction Model Generation](#interaction-model-generation)
   * [Single JAR for AWS Lambda](#single-jar-for-aws-lambda)
* [Skill Modules](#skill-modules)
* [Controllers](#controllers)
   * [Request Mappings](#request-mappings)
   * [Request Handler Chain](#request-handler-chain)
   * [Conditional Mappings](#conditional-mappings)
   * [Argument Resolution](#argument-resolution)
* [Views](#views)
   * [Nashorn (JavaScript) View Resolver](#nashorn-javascript-view-resolver)
   * [View Candidate Enumerators](#view-candidate-enumerators)
* [Interaction Model](#interaction-model)
   * [Intent Schema](#intent-schema)
   * [Intent Data](#intent-data)
   * [Slot Type Schema](#slot-type-schema)
   * [Slot Type Data](#slot-type-data)
* [Extending the MVC Framework](#extending-the-mvc-framework)
   * [Resolvers](#resolvers)
      * [View Resolver](#view-resolver)
      * [Argument](#argument)
      * [Predicate](#predicate)
      * [Exception Handler](#exception-handler)
      * [Request Handler](#request-handler)
      * [Request Interceptor](#request-interceptor)
      * [Response Interceptor](#response-interceptor)
   * [Auto Resolvers](#auto-resolvers)

## Samples

For sample skills, see the following:

* [Colorpicker](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/colorpicker)
* [Decision Tree](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/decisiontree)
* [Tic Tac Toe](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/tictactoe)
* [Trivia](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/trivia)

## Your Skill Class

To create a skill using MVC, you first create a class for your skill that extends `MvcSkillApplication`. This entry point is required for building and deploying your skill's endpoint and interaction model. It defines the skill's invocation name for each supported locale, and registers a collection of `SkillModules` containing the interaction model and handling implementation.

```java
public class HelloWorldSkill extends MvcSkillApplication {
    @Override
    public Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.US, "my en-US invocation name");
    }

    @Override
    public List<SkillModule> getModules() {
        return Collections.singletonList(new HelloWorldSkillModule());
    }
}
```

## Configuration

Using maven, configure a project to generate your interaction model JSON file and single JAR file for AWS Lambda from the CLI.

For more information on maven dependencies, see [Introduction to the Dependency Mechanism](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html). For more information on the maven build lifecycle, see [Introduction to the Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html).

### Dependencies

In your `pom.xml`, add a dependency on the `ask-sdk-mvc` framework:

```xml
<dependencies>
  <dependency>
    <groupId>com.amazon.alexa</groupId>
    <artifactId>ask-sdk-mvc</artifactId>
    <version>0.1.0</version>
  </dependency>
</dependencies>
```

### Interaction Model Generation

In your `pom.xml`, add the `build-model` task from [ask-sdk-maven-plugins](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-maven-plugins) and configure your skill class as the target:

```xml
<plugin>
    <groupId>com.amazon.alexa</groupId>
    <artifactId>ask-sdk-maven-plugins</artifactId>
    <version>0.1.0</version>
    <configuration>
        <destinationDir>ask</destinationDir>
        <className>com.example.HelloWorldSkill</className>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>build-model</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Synthesize the JSON models with [mvn](https://maven.apache.org/install.html):

```bash
mvn compile
```

Update your Skill's model with the [ask cli](https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html):

```bash
ask api update-model \
    --file ./target/ask/en-US.json \
    --locale en-US \
    --skill-id <your-skill-id>
```

### Single JAR for AWS Lambda

Configure a task in your `pom.xml` to output a single .jar file containing your code and dependencies:

For more information on lambda functions using Java, see [Creating a Deployment Package (Java)](https://docs.aws.amazon.com/lambda/latest/dg/lambda-java-how-to-create-deployment-package.html). For more information on Custom Skills and AWS Lambda, see [Host a Custom Skill as an AWS Lambda Function](https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-an-aws-lambda-function.html).

```xml
<plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
        </execution>
    </executions>
</plugin>
```

Create a `SkillStreamHandler` for the lambda entrypoint:

```java
public class HelloWorldSkillLambda extends SkillStreamHandler {
    public HelloWorldSkillLambda() {
        super(new HelloWorldSkill().getSkill());
    }
}
```

Assemble your skill's single jar:

```bash
mvn package
```

And upload the jar to your lambda function:

```bash
aws lambda update-function-code \
    --function-name <name> \
    --zip-file <aritfact-id>-<version>-jar-with-dependencies.jar
    --publish
```

## Skill Modules

A `SkillModule` groups the business logic of controllers and views with the required intents and slot types in the interaction model, enabling you to share aspects of your skill programmatically. For example, the following `HelloWorldModule` adds a single controller for handling requests, and includes the `AMAZON.HelpIntent` in the interaction model:

```java
public class HelloWorldModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        mvcBuilder.addController(new HelloWorldContoller());
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        modelBuilder.intent(HelpIntent.class);
    }
}
```

## Controllers

A controller is an instance of a class with methods for handling inbound Alexa requests. Its methods are scanned for annotations that map requests to methods and auto-wire interceptors and exception handlers.

* @RequestMapping - map a [Request](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/Request.html) sub-class to a method
* @IntentMapping - map an [IntentRequest](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/IntentRequest.html) by name or intent type to a method
* @RequestInterceptor - call a method as a [RequestInterceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/RequestInterceptor.html) in the request handler chain
* @ResponseInterceptor - call a method as a [ResponseInterceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/ResponseInterceptor.html) in the request handler chain
* @ExceptionHandler - map an exception type to a method which will recover with a response

The following demonstrates the use of a controller:

```java
public class HelloWorldController {
    // map request by sub-class
    @RequestMapping(LaunchRequest.class)
    public Response onLaunch(HandlerInput handlerInput) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("hello, world!")
                .build())
            .build()
    }

    // map intent request by intent name
    @IntentMapping(type = "AMAZON.StopIntent")
    public Response onStop(HandlerInput handlerInput) {
        // ...
    }

    // map intent request by intent type
    @IntentMapping(type = HelpIntent.class)
    public Response onHelp(HandlerInput handlerInput) {
        // ...
    }

    @RequestInterceptor
    public void beforeRequest(HandlerInput handlerInput) {
        // ...
    }

    @ResponseInterceptor
    public void afterRequest(HandlerInput input, Response response) {
        // ...
    }

    // recover from specific exceptions thrown by this controller's methods
    @ExceptionHandler(exception = MyException.class)
    public Response recoverFromMyException(MyException exception) {
        // ...
    }
}
```

### Request Mappings

The `@RequestMapping` annotation binds any type of Alexa request to a controller method. In the above example, the `onLaunch` method will be selected when a `LaunchRequest` is received.

The `@IntentMapping` annotation binds specific intents from your interaction model to controller methods. In the above example, `onStop` maps to the `AMAZON.StopIntent` intent by its name and the `onHelp` method maps to the `AMAZON.HelpIntent` by its type.

### Request Handler Chain

When a controller's method is selected to handle the request, a [RequestHandlerChain](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandlerChain.html) containing it and any interceptors or exception handlers is assembled to handle the request. Methods mapped within a controller are considered locally scoped, so any methods annotated with `@RequestInterceptor`, `@ResponseInterceptor` or `@ExceptionHandler` are only added to the chain if it is their controller handling the request.

### Conditional Mappings

MVC supports guarding a method's invocation with some condition described by an annotation. This works for all mapping types, not just request handlers.

The following annotations are provided out of the box:

* @WhenSessionAttribute - require a session attribute to have a specific value
* @WhenDialogState - require the [dialog](https://developer.amazon.com/alexa-skills-kit/dialog-management) to be in some [state](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/DialogState.html).

For example, you can write a handler and interceptor that are only invoked when the session attribute `state` has the value `playing`:

```java
@IntentMapping(type = HelpIntent.class)
@WhenSessionAttribute(path = "state", hasValues="playing")
public Response onHelpWhenPlaying() {
    // return a help response specific to this 'state'
}

@RequestInterceptor
@WhenSessionAttribute(path = "state", hasValues="playing")
public void printPlayingRequest() {
    System.out.println("Received request when in playing state!");
}
```

You can also constrain all mappings within a single controller by annotating the controller's type definition:

```java
@WhenSessionAttribute(path = "state", hasValues="playing")
public class PlayingStateController {
    // mappings are only relevant if state == playing
}
```

### Argument Resolution

A method's arguments are resolved automatically using an `ArgumentResolver`, helping to reduce boilerplate code and simplify unit testing. For example, the following `onHelp` method resolves the whole request's `HandlerInput` and then manually gets the `Request` and `AttributesManager` to fulfill the request.

```java
public Response onHelp(HandlerInput input) {
    Request request = input.getRequestEnvelope().getRequest();
    AttributesManager attributes = input.getAttributesManager();
    // ...
}
```

This method can be simplified with argument resolution:

```
public Response onHelp(Request request, AttributesManager attributes) {
    // ...
}
```

Any class compatible with the [Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper) can be automatically resolved to its concrete representation, avoiding the manual and error-prone process of parsing a raw `IntentRequest`:

```
@IntentMapping(type = HelloWorld.class)
public Response onHelloWorld(HelloWorld intent) {
    System.out.println(intent.getGreeting());
    // ...
}
```

## Views

To promote code modularity, you should separate your skill's application logic such as service calls and state management from the logic of rendering a response. MVC comes with support for a pluggable view/resolver system, allowing developers to write templates and scripts that render responses from a model of key-value-pairs provided by the controller. The views are referenced by name and resolved based on request properties such as locale.

### Nashorn (JavaScript) View Resolver

By default, the core MVC framework supports the JDK's javascript engine, [Nashorn](http://openjdk.java.net/projects/nashorn/), but you can also import the [FreeMarker view resolver](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-mvc-freemarker) or write your own.

To render a response with JavaScript, first register a `NashornViewResolver` which will find and execute your scripts:

```java
package com.example;

public class HelloWorldModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvc) {
        mvc.addController(new HelloWorldController())
           .addViewResolver(NashornViewResolver.builder()
                .withPrefix("views/") // files located in the views resource folder
                .withResourceClass(getClass()) // resource path is relative to this class
                .withRenderFunction("render") // call the 'render' function in the script
                .build());
    }
}
```

Next, adapt your controller's method to return a `ModelAndView` instance instead of a `Response`. `ModelAndView` identifies a view by its name and contains the model attributes.

```java
@IntentMapping(type = HelloWorld.class)
public ModelAndView onHelloWorld(HelloWorld intent) {
    ModelAndView mav = new new ModelAndView("hello_world");
    mav.put("greeting", intent.getGreeting());
    return mav;
}
```

Finally, implement your view logic in a javascript file, `src/main/resources/com/example/views/hello_world.js`:

```javascript
function render(model) { // function taking the model as an argument
    return {
        outputSpeech: {
            type: "PlainText",
            text: model.greeting // use the 'greeting' text from the model
        },
        shouldEndSession: false
    };
}
```

### View Candidate Enumerators

View resolvers look for locale-specific views by default. For example, given a view named `name`, a prefix of `views/` and a suffix of `.js`, the resolver will select the first file that exists from the following:

1. view_en_US.js
2. view/en_US.js
3. view_en.js
4. view/en.js
5. view.js
6. view/global.js

This logic is implemented by the `LocaleViewCandidateEnumerator` and included by default in the standard view resolver builders, but you can also add custom enumerators. For example, you could override the default behavior with an enumerator which selects a versioned view by appending the request's version as a suffix to the view name:

```java
public class MyEnumerator implements ViewCandidateEnumerator {
    @Override
    public Stream<String> enumerate(String viewName, RequestEnvelope requestEnvelope) {
        return Stream.of(viewName + "_" + requestEnvelope.getVersion());
    }
}

// replace the enumerators
NashornViewResolver.builder()
    .withViewCandidateEnumerators(Collections.singletonList(new MyEnumerator())
```

## Interaction Model

MVC integrates with the [Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper) which generates a skill's [interaction model](https://developer.amazon.com/docs/custom-skills/create-the-interaction-model-for-your-skill.html) from code and automatically parses requests with reflection.

Intents and slot types are defined as classes backed by sources of interaction model data such as classpath resource bundles. The interaction model can then be rendered from the CLI at application build time as part of a continuous/automated deployment lifecycle.

### Intent Schema

To define a new intent for your skill, create a class and annotate it with `@Intent`. The name of the intent defaults to the simple name of the class, but can also be defined explicitly with `@Intent(“DifferentName”)`.

A slot is defined as properties annotated with `@SlotProperty`. The name defaults to the property name, but may also be defined explicitly with `@SlotProperty(name = “differentName”)`. The slot type is derived from the property type, such as `AmazonDate`, but can also be defined explicitly with `@SlotProperty(type = AmazonDate.class)`.

```java
@Intent
public class MyIntent {
    @SlotProperty
    private AmazonDate date;

    public AmazonDate getDate() {
        return date;
    }

    public void setDate(AmazonDate date) {
        this.date = date;
    }
}
```

### Intent Data

The interaction model also contains data for an intent such as sample utterances and confirmation/elicitation dialog prompts. You can associate data when registering the intent by constructing an `IntentDataSource`, or by annotating the intent type with an `@IntentResource`.

For example, given a JSON resource file: `src/main/resources/com/example/intents/my_intent_en_US.json`

```
{
  "samples" : [
    "what is the {date}
  ]
}
```

We can create an `IntentDataSource` which will scan for resources relative to the `MyIntent.class`, and associate it with the intent at registration time.

```java
package com.example;

import com.example.intents.MyIntent;

public class HelloWorldModule implements SkillModule {
    @Override
    public void buildModel(Model.Builder model) {
        model.intent(MyIntent.class, IntentData.resource()
            .withResourceClass(MyIntent.class)
            .withName("my_intent")
            .build());
    }
}
```

Alternatively, you can statically associate data with the intent's type by annotating it with an `@IntentResource` annotation. It creates the same resource manually instantiated above, and also supports custom resource classes, suffixes, and codecs:

```java
package com.example.intents;

@IntentResource("my_intent")
public class MyIntent {
    // ..
}
```

Rendering this class for the `en-US` locale results in the following intent schema:

```
{
  "name" : "MyIntent",
  "slots: [
    {
      "name" : "date",
      "type" : "AMAZON.DATE"
    }
  ],
  "samples: [
    "what is the {date}"
  ]
}
```

### Slot Type Schema

The type of an intent's slot is derived from the `@SlotType` annotation on the property's type. For example, the type of the `date` property is `AmazonDate`, a class annotated with the `@SlotType` annotation, explicitly naming it `AMAZON.DATE`.

```java
@SlotType("AMAZON.DATE")
public abstract class AmazonDate extends BaseSlotValue {
    // ..
}
```

Using the `@SlotType` annotation, you can implement custom slot types as classes like `AmazonDate`:

```java
package com.example.slots;

@SlotType
public class MySlotType extends BaseSlotValue {
}
```

Or as an `enum`, where the symbols correspond to either the slot value or a matched [Entity Resolution ID](https://developer.amazon.com/docs/custom-skills/define-synonyms-and-ids-for-slot-type-values-entity-resolution.html).

```java
package com.example.slots;

@SlotType
public enum MySlotType {
    SLOT_ID_1,
    SLOT_ID_2;
}
```

### Slot Type Data

Like with intents, a slot type's interaction model data can be associated when registering its class by creating a `SlotTypeDataSource`, or by annotating the type's class with an `@SlotTypeResource` annotation.

For example, given a JSON resource file, `src/main/resources/com/example/slots/my_slot_type.json`:

```
{
  "values" : [
    {
      "id" : "SLOT_ID_1",
      "name" : {
        "value" : "a",
        "synonyms" : [
          "ay"
        ]
      }
    },
    {
      "id" : "SLOT_ID_2",
      "name" : {
        "value" : "b",
        "synonyms" : [
          "bee"
        ]
      }
    }
  ]
}
```

We can associate a `SlotTypeDataSource` which will scan for resources relative to the `MySlotType.class`:

```java
package com.example;

import com.example.slots.MySlotType;

public class HelloWorldModule implements SkillModule {
    @Override
    public void buildModel(Model.Builder model) {
        model.slotType(MySlotType.class, SlotTypeData.resource()
            .withResourceClass(MySlotType.class)
            .withName("my_slot_type")
            .build());
    }
}
```

Or, you can statically associate data with the slot's type by annotating it with a `@SlotTypeResource` annotation. It creates the same resource manually instantiated above:

```java
package com.example.slots;

@SlotType
@SlotTypeResource("my_slot_Type")
public class MySlotType {
    // ..
}
```

Enums are also supported, but should only be used when fuzzy matching is not required:

```java
package com.example.slots;

@SlotType
@SlotTypeResource("my_slot_Type")
public enum MySlotType {
    SLOT_ID_1,
    SLOT_ID_2;
}
```

Rendering this class for the `en-US` locale results in the following slot type schema:

```
{
  "name" : "MySlotType",
  "values" : [
    {
      "id" : "SLOT_ID_1",
      "name" : {
        "value" : "a",
        "synonyms" : [
          "ay"
        ]
      }
    },
    {
      "id" : "SLOT_ID_2",
      "name" : {
        "value" : "b",
        "synonyms" : [
          "bee"
        ]
      }
    }
  ]
}
```

## Extending the MVC Framework

### Resolvers

The `MvcSdkModule` provides extension points for each aspect as injectable `Resolver` interfaces:

* `com.amazon.ask.mvc.plugin.ViewResolver`
* `com.amazon.ask.mvc.plugin.ArgumentResolver`
* `com.amazon.ask.mvc.plugin.PredicateResolver`
* `com.amazon.ask.mvc.plugin.ExceptionHandlerResolver`
* `com.amazon.ask.mvc.plugin.RequestHandlerResolver`
* `com.amazon.ask.mvc.plugin.RequestInterceptorResolver`
* `com.amazon.ask.mvc.plugin.ResponseInterceptorResolver`

The `MvcSdkModule` includes default resolvers which implements each of its fist-class features such as `@IntentMapping`, `@RequestMapping`, `@RequestInterceptor`, `@WhenSessionAttribute`, etc.

#### View Resolver

Resolves a view to render the return value of a controller's method as a `Response`. Supports request and exception handler methods, e.g. the return value of a method annotated with `@RequestMapping`, `@IntentMapping` or `@ExceptionHandler`.

**Example**: render a string returned by a controller as the output speech.

Write a view resolver which checks if the output is a `String`:

```java
public class StringViewResolver implements ViewResolver {
    @Override
    public Optional<View> resolve(Object handlerOutput, RequestEnvelope request) throws Exception {
        if (handlerOutput instanceof String) {
            return new StringView();
        }
    }
}
```

Build a `Response` and cast the `handlerOutput` to a `String`:

```java
public class StringView implements View {
    @Override
    public Response render(Object handlerOutput, RequestEnvelope requestEnvelope) throws Exception {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText((String) handlerOutput)
                .build())
            .build()
    }
}
```

#### Argument

Attempts to resolve the value for method's parameter given its reflection information and the request.

**Example**: resolve the [attributes manager](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/attributes/AttributesManager.html) from the `HanderInput` if the parameter's type is `AttributesManager.class:`

```java
public class AttributesManagerResolver implements ArgumentResolver {
    @Override
    public Optional<Object> resolve(ArgumentResolverContext context) {
        if (context.parameterTypeEquals(AttributesManager.class)) {
            return Optional.of(context.getHandlerInput().getAttributesManager());
        }
        return Optional.empty();
    }
}
```

#### Predicate

Resolves a Predicate<[HandlerInput](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/HandlerInput.html)> from a controller's type and methods. The context may be that of a `ControllerContext`, or a `ControllerMethodContext` since conditional annotations are allowed to be on both the controller type and individual methods.

**Example**: `true` if the session contains a `state`:

```java
public class StateSessionPredicateResolver implements PredicateResolver {
    @Override
    public Optional<Predicate<HandlerInput>> resolve(AnnotationContext context) {
        Predicate<HandlerInput> predicate = input ->
            input.getAttributesManager()
                 .getSessionAttributes()
                 .containsKey("state");

        return Optional.of(predicate);
    }
}
```

#### Exception Handler

Resolves an [exception handler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/exception/ExceptionHandler.html) for a controller's method.

**Example:** invoke a method if the exception is some type:

```java
public @interface ExceptionMapping {
    Class<? extends Throwable> type();
}

public class ExceptionMappingResolver implements ExceptionHandlerResolver {
    @Override
    public Optional<ExceptionHandler> resolve(ControllerMethodContext context) {
        ExceptionMapping mapping = context.getMethod().getAnnotation(ExceptionHandler.class);
        if (mapping = null) {
            return Optional.empty();
        }

        ExceptionHandler handler = new ExceptionHandler() {
            // check if exception is type, invoke method, etc.
        }
        return Optional.of(handler);
    }
}
```

#### Request Handler

Resolves a [request handler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandlerChain.html) for a controller's method.

**Example**: invoke a method if intent name matches some value:

```java
public @interface IntentMapping {
    String name();
}

public class IntentMappingRequestHandlerResolver implements RequestHandlerResolver {
    @Override
    public Optional<RequestHandler> resolve(ControllerMethodContext context) {
        IntentMapping mapping = context.getMethod().getAnnotation(IntentMapping.class);
        if (mapping == null) {
            return Optional.empty();
        }
        RequestHandler handler = new RequestHandler() {
            // check if intent name matches, invoke the method, etc.
        }
        return Optional.of(handler);
    }
}
```

#### Request Interceptor

Resolves a [request interceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/RequestInterceptor.html) for a controller's method.

**Example**: print a message if the method is annotated with `@PrintInterceptor`:

```java
public @interface PrintInterceptor {}

public class PrintRequestInterceptorResolver implements RequestInterceptorResolver {
    @Override
    public Optional<RequestInterceptor> resolve(ControllerMethodContext context) {
        PrintInterceptor mapping = context.getMethod().getAnnotation(PrintInterceptor.class);
        if (mapping == null) {
            return Optional.empty();
        }
        RequestInterceptor handler = input -> System.out.println("Custom Interceptor!");
        return Optional.of(handler);
    }
}
```

#### Response Interceptor

Resolves a [response interceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/ResponseInterceptor.html) for a controller's method.

**Example**: print a message if the method is annotated with `@PrintInterceptor`:

```java
public @interface PrintInterceptor {}

public class PrintResponseInterceptorResolver implements ResponseInterceptorResolver {
    @Override
    public Optional<RequestInterceptor> resolve(ControllerMethodContext context) {
        PrintInterceptor mapping = context.getMethod().getAnnotation(PrintInterceptor.class);
        if (mapping == null) {
            return Optional.empty();
        }
        ResponseInterceptor handler = (input, response) -> System.out.println("Custom Interceptor!");
        return Optional.of(handler);
    }
}
```

### Auto Resolvers

Annotations such as mappings (`@IntentMapping`), predicates (`@WhenSessionAttribute`) and arguments (`@SessionValues`) can be automatically resolved using a meta-annotation. The annotation type you want to resolve must be annotated with its corresponding `@Auto*` meta-annotation, identifying the class which implements it:

* `com.amazon.ask.mvc.annotation.plugin.AutoArgumentResolver`
* `com.amazon.ask.mvc.annotation.plugin.AutoPredicateResolver`
* `com.amazon.ask.mvc.annotation.plugin.AutoExceptionHandler`
* `com.amazon.ask.mvc.annotation.plugin.AutoRequestHandler`
* `com.amazon.ask.mvc.annotation.plugin.AutoRequestInterceptor`
* `com.amazon.ask.mvc.annotation.plugin.AutoResponseInterceptor`

**Example: **extract a slot by name for an annotated parameter such as `onIntent(@Slot(“answer”) String answer) { .. }`

```java
@AutoArgumentResolver(Slot.Plugin.class) // identify the auto-resolver
public @interface Slot {
    String value(); // slot name

    class Plugin implements AutoArgumentResolver.Plugin<Slot> {
        @Override
        public Object apply(ArgumentResolverContext context, Slot slot) {
            return ((IntentRequest) context.unwrapRequest()).getIntent().getSlots().get(slot.value());
        }
    }
}
```

**Example:** Predicate to check if a slot value is present

```java
@AutoPredicateResolver(SlotExists.Plugin.class) // identify the auto-resolver
public @interface SlotExists {
    String value(); // slot name

    class Plugin implements AutoPredicateResolver.Plugin<SlotExists> {
        @Override
        public Predicate<HandlerInput> apply(AnnotationContext entity, SlotExists annotation) {
            return input -> {
                Request request = input.getHandlerInput().getRequestEnvelope().getRequest();
                if (request instanceOf IntentRequest)
                    IntentRequest intent = (IntentRequest) request;
                    Slot slot = intent.getIntent().getSlots().get(annotation.value());
                    return slot != null;
                }
                return false;
            }
        }
    }
}
```
