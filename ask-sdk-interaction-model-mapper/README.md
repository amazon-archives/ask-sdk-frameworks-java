## ASK SDK Interaction Model Mapper

The Interaction Model Mapper enables you to manage your skill's interaction model as java classes and resources packaged in a standard JAR. It provides tools to generate the interaction model JSON files from the CLI at project build time, and automatically parse a raw `IntentRequest` into its corresponding “plain old java object” (POJO) at runtime.

## Index

* [Configuration](#configuration)
* [Interaction Model Schema](#interaction-model-schema)
   * [Intents](#intents)
   * [Slot Types](#slot-types)
* [Interaction Model Data](#interaction-model-data)
   * [Samples, Confirmations and Elicitations](#samples-confirmations-and-elicitations)
   * [Slot Values, IDs and Synonyms](#slot-values-ids-and-synonyms)
   * [Localization](#localization)
* [Intent Mapper](#intent-mapper)
   * [Intents](#intents-1)
   * [Slot Types](#slot-types-1)
* [Intent Request Handler](#intent-request-handler)
* [Built-in Intents](#built-in-intents)
   * [Standard](#standard-intents)
* [Built-in Slot Types](#built-in-slot-types)
   * [Numbers, Dates and Times](#numbers-dates-and-times)
   * [Phrases](#phrases)
   * [List Types](#list-types)
   * [Literal](#literal)

## Configuration

Add a dependency on `ask-sdk-interaction-model-mapper` to your `pom.xml`:

```xml
<dependency>
    <groupId>com.amazon.alexa</groupId>
    <artifactId>ask-sdk-interaction-model-mapper</artifactId>
    <version>0.1.0</version>
</dependency>
```

Implement the `SkillModelSupplier` interface and add all invocation names, intents, slot types and data to your skill's `SkillModel`.

```java
package com.example;

public class HelloWorldSkill implements SkillModelSupplier {
    @Override
    public SkillModel getSkillModel() {
        return SkillModel.builder()
            .withInvocationName(Locale.US, "hello world")
            .addModel(Model.builder()
                .intent(HelpIntent.class)
                .intent(StopIntent.class)
                .build())
            .build();
    }
}
```

The `SkillModel` contains your interaction model's intents and slot type schemas, and data such as invocation name, sample utterances and prompts for each target locale.

To generate interaction models from maven, add the `build-model` task from [ask-sdk-maven-plugins](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-maven-plugins) and configure your [SkillModelSupplier](#SkillModelSupplier) class as the target:

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

Then generate the JSON models with [mvn](https://maven.apache.org/install.html):

```bash
mvn compile
```

And update your Skill's model with the [ask cli](https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html):

```bash
ask api update-model \
    --file ./target/ask/en-US.json \
    --locale en-US \
    --skill-id <your-skill-id>
```

## Interaction Model Schema
Both custom and built-in variations of Alexa intents and slot types are defined as java classes annotated with `@Intent` or `@SlotType`.

### Intents

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

### Slot Types

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

## Interaction Model Data

Intents and slot types are associated with localized data such as sample utterances, prompts, slot values and synoynms, etc. stored in resource bundles.

### Samples, Confirmations and Elicitations

An intent can be associated with data by passing an `IntentDataSource` when registering it with the `Model.Builder`, or by statically annotating its type with an `@IntentResource`. Both methods point to resource files.

For example, given a JSON resource file: `src/main/resources/com/example/intents/my_intent_en_US.json`

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

We can create an `IntentDataSource` which will scan for resources with name `my_intent` relative to the `MyIntent` class, and associate it with the intent at registration time.

```java
Model model = Model.builder()
	.intent(MyIntent.class, IntentData.resource()
        .withResourceClass(MyIntent.class)
        .withName("my_intent")
        .build())
    .build();
```

Alternatively, we can statically associate data with the intent's type by annotating it with an `@IntentResource` annotation. It creates the same resource manually instantiated above, and also supports custom resource classes, suffixes, and file formats:

```java
package com.example.intents;

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

### Slot Values, IDs and Synonyms

Like with intents, a slot type's interaction model data can be associated when registering its class by creating a `SlotTypeDataSource`, or by annotating the type's class with a `@SlotTypeResource` annotation.

For example, given a JSON resource file: `src/main/resources/com/example/slots/my_slot_type.json`

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
Model model = Model.builder()
	.slotType(MySlotType.class, SlotTypeData.resource()
        .withResourceClass(MySlotType.class)
        .withName("my_slot_type")
        .build())
    .build();
```

Or, we can statically associate data with the slot's type by annotating it with a `@SlotTypeResource` annotation. It creates the same resource manually instantiated above:

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

### Localization

By default, data for intents and slot types are organized like [java resource bundles](https://docs.oracle.com/javase/7/docs/api/java/util/ResourceBundle.html), so different files are selected when generating an interaction model for a particular locale.

For example, given the following intent definition:

```java
package com.example.intents;

@Intent
@IntentResource("my_intent", suffix = ".json")
class MyIntent {}
```

Generating the interaction model for en-US will select the first file that exists from the following:

* `src/main/resources/com/example/intents/my_intent_en_US.json`
* `src/main/resources/com/example/intents/my_intent/en_US.json`
* `src/main/resources/com/example/intents/my_intent_en.json`
* `src/main/resources/com/example/intents/my_intent/en.json`
* `src/main/resources/com/example/intents/my_intent.json`
* `src/main/resources/com/example/intents/my_intent/global.json`

This process is called “resource candidate enumeration”. You can provide custom enumerators when explicitly associating data sources with intent classes:

```java
model.intent(MyIntent.class, IntentData.resource()
    .withResourceClass(MyIntent.class)
    .withName("my_intent")
    .addResourceCandidateEnumerator(new MyEnumerator())
    .build());
```

## Intent Mapper

The `IntentMapper` automatically reads a POJO intent instance from a raw `IntentRequest`. Intent and slot type classes contain properties that are inspected at runtime with reflection to interpret a request.

### Intents

The logic for parsing a property from an `IntentRequest` is defined by the implementation of an `IntentPropertyReader`:

```java
public interface IntentPropertyReader<T> {
    T read(IntentRequest intentRequest) throws IntentParseException;
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

Remember, properties that represent the intent's slots must also be annotated with `@SlotProperty` annotation:

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

### Slot Types

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

## Built-in Intents

### [Standard]((https://developer.amazon.com/docs/custom-skills/standard-built-in-intents.html))

The standard built-in intents can be found in the `com.amazon.ask.interaction.types.intent` namespace:

| Intent | Class |
|-|-|
|`AMAZON.CancelIntent` | `CancelIntent` |
|`AMAZON.FallbackIntent` | `FallbackIntent` |
|`AMAZON.HelpIntent` | `HelpIntent` |
|`AMAZON.LoopOffIntent` | `LoopOffIntent` |
|`AMAZON.LoopOnIntent` | `LoopOnIntent` |
|`AMAZON.MoreIntent` | `MoreIntent` |
|`AMAZON.NavigateHomeIntent` | `NavigateHomeIntent` |
|`AMAZON.NavigateSettingsIntent` | `NavigateSettingsIntent` |
|`AMAZON.NextIntent` | `NextIntent` |
|`AMAZON.NoIntent` | `NoIntent` |
|`AMAZON.PageDownIntent` | `PageDownIntent` |
|`AMAZON.PageUpIntent` | `PageUpIntent` |
|`AMAZON.PauseIntent` | `PauseIntent` |
|`AMAZON.PreviousIntent` | `PreviousIntent` |
|`AMAZON.RepeatIntent` | `RepeatIntent` |
|`AMAZON.ResumeIntent` | `ResumeIntent` |
|`AMAZON.ScrollDownIntent` | `ScrollDownIntent` |
|`AMAZON.ScrollLeftIntent` | `ScrollLeftIntent` |
|`AMAZON.ScrollRightIntent` | `ScrollRightIntent` |
|`AMAZON.ScrollUpIntent` | `ScrollUpIntent` |
|`AMAZON.ShuffleOffIntent` | `ShuffleOffIntent` |
|`AMAZON.ShuffleOnIntent` | `ShuffleOnIntent` |
|`AMAZON.StartOverIntent` | `StartOverIntent` |
|`AMAZON.StopIntent` | `StopIntent` |
|`AMAZON.YesIntent` | `YesIntent` |

## [Built-in Slot Types]((https://developer.amazon.com/docs/custom-skills/slot-type-reference.html))


### [Numbers, Dates and Times](https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#numbers-dates-and-times)

| Slot Type | Class|
|-|-|
|`AMAZON.DATE`|`com.amazon.ask.interaction.types.date.AmazonDate`|
|`AMAZON.DURATION`|`com.amazon.ask.interaction.types.AmazonDuration`|
|`AMAZON.FOUR_DIGIT_NUMBER`|`com.amazon.ask.interaction.types.FourDigitNumber`|
|`AMAZON.NUMBER`|`com.amazon.ask.interaction.types.AmazonNumber`|
|`AMAZON.PhoneNumber`|`com.amazon.ask.interaction.types.PhoneNumber`|
|`AMAZON.TIME`|`com.amazon.ask.interaction.types.time.AmazonTime`|

### [Phrases](https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#phrases)

| Slot Type | Class|
|-|-|
|`AMAZON.SearchQuery`|`com.amazon.ask.interaction.types.SearchQuery`|

### [List Types](https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#list-types)

List slot types can be found in the `com.amazon.ask.interaction.types.slot.list` namespace:

| Slot Type | Class|
|-|-|
| `AMAZON.Actor` | `Actor` |
| `AMAZON.AdministrativeArea` | `AdministrativeArea` |
| `AMAZON.AggregateRating` | `AggregateRating` |
| `AMAZON.Airline` | `Airline` |
| `AMAZON.Airport` | `Airport` |
| `AMAZON.Animal` | `Animal` |
| `AMAZON.Artist` | `Artist` |
| `AMAZON.AT_CITY` | `ATCity` |
| `AMAZON.Athlete` | `Athlete` |
| `AMAZON.AT_REGION` | `ATRegion` |
| `AMAZON.Author` | `Author` |
| `AMAZON.Book` | `Book` |
| `AMAZON.BookSeries` | `BookSeries` |
| `AMAZON.BroadcastChannel` | `BroadcastChannel` |
| `AMAZON.CivicStructure` | `CivicStructure` |
| `AMAZON.Color` | `Color` |
| `AMAZON.Comic` | `Comic` |
| `AMAZON.Corporation` | `Corporation` |
| `AMAZON.Country` | `Country` |
| `AMAZON.CreativeWorkType` | `CreativeWorkType` |
| `AMAZON.DayOfWeek` | `DayOfWeek` |
| `AMAZON.DE_CITY` | `DECity` |
| `AMAZON.DE_FIRST_NAME` | `DEFirstName` |
| `AMAZON.DE_REGION` | `DERegion` |
| `AMAZON.Dessert` | `Dessert` |
| `AMAZON.DeviceType` | `DeviceType` |
| `AMAZON.Director` | `Director` |
| `AMAZON.Drink` | `Drink` |
| `AMAZON.EducationalOrganization` | `EducationalOrganization` |
| `AMAZON.EuropeCity` | `EUCity` |
| `AMAZON.EventType` | `EventType` |
| `AMAZON.Festival` | `Festival` |
| `AMAZON.FictionalCharacter` | `FictionalCharacter` |
| `AMAZON.FinancialService` | `FinancialService` |
| `AMAZON.Food` | `Food` |
| `AMAZON.FoodEstablishment` | `FoodEstablishment` |
| `AMAZON.Game` | `Game` |
| `AMAZON.GB_CITY` | `GBCity` |
| `AMAZON.GB_FIRST_NAME` | `GBFirstName` |
| `AMAZON.GB_REGION` | `GBRegion` |
| `AMAZON.Genre` | `Genre` |
| `AMAZON.Landform` | `Landform` |
| `AMAZON.LandmarksOrHistoricalBuildings` | `LandmarksOrHistoricalBuildings` |
| `AMAZON.Language` | `Language` |
| `AMAZON.LocalBusiness` | `LocalBusiness` |
| `AMAZON.LocalBusinessType` | `LocalBusinessType` |
| `AMAZON.MedicalOrganization` | `MedicalOrganization` |
| `AMAZON.Month` | `Month` |
| `AMAZON.Movie` | `Movie` |
| `AMAZON.MovieSeries` | `MovieSeries` |
| `AMAZON.MovieTheater` | `MovieTheater` |
| `AMAZON.MusicAlbum` | `MusicAlbum` |
| `AMAZON.MusicCreativeWorkType` | `MusicCreativeWorkType` |
| `AMAZON.MusicEvent` | `MusicEvent` |
| `AMAZON.MusicGroup` | `MusicGroup` |
| `AMAZON.Musician` | `Musician` |
| `AMAZON.MusicPlaylist` | `MusicPlaylist` |
| `AMAZON.MusicRecording` | `MusicRecording` |
| `AMAZON.MusicVenue` | `MusicVenue` |
| `AMAZON.MusicVideo` | `MusicVideo` |
| `AMAZON.Organization` | `Organization` |
| `AMAZON.Person` | `Person` |
| `AMAZON.PostalAddress` | `PostalAddress` |
| `AMAZON.Professional` | `Professional` |
| `AMAZON.ProfessionalType` | `ProfessionalType` |
| `AMAZON.RadioChannel` | `RadioChannel` |
| `AMAZON.Residence` | `Residence` |
| `AMAZON.Room` | `Room` |
| `AMAZON.ScreeningEvent` | `ScreeningEvent` |
| `AMAZON.Service` | `Service` |
| `AMAZON.SocialMediaPlatform` | `SocialMediaPlatform` |
| `AMAZON.SoftwareApplication` | `SoftwareApplication` |
| `AMAZON.SoftwareGame` | `SoftwareGame` |
| `AMAZON.SoftwareGenre` | `SoftwareGenre` |
| `AMAZON.Sport` | `Sport` |
| `AMAZON.SportsEvent` | `SportsEvent` |
| `AMAZON.SportsTeam` | `SportsTeam` |
| `AMAZON.StreetAddress` | `StreetAddress` |
| `AMAZON.TelevisionChannel` | `TelevisionChannel` |
| `AMAZON.TVEpisode` | `TVEpisode` |
| `AMAZON.TVSeason` | `TVSeason` |
| `AMAZON.TVSeries` | `TVSeries` |
| `AMAZON.US_CITY` | `USCity` |
| `AMAZON.US_FIRST_NAME` | `USFirstName` |
| `AMAZON.US_STATE` | `USState` |
| `AMAZON.VideoGame` | `VideoGame` |
| `AMAZON.WeatherCondition` | `WeatherCondition` |
| `AMAZON.WrittenCreativeWorkType` | `WrittenCreativeWorkType` |


### [Literal](https://developer.amazon.com/docs/custom-skills/literal-slot-type-reference.html)

*Note: this slot type is only available in en-US.*

| Slot Type | Class|
|-|-|
|`AMAZON.LITERAL`|`com.amazon.ask.interaction.types.AmazonLiteral`|
