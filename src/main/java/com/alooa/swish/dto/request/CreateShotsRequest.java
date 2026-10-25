package com.alooa.swish.dto.request;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.validation.ValueOfEnum;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record CreateShotsRequest(
        @NotBlank(message = "Shot category cannot be null")
        @ValueOfEnum(enumClass = Category.class, message = "Invalid shot category")
        String category,

        @NotBlank(message = "Shot location cannot be null")
        @ValueOfEnum(enumClass = Location.class, message = "Invalid shot location")
        String location,

        @NotNull(message = "Shots taken cannot be null")
        @Min(value = 1, message = "Shots taken must be at least 1")
        @Max(value = 1000, message = "Shots taken must be at most 1000")
        Integer shotsTaken,

        @NotNull(message = "Shots made cannot be null")
        @Min(value = 0, message = "Shots made must be at least 0")
        @Max(value = 1000, message = "Shots made must be at most 1000")
        Integer shotsMade,

        @Size(max = 255, message = "Notes must be at most 255 characters")
        String notes,
        Instant timestamp
) {
}
