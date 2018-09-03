package com.amazon.ask.models.types.slot;

import com.amazon.ask.models.annotation.type.BuiltIn;
import com.amazon.ask.models.annotation.type.SlotType;

/**
 * @link https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#amazonsearchquery
 */
@BuiltIn
@SlotType("AMAZON.SearchQuery")
public class SearchQuery extends BaseSlotValue {
}
