#! /usr/bin/sh

# TODO replace me with your skill id
SKILL_ID=TODO

if [ $SKILL_ID = "TODO" ]; then
  echo "you must provide a skill id" >&2
  exit 1
fi

set -e

mvn compile

ask api update-model \
  -s $SKILL_ID \
  -l en-US \
  -f target/ask/en-US.json
