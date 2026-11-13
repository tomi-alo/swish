package com.alooa.swish.dto.request;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.Instant;

public record CreateShotsRequest(
        @Schema(example = "THREE",
                allowableValues = {"MIDDY", "LAYUP", "DUNK", "THREE", "FREE_THROW"})
        @NotBlank(message = "Shot category cannot be null")
        @ValueOfEnum(enumClass = Category.class, message = "Invalid shot category")
        String category,

        @Schema(example = "LEFT_CORNER",
                allowableValues = {"LEFT_CORNER", "RIGHT_CORNER", "LEFT_WING", "RIGHT_WING",
                        "TOP_OF_KEY", "PAINT", "BASELINE", "FREE_THROW_LINE"})
        @NotBlank(message = "Shot location cannot be null")
        @ValueOfEnum(enumClass = Location.class, message = "Invalid shot location")
        String location,

        @Schema(example = "20")
        @NotNull(message = "Shots taken cannot be null")
        @Min(value = 1, message = "Shots taken must be at least 1")
        @Max(value = 1000, message = "Shots taken must be at most 1000")
        Integer shotsTaken,

        @Schema(example = "13")
        @NotNull(message = "Shots made cannot be null")
        @Min(value = 0, message = "Shots made must be at least 0")
        @Max(value = 1000, message = "Shots made must be at most 1000")
        Integer shotsMade,

        @Schema(example = "Corner threes after practice, felt good")
        @Size(max = 255, message = "Notes must be at most 255 characters")
        String notes,

        @Schema(example = "2026-11-13T18:30:00Z")
        Instant timestamp
) {
}
