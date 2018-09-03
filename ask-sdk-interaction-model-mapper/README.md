# Overview

This framework enables a code-first approach to the development of a skill by abstracting the interaction model. Alexa types such as Intents and Slots are defined as Java classes, and corresponding interaction model data such as sample utterances, prompts, synonyms, etc. are stored in resource bundles. A skill is then constructed from a collection of these entities, and the interaction model is rendered from the CLI. This abstraction enables developers to componentize and share parts of their skill with others and promotes a standard software build/deploy lifecycle for skills.

The framework is also designed to be extensible so as to empower the community to invent and share their own tools. For example, a developer may prefer to write their interaction model data in XML over JSON, or use a grammar to generate sample utterances.

# Defining a Skill

A skill is composed from localized invocation names and a collection of 'Models':

```java
SkillModel.builder()
  .addInvocationName(Locale.en_US, "my skill")
  .addModel(Model.builder()
    .intent(MyIntent.class) // add 'MyIntent' to this skill
    .build());
```

## Defining an Intent

An intent is added to a `Model` by passing its class to the `intent` method:

```java
Model.builder()
  .intent(MyIntent.class)
```

An Intent's class defines the schema (name and slots) and references any associated interaction model data:

```java
@Intent
@IntentResource("models/my_intent")
public class MyIntent {
  private AmazonDate date;
  // getters and setters ..
}
```

* `@Intent` identifies this class as an Intent with the name `MyIntent`.
* `@IntentResource` provides a resource bundle name for the interaction model data.

This class enables the following capability:
* Generate the intent's interaction model sections.
* Generate an `IntentParser` which can create an instance from a raw `IntentRequest`.

For example, given a resource file `models/my_intent.json`:

```json
{
  "samples": [ "sample1", "sample2" ]
}
```

.. the intent schema can be generated:

```json
{
  "name": "MyIntent",
  "slots": [
    {
      "name": "date",
      "type": "AMAZON.DATE"
    }
  ],
  "samples": [
    "sample1",
    "sample2"
  ]
}
```

.. and the `IntentRequest` can be parsed:

```java
IntentMapper intentMapper = model.getIntentMapper();
MyIntent myIntent = intentMapper.parse(intentRequest, MyIntent.class);
```

## Defining a custom Slot Type

In the prior example, MyIntent has one slot with type `AmazonDate`, corresponding to the `AMAZON.DATE` built-in slot type. Like intents, slot types are defined as annotated classes and resource bundles.

To demonstrate, let's define a custom slot type, 'MySlotType'.

```java
@SlotType
@SlotTypeResource("models/my_slot_type")
public class MySlotType {
}
```

* `@SlotType` identifies this class as a custom slot type with the name 'MySlotType'.
* `@SlotTypeResource` provides a resource bundle name for the interaction model data.

Given a 'models/my_slot_type.json' resource file:

```json
{
  "values": {
    "dog": {
      "synonyms": ["pupper", "good boy"]
    },
    "cat": {
      "synonyms": ["kitty"]
    }
  }
}
```

.. the slot type schema can be generated:

```json
{
  "name": "MySlotType",
  "values": [
    {
      "id": "dog",
      "name": {
        "value": "dog",
        "synonyms": ["pupper", "good boy"]
      }
    }
  ]
}
```

## Putting it all together

We can now update 'MyIntent' to use 'MySlotType' as a slot:

```java
@Intent
@IntentResource("models/my_intent")
public class MyIntent {
  private AmazonDate date;
  private MySlotType custom;
  // getters and setters ..
}
```

.. which then renders the new intent schema, includign the custom slot type in the interaction model:

```json
{
  "intents": [
    {
      "name": "MyIntent",
      "slots": [
        {
          "name": "date",
          "type": "AMAZON.DATE"
        },
        {
          "name": "custom",
          "type": "MySlotType"
        }
      ],
      "samples": ["sample1", "sample2"]
    }
  ],
  "types": [
    {
      "name": "MySlotType",
      "values": [
        {
          "id": "dog",
          "name": {
            "value": "dog",
            "synonyms": ["pupper", "good boy"]
          }
        }
      ]
    }
  ]
}
```
