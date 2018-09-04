package com.amazon.ask.interaction.mapper.slot;

import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.interaction.mapper.SlotValueParseException;
import com.amazon.ask.interaction.types.slot.date.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#date">AMAZON.DATE docs</a>
 */
public class AmazonDateParser implements SlotPropertyReader<AmazonDate> {
    private static final String PRESENT_REF = "PRESENT_REF";
    private static final Pattern SPECIFIC_PATTERN = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    private static final Pattern WEEK_PATTERN = Pattern.compile("^([0-9]{4})-W([0-4][0-9]|5[0-2]+)$");
    private static final Pattern WEEKEND_PATTERN = Pattern.compile("^([0-9]{4})-W([0-4][0-9]|5[0-2]+)-WE$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^([0-9]{4})-(0[0-9]|1[0-2])$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^([0-9]{4})$");
    private static final Pattern DECADE_PATTERN = Pattern.compile("^([0-9]{2})([0-9])X$");
    private static final Pattern SEASON_PATTERN = Pattern.compile("^([0-9]{4})-(WI|SP|SU|FA)$");

    @Override
    public AmazonDate read(IntentRequest intentRequest, Slot slot) throws SlotValueParseException {
        AmazonDate parsed = null;
        try {
            if (slot.getValue().equals(PRESENT_REF)) {
                parsed = new PresentRef(slot, LocalDateTime.now());
            }
            if (parsed == null) {
                parsed = tryParse(slot, SPECIFIC_PATTERN, matcher -> new SpecificDate(slot, LocalDate.parse(matcher.group())));
            }
            if (parsed == null) {
                parsed = tryParse(slot, WEEK_PATTERN, matcher -> new WeekDate(slot, getInt(matcher, 1), getInt(matcher, 2)));
            }
            if (parsed == null) {
                parsed = tryParse(slot, WEEKEND_PATTERN, matcher -> new WeekendDate(slot, getInt(matcher, 1), getInt(matcher, 2)));
            }
            if (parsed == null) {
                parsed = tryParse(slot, MONTH_PATTERN, matcher -> new MonthDate(slot, getInt(matcher, 1), getInt(matcher, 2)));
            }
            if (parsed == null) {
                parsed = tryParse(slot, YEAR_PATTERN, matcher -> new YearDate(slot, getInt(matcher, 1)));
            }
            if (parsed == null) {
                parsed = tryParse(slot, DECADE_PATTERN, matcher -> new DecadeDate(slot, getInt(matcher, 1), getInt(matcher, 2)));
            }
            if (parsed == null) {
                parsed = tryParse(slot, SEASON_PATTERN, matcher -> new SeasonDate(slot, getInt(matcher, 1), Season.valueOf(matcher.group(2))));
            }
        } catch (DateTimeParseException ex) {
            throw new SlotValueParseException(slot, AmazonDate.class, ex);
        }

        if (parsed == null) {
            throw new SlotValueParseException(slot, AmazonDate.class);
        }

        return parsed;
    }

    private static int getInt(Matcher matcher, int index) {
        return Integer.valueOf(matcher.group(index));
    }

    private static <T extends AmazonDate> T tryParse(Slot slot, Pattern pattern, Function<Matcher, T> parse) {
        Matcher matcher = pattern.matcher(slot.getValue());
        if (matcher.matches()) {
            return parse.apply(matcher);
        }
        return null;
    }
}
