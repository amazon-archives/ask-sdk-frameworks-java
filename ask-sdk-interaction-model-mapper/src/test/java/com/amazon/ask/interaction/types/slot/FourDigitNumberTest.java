package com.amazon.ask.interaction.types.slot;

import com.amazon.ask.model.Slot;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FourDigitNumberTest {
    private final Slot mockSlot = Slot.builder()
        .withName("test")
        .withValue("test")
        .build();

    @Test(expected = IllegalArgumentException.class)
    public void testNullSlot() {
        new FourDigitNumber(null, 1, 1, 1, 1);
    }

    @Test
    public void testFirstDigit() {
        testDigit(0);
    }
    @Test
    public void testSecondDigit() {
        testDigit(1);
    }
    @Test
    public void testThirdDigit() {
        testDigit(2);
    }
    @Test
    public void testFourthDigit() {
        testDigit(3);
    }

    @Test
    public void testNumber() {
        assertEquals(9999, new FourDigitNumber(mockSlot, 9, 9, 9, 9).getNumber());
        assertEquals(999, new FourDigitNumber(mockSlot, 0, 9, 9, 9).getNumber());
        assertEquals(99, new FourDigitNumber(mockSlot, 0, 0, 9, 9).getNumber());
        assertEquals(9, new FourDigitNumber(mockSlot, 0, 0, 0, 9).getNumber());
        assertEquals(1111, new FourDigitNumber(mockSlot, 1, 1, 1, 1).getNumber());
        assertEquals(111, new FourDigitNumber(mockSlot, 0, 1, 1, 1).getNumber());
        assertEquals(11, new FourDigitNumber(mockSlot, 0, 0, 1, 1).getNumber());
        assertEquals(1, new FourDigitNumber(mockSlot, 0, 0, 0, 1).getNumber());
        assertEquals(0, new FourDigitNumber(mockSlot, 0, 0, 0, 0).getNumber());
    }
    
    @Test
    public void testStringValue() {
        assertEquals("9999", new FourDigitNumber(mockSlot, 9, 9, 9, 9).stringValue());
        assertEquals("0999", new FourDigitNumber(mockSlot, 0, 9, 9, 9).stringValue());
        assertEquals("0099", new FourDigitNumber(mockSlot, 0, 0, 9, 9).stringValue());
        assertEquals("0009", new FourDigitNumber(mockSlot, 0, 0, 0, 9).stringValue());
        assertEquals("1111", new FourDigitNumber(mockSlot, 1, 1, 1, 1).stringValue());
        assertEquals("0111", new FourDigitNumber(mockSlot, 0, 1, 1, 1).stringValue());
        assertEquals("0011", new FourDigitNumber(mockSlot, 0, 0, 1, 1).stringValue());
        assertEquals("0001", new FourDigitNumber(mockSlot, 0, 0, 0, 1).stringValue());
        assertEquals("0000", new FourDigitNumber(mockSlot, 0, 0, 0, 0).stringValue());
    }

    @Test
    public void testEqualsSelf() {
        FourDigitNumber num = new FourDigitNumber(mockSlot, 0, 0, 0, 0);

        assertEquals(num, num);
        assertEquals(num.getNumber(), num.getNumber());
        assertEquals(num.getFirstDigit(), num.getFirstDigit());
        assertEquals(num.getSecondDigit(), num.getSecondDigit());
        assertEquals(num.getThirdDigit(), num.getThirdDigit());
        assertEquals(num.getFourthDigit(), num.getFourthDigit());
        assertArrayEquals(num.getDigits(), num.getDigits());
        assertEquals(num.stringValue(), num.stringValue());
        assertEquals(num.getSlot(), num.getSlot());
        assertEquals(num.hashCode(), num.hashCode());
        assertEquals(num.toString(), num.toString());
    }

    @Test
    public void testEquals() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);

        assertEquals(num1, num2);
        assertEquals(num1.getNumber(), num2.getNumber());
        assertEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayEquals(num1.getDigits(), num2.getDigits());
        assertEquals(num1.stringValue(), num2.stringValue());
        assertEquals(num1.getSlot(), num2.getSlot());
        assertEquals(num1.hashCode(), num2.hashCode());
        assertEquals(num1.toString(), num2.toString());
    }

    @Test
    public void testNotEqualsNull() {
        FourDigitNumber num = new FourDigitNumber(mockSlot, 0, 0, 0, 0);

        assertNotEquals(num, null);
        assertNotEquals(null, num);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        FourDigitNumber num = new FourDigitNumber(mockSlot, 0, 0, 0, 0);

        assertNotEquals(num, "different");
        assertNotEquals("different", num);
    }

    @Test
    public void testNotEqualsDifferentSlot() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(Slot.builder()
            .withName("different")
            .withValue("different")
            .build(), 0, 0, 0, 0);

        assertNotEquals(num1, num2);
        assertEquals(num1.getNumber(), num2.getNumber());
        assertEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayEquals(num1.getDigits(), num2.getDigits());
        assertNotEquals(num1.getSlot(), num2.getSlot());
        assertEquals(num1.stringValue(), num2.stringValue());
        assertNotEquals(num1.hashCode(), num2.hashCode());
        assertNotEquals(num1.toString(), num2.toString());
    }

    @Test
    public void testNotEqualsDifferentFirstDigit() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(mockSlot, 1, 0, 0, 0);

        assertNotEquals(num1, num2);
        assertNotEquals(num1.getNumber(), num2.getNumber());
        assertNotEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayNotEquals(num1.getDigits(), num2.getDigits());
        assertEquals(num1.getSlot(), num2.getSlot());
        assertNotEquals(num1.stringValue(), num2.stringValue());
        assertNotEquals(num1.hashCode(), num2.hashCode());
        assertNotEquals(num1.toString(), num2.toString());
    }

    @Test
    public void testNotEqualsDifferentSecondDigit() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(mockSlot, 0, 1, 0, 0);

        assertNotEquals(num1, num2);
        assertNotEquals(num1.getNumber(), num2.getNumber());
        assertEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertNotEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayNotEquals(num1.getDigits(), num2.getDigits());
        assertEquals(num1.getSlot(), num2.getSlot());
        assertNotEquals(num1.stringValue(), num2.stringValue());
        assertNotEquals(num1.hashCode(), num2.hashCode());
        assertNotEquals(num1.toString(), num2.toString());
    }

    @Test
    public void testNotEqualsDifferentThirdDigit() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(mockSlot, 0, 0, 1, 0);

        assertNotEquals(num1, num2);
        assertNotEquals(num1.getNumber(), num2.getNumber());
        assertEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertNotEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayNotEquals(num1.getDigits(), num2.getDigits());
        assertEquals(num1.getSlot(), num2.getSlot());
        assertNotEquals(num1.stringValue(), num2.stringValue());
        assertNotEquals(num1.hashCode(), num2.hashCode());
        assertNotEquals(num1.toString(), num2.toString());
    }

    @Test
    public void testNotEqualsDifferentFourthDigit() {
        FourDigitNumber num1 = new FourDigitNumber(mockSlot, 0, 0, 0, 0);
        FourDigitNumber num2 = new FourDigitNumber(mockSlot, 0, 0, 0, 1);

        assertNotEquals(num1, num2);
        assertNotEquals(num1.getNumber(), num2.getNumber());
        assertEquals(num1.getFirstDigit(), num2.getFirstDigit());
        assertEquals(num1.getSecondDigit(), num2.getSecondDigit());
        assertEquals(num1.getThirdDigit(), num2.getThirdDigit());
        assertNotEquals(num1.getFourthDigit(), num2.getFourthDigit());
        assertArrayNotEquals(num1.getDigits(), num2.getDigits());
        assertEquals(num1.getSlot(), num2.getSlot());
        assertNotEquals(num1.stringValue(), num2.stringValue());
        assertNotEquals(num1.hashCode(), num2.hashCode());
        assertNotEquals(num1.toString(), num2.toString());
    }

    private FourDigitNumber construct(int position, int i) {
        if (position == 0) return new FourDigitNumber(mockSlot, i, 0, 0, 0);
        else if (position == 1) return new FourDigitNumber(mockSlot, 0, i, 0, 0);
        else if (position == 2) return new FourDigitNumber(mockSlot, 0, 0, i, 0);
        else if (position == 3) return new FourDigitNumber(mockSlot, 0, 0, 0, i);
        fail("invalid position " + position);
        throw new IllegalArgumentException("invalid position " + position);
    }

    private void testDigit(int position) {
        for (int i = 0; i < 10; i++) {
            FourDigitNumber number = construct(position, i);
            if (position == 0) assertEquals(i, number.getFirstDigit());
            else if (position == 1) assertEquals(i, number.getSecondDigit());
            else if (position == 2) assertEquals(i, number.getThirdDigit());
            else if (position == 3) assertEquals(i, number.getFourthDigit());
        }
        try {
            construct(position, -1);
            fail("Should not accept a negative digit");
        } catch (IllegalArgumentException ex) {}
        try {
            construct(position, 10);
            fail("Should not accept a digit > 9");
        } catch (IllegalArgumentException ex) {}
    }

    private static void assertArrayNotEquals(int[] left, int[] right) {
        if (Arrays.equals(left, right)) {
            fail("arrays should not be equal:" + Arrays.toString(left) + ", " + Arrays.toString(right));
        }
    }
}
