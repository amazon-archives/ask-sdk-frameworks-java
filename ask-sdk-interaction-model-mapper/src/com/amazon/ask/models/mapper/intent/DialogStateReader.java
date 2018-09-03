package com.amazon.ask.models.mapper.intent;

import com.amazon.ask.model.DialogState;

/**
 *
 */
public class DialogStateReader implements IntentPropertyReader<DialogState> {
    @Override
    public DialogState read(IntentPropertyContext context) {
        return context.getIntentRequest().getDialogState();
    }
}
