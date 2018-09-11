'use strict';

function render(model) {
    return {
        directives: [
            {
                type: "Dialog.Delegate",
                updatedIntent: model.currentIntent
            }
        ]
    };
}