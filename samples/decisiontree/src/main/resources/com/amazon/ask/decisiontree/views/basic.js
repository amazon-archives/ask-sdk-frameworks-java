'use strict';

function render(model) {
    var response = {
        outputSpeech: {
            type: 'PlainText',
            text: model.speech
        }
    };

    if (model.reprompt) {
        response.reprompt = {
            outputSpeech: {
                type: 'PlainText',
                text: model.reprompt
            }
        };
        response.shouldEndSession = false;
    }

    return response;
}