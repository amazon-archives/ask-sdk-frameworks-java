# ASK SDK Interaction Model Mapper

The Interaction Model Mapper enables you to manage your skill's interaction model as java classes and resources packaged in a standard JAR. It provides tools to generate the interaction model JSON files from the CLI at project build time, and automatically parse a raw `IntentRequest` into its corresponding “plain old java object” (POJO) at runtime.

## Dependency

```xml
<dependency>
    <groupId>com.amazon.alexa</groupId>
    <artifactId>ask-sdk-interaction-model-mapper</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Skill Application

The SkillApplication interface acts as a hook for generating the interaction model from project management tools. For maven integration, see the [ask-sdk-maven-plugins](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-maven-plugins/) maven mojo.

Your skill application class defines a `SkillModel` containing your interaction model's schema and data, and constructs a `Skill` to handle those requests at runtime.

```java
public class MySkill implements SkillApplication {
    @Override
    public SkillModel getSkillModel() {
        return SkillModel.builder()
            .withInvocationName(Locale.US, "hello world")
            .addModel(Model.builder()
                .intent(MyIntent.class)
                .build())
            .build();
    }

    @Override
    public Skill getSkill() {
        IntentMapper intentMapper = IntentMapper.builder()
            .withSkillModel(getSkillModel())
            .build();

        MyIntentHandler myIntentHandler= = new MyIntentHandler(intentMapper);

        return Skills.standard()
            .addRequestHandler(myIntentHandler)
            .build();
    }
}
```

## Intent Schema

To define a new intent for your skill, create a class and annotate it with `@Intent`. The name of the intent defaults to the simple name of the class, but can also be defined explicitly with `@Intent(“DifferentName”)`.

Slots are defined as java bean properties annotated with `@SlotProperty`. The name defaults to the property name, but may also be defined explicitly with `@SlotProperty(name = “differentName”)`. The slot type is derived from the property type, such as `AmazonDate`, but can also be defined explicitly with `@SlotProperty(type = AmazonDate.class)`.

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

## Slot Type Schema

The type of a slot is derived from the property type. For example, the type of the `date` property above is `AmazonDate`, a class annotated with the `@SlotType` annotation, explicitly naming it `AMAZON.DATE`.

```java
@SlotType("AMAZON.DATE")
public abstract class AmazonDate extends BaseSlotValue {
    // ..
}
```

Using the `@SlotType` annotation, you can implement custom slot types as classes:

```java
@SlotType
public class MySlotType extends BaseSlotValue {
}
```

Or as an `enum`, where the symbols correspond to the slot value or a matched [Entity Resolution ID](https://developer.amazon.com/docs/custom-skills/define-synonyms-and-ids-for-slot-type-values-entity-resolution.html).

```java
@SlotType
public enum MySlotType {
    SLOT_ID_1,
    SLOT_ID_2;
}
```

## Intent Mapper

The `IntentMapper` automatically reads a POJO intent instance from a raw `IntentRequest`. Intent and slot type classes contain properties that are inspected at runtime with reflection to interpret a request.

### Intent Classes

The logic for parsing a property from an `IntentRequest` is defined by the implementation of an `IntentPropertyReader`:

```java
public interface IntentPropertyReader<T> {
    T read(IntentPropertyContext context) throws IntentParseException;
}
```

Associate a reader with a type when constructing the `IntentMapper`:

```java
IntentMapper.builder()
    .addIntentPropertyReader(MyType.class, new MyTypeReader())
```

Properties with the type, `MyType`, will now be read with the `MyTypeReader` implementation:

```java
@Intent // IntentPropertyReader only applies to intent types
class MyIntent {
    private MyType myType; // use MyTypeReader to read the value
}
```

Unless explicitly annotated with the `@IntentPropertyReader` annotation:

```java
@Intent
class MyIntent {
    @IntentPropertyReader(MyOtherPropertyReader.class)
    private Object property;
}
```

You can then parse a raw `IntentRequest` into the `MyIntent` object:

```java
IntentRequest request = ...;
IntentMapper mapper = ...;

