package com.alooa.swish.model;

public enum Category {
    MIDDY,
    LAYUP,
    DUNK,
    THREE,
    FREE_THROW;

    public static Category fromString(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Invalid, category cannot be null");
        }

        String trimmedCategory = category.trim();
        if (trimmedCategory.isEmpty()) {
            throw new IllegalArgumentException("Invalid, category cannot be empty");
        }

        for (Category c : Category.values()) {
            if (c.name().equalsIgnoreCase(trimmedCategory)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + category);
    }
}
