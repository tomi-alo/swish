package com.alooa.swish.dto.request;

import com.alooa.swish.entity.Category;
import com.alooa.swish.entity.Location;
import com.alooa.validation.ValueOfEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record UpdateShotsRequest(
        @NotNull(message = "Shot category cannot be null")
        @ValueOfEnum(enumClass = Category.class, message = "Invalid shot category")
        String category,

        @NotNull(message = "Shot location cannot be null")
        @ValueOfEnum(enumClass = Location.class, message = "Invalid shot location")
        String location,

        @NotNull(message = "Shots taken cannot be null")
        @Positive(message = "Shots taken must be a positive number")
        int shotsTaken,

        @NotNull(message = "Shots made cannot be null")
        @Positive(message = "Shots made must be a positive number")
        int shotsMade,

        String notes,
        Instant timestamp
) {
}
