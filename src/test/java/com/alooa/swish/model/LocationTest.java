package com.alooa.swish.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationTest {
    @Test
    void FromStringShouldWorkWithValidInputs() {
        assertEquals(Location.LEFT_WING, Location.fromString("LEFT_WING"));
        assertEquals(Location.RIGHT_WING, Location.fromString("right_wing  "));
        assertEquals(Location.LEFT_CORNER, Location.fromString("    left_corner"));
    }

    @Test
    void FromStringShouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString(null);
        });
    }

    @Test
    void FromStringShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("");
        });
    }

    @Test
    void FromStringShouldThrowExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("banana");
        });
    }
}
