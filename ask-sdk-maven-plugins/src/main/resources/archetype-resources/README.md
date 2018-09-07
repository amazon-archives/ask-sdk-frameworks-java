# Overview
This maven project template deploys a skill to [AWS Lambda](https://aws.amazon.com/lambda/) using a [CloudFormation](https://aws.amazon.com/cloudformation/) [SAM](https://github.com/awslabs/serverless-application-model) template. It requires you have installed and configured the [ASK CLI](https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html) and [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/installing.html).

# Instructions

#### Create a S3 bucket to host your compiled jars

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

Your project directory will contain a `SAM.yaml` template describing a Lambda Function and referencing the assembled jar: `./target/hello-world-1.0-jar-with-dependencies.jar`. Use the AWS CLI to package this template and your code into a deployable CloudFormation template, `./target/packaged-SAM.yaml`:

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

CloudFormation will create a Stack with a Lambda Function and IAM Role already configured to handle Alexa requests.

### Create a Skill

Now that you've deployed your AWS Lambda Function, you can complete the skill manifest file, `skill.json,` by configuring your function's ARN as the endpoint URI. To discover your function's ARN, use the AWS CLI to list them and copy the `FunctionArn` value:

```bash
aws lambda list-functions
```

Example:
```json
{
    ..
    "FunctionName": "EXAMPLE1231",
    "MemorySize": 512,
    "CodeSize": 123,
    "FunctionArn": "arn:aws:lambda:us-west-2:123456789:function:Example-ABCDEFG",
    "Handler": "com.example.Example::handleRequest",
    ..
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

### Generate and update your Skill's Interaction Model

Your skill's interaction model is generated as part of project compilation:

```bash
mvn compile
```

After compilation, use the ASK CLI to update your skill's interaction model:

```bash
ask api update-model \
  --skill-id amzn1.ask.skill.12345678-1234-1234-1234-123456789012 \
  --locale en-US \
  --file ./target/ask/en-US.json
```

