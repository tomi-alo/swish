package com.alooa.swish.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {
    @Test
    void fromStringShouldWorkWithValidInputs() {
        assertEquals(Category.FREE_THROW, Category.fromString("FREE_THROW"));
        assertEquals(Category.THREE, Category.fromString("three  "));
        assertEquals(Category.FREE_THROW, Category.fromString("    free_throw"));
    }

    @Test
    void fromStringShouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString(null);
        });
    }

    @Test
    void fromStringShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("");
        });
    }

    @Test
    void fromStringShouldThrowExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("banana");
        });
    }
}
