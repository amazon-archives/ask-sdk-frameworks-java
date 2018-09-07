function render(model) {
    var intent = model.intent;

    return {
        outputSpeech: {
            type: "PlainText",
            value: "Thanks! Your pet type was " + intent.petType.slot.value
        }
    }
}