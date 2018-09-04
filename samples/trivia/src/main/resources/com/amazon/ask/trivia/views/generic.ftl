{
    "outputSpeech": {
        "type": "PlainText",
        "text": "${speech}"
    }
<#if reprompt??>
    ,"reprompt": {
        "outputSpeech": {
            "type": "PlainText",
            "text": "${reprompt}"
        }
    },
    "shouldEndSession": false
  <#if card_title??>
    ,"card": {
        "type": "Simple",
        "title": "${card_title}",
        "content": "${reprompt}"
    }
  </#if>
</#if>
}