<#assign repromptText="Please tell me your favorite color by saying, my favorite color is red">
<#if isWelcomeMessage>
  <#assign speechText="Welcome to the Alexa Skills Kit sample. Please tell me your favorite color by saying, my favorite color is red">
<#else>
  <#assign speechText="Please tell me your favorite color by saying, my favorite color is red">
</#if>
{
    "outputSpeech": {
        "type": "PlainText",
        "text": "${speechText}"
    },
    "shouldEndSession": false,
    "reprompt": {
        "outputSpeech": {
            "type": "PlainText",
            "text": "${repromptText}"
        }
    },
    "card": {
        "type": "Simple",
        "title": "ColorSession",
        "content": "${speechText}"
    }
}