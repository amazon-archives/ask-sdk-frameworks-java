var text = "hello";

var renderer = {
    render: function(model) {
        return {
            outputSpeech: {
                type: 'PlainText',
                text: text + " " + model.attribute
            }
        };
    }
};
