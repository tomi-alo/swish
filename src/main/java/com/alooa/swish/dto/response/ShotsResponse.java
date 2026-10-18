package com.alooa.swish.dto.response;

import com.alooa.swish.entity.Category;
import com.alooa.swish.entity.Location;

import java.time.Instant;

public record ShotsResponse(
        Long id,
        Category category,
        Location location,
        int shotsTaken,
        int shotsMade,
        double shootingPercentage,
        String notes,
        Instant timestamp
) {
}
