package com.alooa.swish.dto.response;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;

import java.math.BigDecimal;
import java.time.Instant;

public record ShotsResponse(
        Long id,
        Category category,
        Location location,
        int shotsTaken,
        int shotsMade,
        BigDecimal shootingPercentage,
        String assessment,
        String notes,
        Instant timestamp
) {
}
