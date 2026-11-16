package com.alooa.swish.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationTest {
    @Test
    void fromStringShouldWorkWithValidInputs() {
        assertEquals(Location.LEFT_WING, Location.fromString("LEFT_WING"));
        assertEquals(Location.RIGHT_WING, Location.fromString("right_wing  "));
        assertEquals(Location.LEFT_CORNER, Location.fromString("    left_corner"));
    }

    @Test
    void fromStringShouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString(null);
        });
    }

    @Test
    void fromStringShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("");
        });
    }

    @Test
    void fromStringShouldThrowExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Location.fromString("banana");
        });
    }
}
