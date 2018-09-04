var text = "hello";

// entry point taking model attributes
function render(model) {
    return {
        outputSpeech: {
            type: 'PlainText',
            text: text + " " + model.attribute
        }
    };
}