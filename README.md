## Ask SDK Frameworks Java

This repository contains framework and plugin libraries for building Alexa [Custom Skills](https://developer.amazon.com/docs/custom-skills/understanding-custom-skills.html) in Java.

* [ASK SDK Model-View-Controller (MVC) Framework](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-mvc) - adapts the MVC pattern to skills. Supports mapping requests by name/type to methods on controler classes and rendering responses from view scripts/templates.
* [ASK SDK MVC FreeMarker View Resolver](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-mvc-freemarker) - adds support for [Apache FreeMarker](https://freemarker.apache.org/) templates as MVC views.
* [ASK SDK Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper) - generate an interaction model from code and automatically interpret raw `IntentRequests` into corresponding 'plain old java object' instances.
* [ASK SDK Interaction Model Code Generator](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-codegen) - generates Java code from standard [interaction model](https://developer.amazon.com/docs/smapi/interaction-model-schema.html) files.
* [ASK SDK Interaction Model](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model) - Java classes for parsing the [interaction model](https://developer.amazon.com/docs/smapi/interaction-model-schema.html) JSON schema.
* [ASK SDK Maven Plugins](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-maven-plugins) - generate the interaction model JSON files from a project built with [MVC](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-mvc) or the [Interaction Model Mapper](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-interaction-model-mapper).

## Samples of MVC Skill

* [Colorpicker](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/colorpicker)
* [Decision Tree](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/decisiontree)
* [Tic Tac Toe](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/tictactoe)
* [Trivia](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/samples/trivia)

## License

These libraries are licensed under the Apache 2.0 License.
