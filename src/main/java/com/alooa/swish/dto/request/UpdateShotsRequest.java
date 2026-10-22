package com.alooa.swish.dto.request;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.validation.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateShotsRequest(
        @ValueOfEnum(enumClass = Category.class, message = "Invalid shot category")
        String category,

        @ValueOfEnum(enumClass = Location.class, message = "Invalid shot location")
        String location,

        @Pattern(regexp = "^(1000|[1-9]\\d{0,2}|0)$\n", message = "Shots taken must be between 0 and 1000")
        int shotsTaken,

        @Pattern(regexp = "^(1000|[1-9]\\d{0,2}|0)$\n", message = "Shots made must be between 0 and 1000")
        int shotsMade,

        @Size(max = 255, message = "Notes must be at most 255 characters")
        String notes,
        Instant timestamp
) {
}
