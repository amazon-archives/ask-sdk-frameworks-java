package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.interaction.annotation.data.SlotPropertyReader;
import com.amazon.ask.interaction.annotation.type.BuiltIn;
import com.amazon.ask.interaction.annotation.type.SlotType;
import com.amazon.ask.interaction.mapper.slot.FourDigitNumberParser;

import java.util.Arrays;
import java.util.Objects;

import static com.amazon.ask.util.ValidationUtils.assertNotNull;

/**
 * Provides recognition for special ways four-digit numbers are often spoken. This is useful for input such as PIN
 * codes, validation codes or years that are often said as single or groups of digits. The user can speak the number in
 * a variety of ways. The Alexa service sends your service the recognized digits.
 *
 * @see <a href="https://developer.amazon.com/docs/custom-skills/slot-type-reference.html#four_digit_number">AMAZON.FOUR_DIGIT_NUMBER docs</a>
 */
@BuiltIn
@SlotType(value = "AMAZON.FOUR_DIGIT_NUMBER")
@SlotPropertyReader(FourDigitNumberParser.class)
public class FourDigitNumber extends BaseSlotValue {
    private final int number;
    private final int[] digits;

    public FourDigitNumber(com.amazon.ask.model.Slot slot, int first, int second, int third, int fourth) {
        this.setSlot(assertNotNull(slot, "slot"));

        verifyDigit(first);
        verifyDigit(second);
        verifyDigit(third);
        verifyDigit(fourth);
        this.number =
            1000 * first +
            100 * second +
            10 * third +
            fourth;
        this.digits = new int[]{first, second, third, fourth};
    }

    private static void verifyDigit(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException(String.format("digit '%d' must be between 0 and 9", digit));
        }
    }

    public int[] getDigits() {
        return Arrays.copyOf(digits, 4);
    }

    public int getFirstDigit() {
        return digits[0];
    }

    public int getSecondDigit() {
        return digits[1];
    }

    public int getThirdDigit() {
        return digits[2];
    }

    public int getFourthDigit() {
        return digits[3];
    }

    public int getNumber() {
        return number;
    }

    public String stringValue() {
        return String.format("%04d", getNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        FourDigitNumber that = (FourDigitNumber) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, Arrays.hashCode(digits));
    }

    @Override
    public String toString() {
        return "FourDigitNumber{" +
            "number=" + number +
            ", digits=" + Arrays.toString(digits) +
            ", slot=" + getSlot() +
            '}';
    }
}
