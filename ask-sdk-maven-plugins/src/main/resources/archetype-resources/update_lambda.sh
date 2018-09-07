#! /usr/bin/sh

# TODO replace me with your s3 bucket name
BUCKET_NAME=TODO

if [ $BUCKET_NAME = "TODO" ]; then
  echo "you must provide an s3 bucket to host lambda jar" >&2
  exit 1
fi

set -e

mvn package

aws cloudformation package \
  --template-file ./SAM.yaml \
  --s3-bucket $BUCKET_NAME \
  --output-template-file ./target/packaged-SAM.yaml

aws cloudformation deploy \
  --template-file ./target/packaged-SAM.yaml \
  --stack-name ${skillName}Stack \
  --capabilities CAPABILITY_IAM
