'use strict';

function render(model) {
    var intent = model.currentIntent;
    var toleranceVerb = intent.bloodTolerance.slot.value === "high" ? "can" : "can't";

    var summary =  "So money is " + model.salary + ", "
                    + "You are an " + intent.personality.slot.value + ", "
                    + "you like " + intent.preferredSpecies.slot.value + ", "
                    + "and you " + toleranceVerb + " tolerate blood. "
                    + "You should consider being a " + model.recommendation + ". "
                    + "Thank you for using Decision Tree. See you next time. Bye.";

    var response = {
        outputSpeech: {
            type: 'PlainText',
            text: summary
        }
    };

    return response;
}
