# ASK SDK Frameworks Example - Color Picker
This sample shows how to create a skill that illustrates the following concepts:

- Custom slot type: demonstrates using custom slot types to handle a finite set of known values
- Dialog and Session state: Handles two models, both a one-shot ask and tell model, and a multi-turn dialog model.

## Setup
To run this example we will:
- Build the skill, which will produce a fat JAR containing the skill logic and all its dependencies for Lambda, as well as the interaction model JSON which will be uploaded to Alexa.
- Deploy the skill JAR to Lambda using the provided [SAM template](https://docs.aws.amazon.com/lambda/latest/dg/serverless_app.html)
- Create the skill in Alexa using the ASK CLI and the provided `skill.json`

### Prerequisites
- [Apache Maven](https://maven.apache.org/), which will be used to build the skill and produce a deployment JAR and generate a skill interaction model.
- An installed [AWS CLI](https://aws.amazon.com/cli/) configured against your AWS account. This will be used to deploy your skill logic to AWS Lambda.
- An installed [ASK CLI](https://developer.amazon.com/docs/smapi/quick-start-alexa-skills-kit-command-line-interface.html) configured against your Amazon developer account. This will be used to create and configure your skill with Alexa.
- A S3 bucket. This bucket will be used by CloudFormation to store your Lambda artifacts when deploying the function. A bucket can easily be created through the AWS CLI, for example: `aws s3 mb s3://my-alexa-skills`.

### Step 1
Open the skill directory in a terminal and execute a Maven build by running `mvn clean package`.

### Step 2
Run the following command, substituting your S3 bucket name:
`aws cloudformation package --template-file ./SAM.yaml --s3-bucket <YOUR_BUCKET_HERE> --output-template-file ./target/packaged-SAM.yaml`

This packages your skill artifacts in preparation for deploying your skill to Lambda through CloudFormation. To do that, run the following command:
`aws cloudformation deploy --template-file ./target/packaged-SAM.yaml --stack-name ColorPickerSkillExample --capabilities CAPABILITY_IAM`

If you get permissions errors while running either of these commands make sure the IAM user being used by the AWS CLI has the right permissions.

Next, you'll need to get the ARN of the Lambda function that CloudFormation created for you. To do this, run `aws lambda list-functions` and look for a function name that begins with *ColorPickerSkillExample*. Copy out the **FunctionArn** value, which we'll be using in the next step.

### Step 3
Open the `skill.json` file in an editor and replace the Lambda ARN placeholder with the ARN you got from the last step. Save the file, and then run the following command in your terminal:

`ask api create-skill --file skill.json`

This creates a new skill with Alexa called **Color Picker** and referencing the Lambda endpoint you provided. Copy the Skill ID from the result of that command and then run:
`ask api update-model -s <YOUR_SKILL_ID> -l en-US -f target/ask/en-US.json`

This uploads your generated interaction model to Alexa and starts the model build process. To check the status of the process, you can run the command specified by the CLI. Once the model build status is **SUCCESS**, run the following command to enable your skill for testing:

`ask api enable-skill --skill-id <YOUR_SKILL_ID> `

### Testing
You can test your skill in one of three ways:
- Through your Alexa device. Using your Alexa app, make sure the new skill is enabled under **My Skills**. Once done, you can invoke your skill using its invocation name ("Alexa, open color picker").
- Through the [ASK Developer Console](https://developer.amazon.com/alexa/console/ask). Open your skill and use the **Test** tab to open the simulator and issue requests against your skill.
- Using the ASK CLI [simulate command](https://developer.amazon.com/docs/smapi/ask-cli-command-reference.html#simulate-command), providing your skill ID and utterance text. For example: `ask simulate -s <YOUR_SKILL_ID> -t "open color picker" -l en-US`

Feel free to start playing around and making changes to the skill at this point. Anytime you want to push your changes, simply rebuild the skill, redeploy to CloudFormation, and update the skill model if model changes were made.