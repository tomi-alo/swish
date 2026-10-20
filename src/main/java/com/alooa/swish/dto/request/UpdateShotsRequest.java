package com.alooa.swish.dto.request;

import com.alooa.swish.entity.Category;
import com.alooa.swish.entity.Location;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

public record UpdateShotsRequest(
        @NotNull(message = "Shot category cannot be null")
        Category category,

        @NotNull(message = "Shot location cannot be null")
        Location location,

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
