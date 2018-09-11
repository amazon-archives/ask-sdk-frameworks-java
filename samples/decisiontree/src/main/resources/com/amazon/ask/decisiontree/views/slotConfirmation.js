'use strict';

function render(model) {
    return {
        outputSpeech: {
            type: "PlainText",
            text: model.prompt
        },
        directives: [
            {
                type: "Dialog.ElicitSlot",
                slotToElicit: model.slotToElicit
            }
        ],
        reprompt: {
            outputSpeech: {
                type: "PlainText",
                text: model.prompt
            }
        },
        shouldEndSession: false
    };
}
