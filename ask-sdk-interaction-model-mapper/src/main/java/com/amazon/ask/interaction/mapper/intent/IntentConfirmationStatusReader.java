package com.amazon.ask.interaction.mapper.intent;

import com.amazon.ask.model.IntentConfirmationStatus;

/**
 *
 */
public class IntentConfirmationStatusReader implements IntentPropertyReader<IntentConfirmationStatus> {
    @Override
    public IntentConfirmationStatus read(IntentPropertyContext context) {
        return context.getIntentRequest().getIntent().getConfirmationStatus();
    }
}
