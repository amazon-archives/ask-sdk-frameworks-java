# ASK SDK Interaction Model Code Generator

This tool generates java classes from [interaction model JSON files](https://developer.amazon.com/docs/custom-skills/create-the-interaction-model-for-your-skill.html) for use with the [Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper).


# Installation

Checkout out the `ask-sdk-interaction-model-codegen` maven package:

```bash
git checkout git@github.com:alexa-labs/ask-sdk-frameworks-java.git
cd ask-sdk-interaction-model-codegen
```

Compile and package an executable jar for the code generator application:

```bash
# compile and package the executable JAR
mvn package

# execute the JAR
java -jar ./target/ask-sdk-interaction-model-codegen-0.1.0-jar-with-dependencies.jar
```

# Usage

Execute the code generator application with a skill name, java package namespace and an interaction model file per locale. It will generate java classes for the intents and slot types in your models, and JSON resource files containing the interaction model data such as sample utterances and dialog prompts.

```bash
java -jar ./target/ask-sdk-interaction-model-codegen-0.1.0-jar-with-dependencies.jar \
    --model en-US=./models/en-US.json \
    --model de-DE=./models/de-DE.json \
    --namespace com.example \
    --output ./target/generated-sources/ \
    --skill-name MyName
```

The `./target/generated-sources/` folder now contains the following directory structure:

* `src/main/java/com/example/MyNameSkill.java`
* `src/main/java/com/example/intents/MyIntent.java`
* `src/main/java/com/example/slots/MySlotType.java`
* `src/main/resources/com/example/intents/my_intent_de_DE.json`
* `src/main/resources/com/example/intents/my_intent_en_US.json`
* `src/main/resources/com/example/slots/my_slot_type_de_DE.json`
* `src/main/resources/com/example/slots/my_slot_type_en_US.json`

See the usage help message:

```bash
usage: GeneratorMain
 -h,--help               print this help message
 -m,--model <arg>        locale and model path(s), e.g. -m
                         en_US=path/to/en_US_file.json
 -n,--namespace <arg>    java namespace of generated code
 -o,--output <arg>       output path of generated artifacts
 -s,--skill-name <arg>   name of generated skill class implementing
                         SkillApplication
```
