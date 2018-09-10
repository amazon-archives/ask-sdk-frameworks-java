# Apache Freemarker MVC View Resolver

This plugin integrates the [Apache FreeMarker](https://freemarker.apache.org/) templating engine as a view format in the [Alexa Skills Kit MVC Framework](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/samgood-dev/ask-sdk-mvc), enabling developers to render responses using `.ftl` template files.

# Dependency

```xml
<dependencies>
  <dependency>
    <groupId>com.amazon.alexa</groupId>
    <artifactId>ask-sdk-mvc-freemarker</artifactId>
    <version>0.1.0</version>
  </dependency>
</dependencies>
```

# Usage
Install the Freemarker `ViewResolver` as part of your `SkillModule`:

```java
package com.example;

public class MyModule implements SkillModule {
	@Override
    public void buildMvc(MvcSdkModule mvc) {
    	mvc.addViewResolver(FreeMarkerViewResolver.builder()
        	.withResourceClass(getClass())
        	.withPrefix("views/")
            .withName("my_view")
	        .build());
    }
}
```

Create a resource file containing the view template, `src/main/resources/com/example/my_view.ftl`:

```
<#if favoriteColor??>
    <#assign speechText="Your favorite color is ${favoriteColor}. Goodbye.">
<#else>
    <#assign speechText="I'm not sure what your favorite color is. You can say, my favorite color is red.">
</#if>
{
    "outputSpeech": {
        "type": "PlainText",
        "text": "${speechText}"
    },
    "shouldEndSession": true,
    "card": {
        "type": "Simple",
        "title": "ColorSession",
        "content": "${speechText}"
    }
}
```

And return a `ModelAndView` containing the `favoriteColor` model attribute and view name `my_view` in the controller:

```java
public class MyController {
	@IntentMapping(type = MyColorIsIntent.class)
    public ModelAndView handleIntent(MyColorIsIntent intent) {
    	Map<String, Object> model = new HashMap<>();
        model.put("favoriteColor", intent.getColor());
        return new ModelAndView("my_view", model);
    }
}
```
