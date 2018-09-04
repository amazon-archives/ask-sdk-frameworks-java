package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;

/**
 * @link https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#amazonsearchquery
 */
@BuiltIn
@SlotType("AMAZON.SearchQuery")
public class SearchQuery extends BaseSlotValue {
}
