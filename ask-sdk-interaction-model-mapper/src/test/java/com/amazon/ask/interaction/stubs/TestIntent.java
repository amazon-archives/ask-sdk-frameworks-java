/*
    Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
    except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
    the specific language governing permissions and limitations under the License.
 */

package com.amazon.ask.interaction.stubs;

import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentConfirmationStatus;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.type.Intent;
import com.amazon.ask.interaction.mapper.intent.DialogStateReader;
import com.amazon.ask.interaction.mapper.intent.IntentConfirmationStatusReader;
import com.amazon.ask.interaction.types.slot.AmazonDuration;
import com.amazon.ask.interaction.types.slot.AmazonLiteral;
import com.amazon.ask.interaction.types.slot.AmazonNumber;
import com.amazon.ask.interaction.types.slot.date.AmazonDate;
import com.amazon.ask.interaction.types.slot.list.Actor;
import com.amazon.ask.interaction.types.slot.list.DayOfWeek;
import com.amazon.ask.interaction.types.slot.time.AmazonTime;

/**
 *
 */
@Intent("TestIntent")
public class TestIntent {
    @SlotProperty
    private AmazonLiteral literalSlot;

    @SlotProperty
    private AmazonNumber numberSlot;

    @SlotProperty
    private TestCustom testCustomSlot;

    @SlotProperty
    private TestCustomEnum testCustomEnumSlot;

    @SlotProperty
    private Actor listTypeSlot;

    @SlotProperty
    private AmazonDate dateSlot;

    @SlotProperty
    private AmazonDuration durationSlot;

    @SlotProperty
    private AmazonTime timeSlot;

    @SlotProperty
    private DayOfWeek dayOfWeekSlot;

    @SlotProperty(type = AmazonLiteral.class)
    private Slot slot;

    /** @see DialogStateReader **/
    private DialogState dialogState;

    /** @see IntentConfirmationStatusReader **/
    private IntentConfirmationStatus intentConfirmationStatus;

    public AmazonLiteral getLiteralSlot() {
        return literalSlot;
    }

    public void setLiteralSlot(AmazonLiteral literalSlot) {
        this.literalSlot = literalSlot;
    }

    public AmazonNumber getNumberSlot() {
        return numberSlot;
    }

    public void setNumberSlot(AmazonNumber numberSlot) {
        this.numberSlot = numberSlot;
    }

    public TestCustom getTestCustomSlot() {
        return testCustomSlot;
    }

    public void setTestCustomSlot(TestCustom testCustomSlot) {
        this.testCustomSlot = testCustomSlot;
    }

    public TestCustomEnum getTestCustomEnumSlot() {
        return testCustomEnumSlot;
    }

    public void setTestCustomEnumSlot(TestCustomEnum testCustomEnumSlot) {
        this.testCustomEnumSlot = testCustomEnumSlot;
    }

    public Actor getListTypeSlot() {
        return listTypeSlot;
    }

    public void setListTypeSlot(Actor listTypeSlot) {
        this.listTypeSlot = listTypeSlot;
    }

    public AmazonDate getDateSlot() {
        return dateSlot;
    }

    public void setDateSlot(AmazonDate dateSlot) {
        this.dateSlot = dateSlot;
    }

    public AmazonDuration getDurationSlot() {
        return durationSlot;
    }

    public void setDurationSlot(AmazonDuration durationSlot) {
        this.durationSlot = durationSlot;
    }

    public AmazonTime getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(AmazonTime timeSlot) {
        this.timeSlot = timeSlot;
    }

    public DayOfWeek getDayOfWeekSlot() {
        return dayOfWeekSlot;
    }

    public void setDayOfWeekSlot(DayOfWeek dayOfWeekSlot) {
        this.dayOfWeekSlot = dayOfWeekSlot;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public DialogState getDialogState() {
        return dialogState;
    }

    public void setDialogState(DialogState dialogState) {
        this.dialogState = dialogState;
    }

    public IntentConfirmationStatus getIntentConfirmationStatus() {
        return intentConfirmationStatus;
    }

    public void setIntentConfirmationStatus(IntentConfirmationStatus intentConfirmationStatus) {
        this.intentConfirmationStatus = intentConfirmationStatus;
    }

    @Override
    public String toString() {
        return "TestIntent{" +
            "literalSlot=" + literalSlot +
            ", numberSlot=" + numberSlot +
            ", testCustomSlot=" + testCustomSlot +
            ", testCustomEnumSlot=" + testCustomEnumSlot +
            ", listTypeSlot=" + listTypeSlot +
            ", dateSlot=" + dateSlot +
            ", durationSlot=" + durationSlot +
            ", timeSlot=" + timeSlot +
            ", dayOfWeekSlot=" + dayOfWeekSlot +
            ", slot=" + slot +
            ", dialogState=" + dialogState +
            ", intentConfirmationStatus=" + intentConfirmationStatus +
            '}';
    }
}
