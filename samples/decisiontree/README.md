

```bash

mvn package

ask api update-model -s amzn1.ask.skill.5c06c94b-b57b-4fa1-b78e-9e764f1f3f40 -l en-US -f target/ask/en-US.json --profile sample-skills

ask api get-skill-status -s amzn1.ask.skill.5c06c94b-b57b-4fa1-b78e-9e764f1f3f40 --profile sample-skills

aws --profile default cloudformation package --template-file ./SAM.yaml --s3-bucket com.amazon.ask.mvc.examples --output-template-file ./target/packaged-SAM.yaml

aws --profile default cloudformation deploy --template-file ./target/packaged-SAM.yaml --stack-name DecisionTreeSkillExample --capabilities CAPABILITY_IAM

```