MyIntent myIntent = mapper.parseIntent(request, MyIntent.class);
```

The behavior of `IntentPropertyReader` is the same for all properties on intent classes, except those that represent the intent's slots must also be annotated with `@SlotProperty` annotation:

```java
@Intent
class MyIntent {
    @SlotProperty
    private AmazonDate dateSlot;

    @SlotProperty(type = AmazonDate.class) // slot has type, AMAZON.DATE
    private Slot explicitDateSlot; // use default Slot reader to read the value

    @SlotProperty(type = AmazonDate.class) // slot has type, AMAZON.DATE
    @IntentPropertyReader(MyCustomReader.class) // use a custom reader class
    private MyCustomObject customDateRepresentation;
}
```

### Slot Type Classes

Similarly to intent classes, the logic for parsing a property from a slot value is defined by the implementation of a `SlotPropertyReader`:

```java
public interface SlotPropertyReader<T> {
    T read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException;
}
```

Slot property readers are then associated with a type when constructing the `IntentMapper`:

```java
IntentMapper.builder()
    .addSlotPropertyReader(MyType.class, new MyTypeReader())
```

Properties on slot type classes with the type, `MyType`, will now be parsed with the `MyTypeReader` implementation:

```java
@SlotType // SlotPropertyReader only applies to slot types
class MyIntent {
    private MyType myType;
}
```

Unless explicitly annotated with `@SlotPropertyReader`:

```java
@SlotType
class MyIntent {
    @SlotPropertyReader(MyTypeReader.class)
    private MyType myType;
}
```

Most slot types extend `BaseSlotValue` which simply extracts the raw `Slot` value:

```java
public abstract class BaseSlotValue {
    @SlotPropertyReader(RawSlotPropertyReader.class)
    private Slot slot;

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
```

## Intent Request Handler

You can automatically derive the `canHandle` and `IntentRequest` parsing logic of a `RequestHandler` for your intent class by extending the `IntentRequestHandler`:

```java
public class MyIntentHandler extends IntentRequestHandler<MyIntent> {
    public MyIntentHandler(IntentMapper intentMapper) {
        super(MyIntent.class, // pass in your intent's class
              intentMapper); // with a configured intent mapper
    }

