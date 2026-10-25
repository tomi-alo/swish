package com.alooa.swish.dto.response;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;

import java.time.Instant;

public record ShotsResponse(
        Long id,
        Category category,
        Location location,
        int shotsTaken,
        int shotsMade,
        double shootingPercentage,
        String assessment,
        String notes,
        Instant timestamp
) {
}
