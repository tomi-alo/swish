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
        @Pattern(regexp = "^(1000|[1-9]\\d{0,2}|0)$\n", message = "Shots taken must be between 0 and 1000")
        int shotsTaken,

        @NotNull(message = "Shots made cannot be null")
        @Pattern(regexp = "^(1000|[1-9]\\d{0,2}|0)$\n", message = "Shots made must be between 0 and 1000")
        int shotsMade,

        String notes,
        Instant timestamp
) {
}
