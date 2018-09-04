package com.amazon.ask.interaction.renderer.models;

import com.amazon.ask.interaction.annotation.data.SlotProperty;
import com.amazon.ask.interaction.annotation.data.IntentResource;
import com.amazon.ask.interaction.annotation.type.Intent;
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
@IntentResource("TestIntent")
public class TestIntent {

    @SlotProperty
    private AmazonLiteral literalSlot;

    @SlotProperty
    private AmazonNumber numberSlot;

    @SlotProperty
    private TestCustom customSlot;

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

    public AmazonLiteral getLiteralSlot() { return literalSlot; }
    public void setLiteralSlot(AmazonLiteral literalSlot) { this.literalSlot = literalSlot; }
    public AmazonNumber getNumberSlot() { return numberSlot; }
    public void setNumberSlot(AmazonNumber numberSlot) { this.numberSlot = numberSlot; }
    public TestCustom getCustomSlot() { return customSlot; }
    public void setCustomSlot(TestCustom customSlot) { this.customSlot = customSlot; }
    public Actor getListTypeSlot() { return listTypeSlot; }
    public void setListTypeSlot(Actor listTypeSlot) { this.listTypeSlot = listTypeSlot; }
    public AmazonDate getDateSlot() { return dateSlot; }
    public void setDateSlot(AmazonDate dateSlot) { this.dateSlot = dateSlot; }
    public AmazonDuration getDurationSlot() { return durationSlot; }
    public void setDurationSlot(AmazonDuration durationSlot) { this.durationSlot = durationSlot; }
    public AmazonTime getTimeSlot() { return timeSlot; }
    public void setTimeSlot(AmazonTime timeSlot) { this.timeSlot = timeSlot; }
    public DayOfWeek getDayOfWeekSlot() { return dayOfWeekSlot; }
    public void setDayOfWeekSlot(DayOfWeek dayOfWeekSlot) { this.dayOfWeekSlot = dayOfWeekSlot; }
}
