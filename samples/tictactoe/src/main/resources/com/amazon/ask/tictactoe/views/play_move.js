function render(model) {
    var text;
    var end = false;
    if (model.result === "LEGAL") {
        text = "the " + model.square + " is now an " + model.currentPlayer + ". " +
               "it is now player " + model.nextPlayer + "'s turn.";

    } else if (model.result === "ILLEGAL") {
        text = "square " + model.square + " is already taken.";
    } else {
        end = true;
        if (model.result === "WIN") {
            text = "player " + model.currentPlayer + " wins!";
        } else {
            text = "the game was a draw!";
        }
    }

    return {
        outputSpeech: {
            type: "PlainText",
            text: text
        },
        shouldEndSession: end
    };
}
