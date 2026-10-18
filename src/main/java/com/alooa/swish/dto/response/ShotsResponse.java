package com.alooa.swish.dto.response;

import com.alooa.swish.entity.Category;
import com.alooa.swish.entity.Location;

public record ShotsResponse(
        Long id,
        Category category,
        Location location,
        int shotsTaken,
        int shotsMade,
        double shootingPercentage,
        String notes
) {
}