    @Override
    public Optional<Response> handle(HandlerInput input, MyIntent myIntent) {
        // handle the intent request
    }
}
```

## Intent Data

An intent's class definition defines the intent schema (name and slot names/types), but the interaction model also contains data such as sample utterances and confirmation/elicitation dialog management prompts. This data is associated when registering the intent by constructing an `IntentDataSource` or by annotating the intent type with an `@IntentResource`.

For example, given a JSON resource file: `src/main/resources/my_intent_en_US.json`

```json
{
  "samples" : [
    "what is the {date}"
  ],
  "confirmationRequired": true,
  "confirmations": [
    {
      "type": "PlainText",
      "value": "are you sure?"
    }
  ],
  "slots": {
    "date": {
      "samples": [
        "the date is {date}"
      ],
      "confirmationRequired": true,
      "confirmations": [
        {
          "type": "PlainText",
          "value": "did you say the date was {date}?"
        }
      ],
      "elicitationRequired": true,
      "elicitations" : [
        {
          "type": "PlainText",
          "value": "what is the date?"
        }
      ]
    }
  }
}
```

We can create an `IntentDataSource` which will scan for resources relative to the `MyIntent.class`, and associate it with the intent at registration time.

```java
model.intent(MyIntent.class, IntentData.resource()
    .withResourceClass(MyIntent.class)
    .withName("my_intent")
    .build());
```

Alternatively, we can statically associate data with the intent's type by annotating it with an `@IntentResource` annotation. It creates the same resource manually instantiated above, and also supports custom resource classes, suffixes, and file formats:

```java
@Intent
@IntentResource("my_intent")
public class MyIntent {
    // ..
}
```

Rendering this class for the `en-US` locale results in the following interaction model:

```json
{
  "languageModel" : {
    "intents" : [
      {
        "name" : "MyIntent",
        "slots" : [
          {
            "name" : "date",
            "type" : "AMAZON.DATE",
            "samples" : [
              "the date is {date}"
            ]
          }
        ],
        "samples" : [
          "what is the {date}"
        ]
      }
    ]
  },
  "dialog" : {
    "intents" : [
      {
        "name" : "MyIntent",
        "confirmationRequired" : true,
        "prompts" : {
          "confirmation" : "Confirm.Intent-MyIntent"
        },
        "slots" : [
          {
            "name" : "date",
            "type" : "AMAZON.DATE",
            "confirmationRequired" : true,
            "elicitationRequired" : true,
            "prompts" : {
              "elicitation" : "Elicit.Intent-MyIntent.IntentSlot-date",
              "confirmation" : "Confirm.Intent-MyIntent.IntentSlot-date"
            }
          }
        ]
      }
    ]
  },
  "prompts" : [
    {
      "id" : "Confirm.Intent-MyIntent",
      "variations" : [
        {
          "type" : "PlainText",
          "value" : "are you sure?"
        }
      ]
    },
    {
      "id" : "Confirm.Intent-MyIntent.IntentSlot-date",
      "variations" : [
        {
          "type" : "PlainText",
          "value" : "did you say the date was {date}?"
        }
      ]
    },
    {
      "id" : "Elicit.Intent-MyIntent.IntentSlot-date",
      "variations" : [
        {
          "type" : "PlainText",
          "value" : "what is the date?"
        }
      ]
    }
  ]
}
```

## Slot Type Data

Like with intents, a slot type's interaction model data can be associated when registering its class by creating a `SlotTypeDataSource`, or by annotating the type's class with a `@SlotTypeResource` annotation.

For example, given a JSON resource file: `src/main/resources/my_slot_type.json`

```json
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
model.slotType(MySlotType.class, SlotTypeData.resource()
    .withResourceClass(MySlotType.class)
    .withName("my_slot_type")
    .build());
```

Or, we can statically associate data with the slot's type by annotating it with a `@SlotTypeResource` annotation. It creates the same resource manually instantiated above:

```java
@SlotType
@SlotTypeResource("my_slot_Type")
public class MySlotType {
    // ..
}
```

Enums are also supported, but should only be used when fuzzy matching is not required:

```java
@SlotType
@SlotTypeResource("my_slot_Type")
public enum MySlotType {
    SLOT_ID_1,
    SLOT_ID_2;
}
```

Rendering this class for the `en-US` locale results in the following slot type schema:

```json
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

## Data Localization

By default, data for intents and slot types are organized like [java resource bundles](https://docs.oracle.com/javase/7/docs/api/java/util/ResourceBundle.html), so different files are selected when generating an interaction model for a particular locale.

For example, given the following intent definition:

```java
package com.example.intents;

@Intent
@IntentResource("my_intent", suffix = ".json")
class MyIntent {}
```

Generating the interaction model for en-US will select the first file that exists from the following:

* `src/main/resources/com/example/my_intent_en_US.json`
* `src/main/resources/com/example/my_intent/en_US.json`
* `src/main/resources/com/example/my_intent_en.json`
* `src/main/resources/com/example/my_intent/en.json`
* `src/main/resources/com/example/my_intent.json`
* `src/main/resources/com/example/my_intent/global.json`

This process is called “resource candidate enumeration”. You can provide custom enumerators when explicitly associating data sources with intent classes:

```java
model.intent(MyIntent.class, IntentData.resource()
    .withResourceClass(MyIntent.class)
    .withName("my_intent")
    .addResourceCandidateEnumerator(new MyEnumerator())
    .build());
```
