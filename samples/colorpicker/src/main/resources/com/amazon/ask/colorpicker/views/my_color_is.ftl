<#if !isAskResponse>
    <#assign speechText="I now know that your favorite color is ${favoriteColor}. You can ask me your favorite color by saying, what's my favorite color?">
    <#assign repromptText="You can ask me your favorite color by saying, what's my favorite color?">
<#else>
    <#assign speechText="I'm not sure what your favorite color is, please try again.">
    <#assign repromptText="I'm not sure what your favorite color is. You can tell me your favorite color by saying, my color is red">
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