package com.alooa.swish.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {
    @Test
    void FromStringShouldWorkWithValidInputs() {
        assertEquals(Category.FREE_THROW, Category.fromString("FREE_THROW"));
        assertEquals(Category.THREE, Category.fromString("three  "));
        assertEquals(Category.FREE_THROW, Category.fromString("    free_throw"));
    }

    @Test
    void FromStringShouldThrowExceptionForNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString(null);
        });
    }

    @Test
    void FromStringShouldThrowExceptionForEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("   ");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("");
        });
    }

    @Test
    void FromStringShouldThrowExceptionForInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            Category.fromString("banana");
        });
    }
}
