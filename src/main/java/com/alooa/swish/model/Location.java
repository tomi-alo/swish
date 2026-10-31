package com.alooa.swish.model;

public enum Location {
    LEFT_CORNER,
    RIGHT_CORNER,
    LEFT_WING,
    RIGHT_WING,
    TOP_OF_KEY,
    PAINT,
    BASELINE,
    FREE_THROW_LINE;

    public static Location fromString(String location) {
        if (location == null) {
            throw new IllegalArgumentException("Invalid, location cannot be null");
        }

        String trimmedLocation = location.trim();
        if (trimmedLocation.isEmpty()) {
            throw new IllegalArgumentException("Invalid, location cannot be empty");
        }

        for (Location l : Location.values()) {
            if (l.name().equalsIgnoreCase(trimmedLocation)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Invalid location: " + location);
    }
}
