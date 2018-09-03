# Alexa Skils MVC Framework

The Alexa Skills MVC Framework extends the core ASK SDK, adding features for mapping requests to controller methods and rendering responses with view scripts and templates such as Nashorn Javascript and Freemarker. It also integrates the Interaction Model Mapper to create a single environment where both the interaction model and business logic of skills can be managed as code packages.

# Quickstart Examples

Link: https://code.amazon.com/packages/AskSdkJavaMvcExamples/trees/mainline

# Creating a MVC Skill

## Entry Point

To create a skill using MVC, you first create a class for your skill which extends `MVCSkillApplication`. This entry point is required for building and deploying your Skill's Endpoint and Interaction Model. It defines the skill's invocation name for each supported locale, and registers a collection of 'Skill Modules' making up the implementation.

```java
public class HelloWorldSkill extends MvcSkillApplication {
    @Override
    public Map<Locale, String> getInvocationNames() {
        return Collections.singletonMap(Locale.US, "my us invocation name");
    }

    @Override
    public List<SkillModule> getModules() {
        return Collections.singletonList(new HelloWorldSkillModule());
    }
}
```

## Skill Modules

A Skill Module groups Controllers and Views (business logic) with their required Intents and Slot Types (interaction model). This enables developers to modularize, share and customize aspects of their skill programatically.

For example, the `HelloWorldModule` below adds a single Controller for handling requests and includes the `AMAZON.StopIntent` in the Interaction Model.

```java
// import the AMAZON.HelpIntent class from the Interaction Model Mapper library
import com.amazon.ask.models.types.intent.HelpIntent;

public class HelloWorldModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvcBuilder) {
        // instantiate and add the controller
        mvcBuilder.addController(new HelloWorldContoller());
    }

    @Override
    public void buildModel(Model.Builder modelBuilder) {
        // add the intent to the interaction model by registering its class
        modelBuilder.intent(HelpIntent.class);
    }
}
```

## Controllers

