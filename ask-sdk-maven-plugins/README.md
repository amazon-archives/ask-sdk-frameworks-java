# Overview

The Alexa Skills Kit [Maven](maven.apache.org) Plugins provide a [mojo](https://maven.apache.org/developers/mojo-api-specification.html) to generate your skill's interaction model, and an [archetype](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html) to generate a standard [MVC](https://github.com/alexa-labs/ask-sdk-frameworks-java/tree/master/ask-sdk-mvc) project.

# Interaction Model Generation Mojo

This Mojo generates the final interaction model JSON files required by Alexa as part of the maven `compile`.

# Usage

Install the plugin into your `pom.xml` as part of the `compile` phase, and configure your skill class which extends `MvcSkillApplication` as the `className`:

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

Running `mvn compile` will compile your code, load it as java process and generate an interaction model for each locale that has an invocation name declared in the `MvcSkillApplication`.

Each file is written to `./target/ask/`, e.g. `./target/ask/en-US.json`.

Optionally, you can specify the individual locales you wish to generate a model for:

```xml
<configuration>
    <destinationDir>ask</destinationDir>
    <className>com.example.HelloWorldSkill</className>
    <locales>
        <param>en-US</param>
    </locales>
</configuration>
```

# MVC Skill Project Archetype

This maven project template sets up a MVC skill project which deploys to [AWS Lambda](https://aws.amazon.com/lambda/) using a [CloudFormation](https://aws.amazon.com/cloudformation/) [SAM](https://github.com/awslabs/serverless-application-model) template. It requires you have installed and configured the [ASK CLI](https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html) and [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/installing.html).

## Instructions

### Generate the project directory using Maven

Execute the following command and follow the interactive prompts on the CLI:

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.amazon.alexa \
  -DarchetypeArtifactId=ask-sdk-maven-plugins \
  -DarchetypeVersion=0.1.0
```

Example usage:

```bash
[INFO] Generating project in Interactive mode
Define value for property 'groupId': com.example
Define value for property 'artifactId': hello
Define value for property 'version' 1.0-SNAPSHOT: :
Define value for property 'package' com.example: :
Define value for property 'invocationName': hello world
Define value for property 'skillName': HelloWorld
Confirm properties configuration:
groupId: com.example
artifactId: hello
version: 1.0-SNAPSHOT
package: com.example
invocationName: hello world
skillName: HelloWorld
Y
```

### Create a S3 bucket to host your compiled jars

Your lambda function is created from a JAR hosted in S3, so create a bucket (or re-use an existing one) and take note of its name:

```bash
aws s3 mb my.bucket.name
```

### Use maven to generate the Interaction Model and JAR

```bash
mvn package
```

Building your project outputs two artifacts to the `target` directory:
* A single jar containing your code and all dependencies for Lambda: `./target/hello-world-1.0-jar-with-dependencies.jar`
* The interaction models (one per locale): `./target/ask/en-US.json`

### Deploy a lambda function with a [CloudFormation](https://aws.amazon.com/cloudformation/) [SAM](https://github.com/awslabs/serverless-application-model) template

Your project directory will contain a `SAM.yaml` template describing a Lambda function and referencing the assembled jar: `./target/hello-world-1.0-jar-with-dependencies.jar`. Use the AWS CLI to package this template with your code as a deployable CloudFormation template, `./target/packaged-SAM.yaml`:

```bash
aws cloudformation package \
  --template-file ./SAM.yaml \
  --s3-bucket my.bucket.name \
  --output-template-file ./target/packaged-SAM.yaml
```

This will upload your jar to S3 and output a standard CloudFormation template to `./target/packaged-SAM.yaml`, which you can then deploy to AWS as a [Stack](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/stacks.html):

```bash
aws cloudformation deploy \
  --template-file ./target/packaged-SAM.yaml \
  --stack-name HelloWorldStack \
  --capabilities CAPABILITY_IAM
```

CloudFormation will create a stack with a lambda function and IAM role already configured to handle alexa requests.

### Create your skill

Now that you've deployed your function, you can complete the skill manifest file, `skill.json`. Use the AWS CLI to list the lambda functions them and copy the `FunctionArn` value as your endpoint URI.

```bash
aws lambda list-functions
```

Example:
```json
{
    "FunctionName": "EXAMPLE1231",
    "MemorySize": 512,
    "CodeSize": 123,
    "FunctionArn": "arn:aws:lambda:us-west-2:123456789:function:Example-ABCDEFG",
    "Handler": "com.example.Example::handleRequest",
}
```

Replace the `uri` value in your `./skill.json`:

```json
"apis": {
  "custom": {
    "endpoint": {
      "uri": "arn:aws:lambda:us-west-2:123456789:function:Example-ABCDEFG"
    }
  }
}
```

Then, create the skill using the ASK CLI:

```bash
ask api create-skill -f ./skill.json
```

Example output:

```
Create skill request submitted.
Skill ID: amzn1.ask.skill.12345678-1234-1234-1234-123456789012
Please use the following command to track the skill status:
    ask api get-skill-status -s amzn1.ask.skill.12345678-1234-1234-1234-123456789012
```

### Generate and update your skill's interaction model

Your skill's interaction model is generated as part of project compilation:

```bash
mvn compile
```

After compilation, use the ASK CLI to build the model:

```bash
ask api update-model \
  --skill-id amzn1.ask.skill.12345678-1234-1234-1234-123456789012 \
  --locale en-US \
  --file ./target/ask/en-US.json
```
