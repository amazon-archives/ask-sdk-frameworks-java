<#if favoriteColor??>
    <#assign speechText="Your favorite color is ${favoriteColor}. Goodbye.">
<#else>
    <#assign speechText="I'm not sure what your favorite color is. You can say, my favorite color is red.">
</#if>
{
    "outputSpeech": {
        "type": "PlainText",
        "text": "${speechText}"
    },
    "shouldEndSession": true,
    "card": {
        "type": "Simple",
        "title": "ColorSession",
        "content": "${speechText}"
    }
}