A Controller is an instance of a class with methods that can handle inbound Alexa requests. Those methods make use of annotations to simplify the process of writing and registering the [core SDK request handling concepts](https://alexa-skills-kit-sdk-for-java.readthedocs.io/en/latest/Request-Processing.html).

On application start up, MVC scans each registered controller for methods annotated with mapping annotations and creates a handler/interceptor instance for each found method. There are five mapping-annotations provided with the core MVC framework:

* `@RequestMapping` - creates a [RequestHandler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandler.html) for a [Request](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/Request.html) type
* `@IntentMapping` - creates a [RequestHandler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandler.html) for a specific intent from an [IntentRequest](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/IntentRequest.html)
* `@RequestInterceptor` - creates a [RequestInterceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/RequestInterceptor.html)
* `@ResponseInterceptor` - creates a [ResponseInterceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/ResponseInterceptor.html)
* `@ExceptionHandler` - creates an [ExceptionHandler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/exception/ExceptionHandler.html)

Below is a controller demonstrating their use:

```java
public class HelloWorldController {
    @RequestMapping(LaunchRequest.class)
    public Response onLaunch(HandlerInput handlerInput) {
        return Response.builder()
            .withShouldEndSession(false)
            .withOutputSpeech(PlainTextOutputSpeech.builder()
                .withText("hello, world!")
                .build())
            .build()
    }

    @IntentMapping(type = "AMAZON.StopIntent")
    public Response onStop(HandlerInput handlerInput) {
        // ...
    }

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

    @ExceptionHandler(exception = MyException.class)
    public Response onException(MyException exception) {
        // ...
    }
}
```

### Request Mappings

The `@RequestMapping` annotation binds any type of Alexa request to a controller method. In the above example, the `onLaunch` method will be selected when a `LaunchRequest` is received.

The `@IntentMapping` annotation binds specific intents from your interaction model to controller methods. In the above example, `onStop` maps to the `AMAZON.StopIntent` intent by its name, and the `onHelp` method maps to the `AMAZON.HelpIntent` by its type, `HelpIntent.class`.

### Request Handler Chain

Once a controller's method is selected to handle the request, a [RequestHandlerChain](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandlerChain.html) containing it and any interceptors or exception handlers is assembled to actually handle the request. Methods mapped within a controller are considered "locally scoped", so any methods annotated with @RequestInterceptor, @ResponseInterceptor or @ExceptionHandler are only added to the chain if it is their controller who is handling the request.

### Conditional Mappings

MVC supports guarding a method's invocation with some condition described by an annotation. There are two annotations provided out of the box:

* @WhenSessionAttribute - require a session attribute have a specific value or is not set
* @WhenDialogState - require the [dialog](https://developer.amazon.com/alexa-skills-kit/dialog-management) to be in some [state](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/model/DialogState.html).

For example, we could write a handler and interceptor that are only invoked when the session contains the value 'playing' for a session attribute, 'state':

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

A method's arguments are resolved automatically using Argument Resolvers, helping to reduce boiler-plate code and simplify unit testing.

For example, the following `onHelp` method resolves the whole request's `HandlerInput` instance and then manually gets the `Request` and `AttributesManager` to fulfill the request.

```java
public Response onHelp(HandlerInput input) {
    Request request = input.getRequestEnvelope().getRequest();
    AttributesManager attributes = input.getAttributesManager();
    // ...
}
```

... making use of argument resolution, this method can be simplified:

```java
public Response onHelp(Request request, AttributesManager attributes) {
    // ...
}
```

.. also, any intent defined in code using the Interaction Model Mapper (TODO: link) can be deserialized to its POJO representation automatically, relieving developers from the manual and error-prone process of parsing a raw `IntentRequest`:

```java
@IntentMapping(type = HelloWorld.class)
public Response onHelloWorld(HelloWorld intent) {
    System.out.println(intent.getGreeting());
    // ...
}
```

*For a list of all default argument resolvers, see: TODO*

## Views

The request handlers we've described so far have returned their Response inline using a builder. To promote code modularity, it is encouraged to separate your skill's application logic such as service calls and state management from the logic of rendering a response. As this is a MVC framework, it comes with support for a pluggable View and ViewResolver system, allowing developers to write templates and scripts that render responses from a collection of key-value-pairs, the 'model'.

*By default, the core MVC framework supports the JDK's javascript engine, Nashorn, but you can also import the (TODO: link) FreeMarker view resolver dependency or write your own.*

To render a response with a view, we must adapt our controller's method to return a `ModelAndView` instance instead of a `Response`. This instance identifies a view by its name and provides the model containing relevant attributes.

```java
@IntentMapping(type = HelloWorld.class)
public ModelAndView onHelloWorld(HelloWorld intent) {
    Map<String, Object> model = new HashMap<>();
    model.put("greeting", intent.getGreeting()); // referenced by name in the view

    return new ModelAndView("hello_world", model);
}
```

... next, we extract our view logic into a javascript file, '`hello_world.js`':

```java
function render(model) { // function taking the model as an argument
    return {
        outputSpeech: {
            type: "PlainText",
            text: model.greeting // take the 'greeting' text from the model
        },
        shouldEndSession: false
    };
}
```

... and finally, we register a `ViewResolver` which know how to find and execute the script:

```java
public class HelloWorldModule implements SkillModule {
    @Override
    public void buildMvc(MvcSdkModule.Builder mvc) {
        mvc
            .addController(new HelloWorldController())
            .addViewResolver(NashornViewResolver.builder()
                .withPrefix("views/") // files located in the views resource folder
                .withResourceClass(getClass()) // resource path is relative to this class
                .withRenderFunction("render") // call the 'render' function in the script
                .build());
    }

    // ...
}
```

# Extending the MVC Framework

TODO: Separate page for extensions?

## Resolvers

The MVC framework attempts to provide extension points for each aspect as injectable 'Resolver' interfaces in the `com.amazon.ask.mvc.plugin` package. *TODO: link*

* ViewResolver
* ArgumentResolver
* PredicateResolver
* ExceptionHandlerResolver
* RequestHandlerResolver
* RequestInterceptorResolver
* ResponseInterceptorResolver

*The MvcSdkModule includes default resolvers which implements each of its fist-class features such as `@IntentMapping`, `@RequestMapping`, `@RequestInterceptor`, `@WhenSessionAttribute`, etc.*

### View Resolver

Resolves a View to render the return value of a controller's method. Supports request and exception handler methods, e.g. those annotated with `@RequestMapping`, `@IntentMapping` or `@ExceptionHandler`.

TODO: Show a basic one.

TODO: Suggest extending the BaseViewResolver

### Argument

Attempts to resolve the parameter of a method, given its reflective information and the request context.

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

### Predicate/Condition

Resolves a Predicate<[HandlerInput](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/HandlerInput.html)> from conditional annotation. The context may be that of a `ControllerContext`, or a `ControllerMethodContext` since conditional annotations are allowed to be on both the controller type and individual method mappings.

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

### Exception Handler

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
```

### Request Handler

Resolves a [request handler](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/handler/RequestHandlerChain.html) for a controller's method.

**Example**: invoke a method if intent name is 'Test':

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

### Request Interceptor

Resolves a [request interceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/RequestInterceptor.html) for a controller's method.

**Example**: @RequestInterceptor print a message if the method is annotated with `@PrintInterceptor`:

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

### Response Interceptor

Resolves a [request interceptor](http://ask-sdk-java-javadocs.s3-website-us-west-2.amazonaws.com/com/amazon/ask/dispatcher/request/interceptor/ResponseInterceptor.html) for a controller's method.

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

## Auto Resolvers

For annotation mappings, predicates and argument resolvers, a resolver can be automatically be resolved from a meta-annotation. The annotation type must be annotated with its corresponding @Auto* meta-annotation, identifying the class which implements the resolution logic:

* @AutoArgumentResolver
* @AutoPredicateResolver
* @AutoExceptionHandler
* @AutoRequestHandler
* @AutoRequestInterceptor
* @AutoResponseInterceptor

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

**Example:** check if a slot is present

```java
@AutoPredicateResolver(NotNull.Plugin.class) // identify the auto-resolver
public @interface SlotExists {
    String value(); // slot name

    class Plugin implements AutoPredicateResolver<SlotExists> {
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
