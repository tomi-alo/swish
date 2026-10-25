package com.alooa.swish.mapper;

import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.model.Shots;

import java.time.Instant;

public class ShotsMapper {
    public Shots toEntity(CreateShotsRequest request) {
        Category category = Category.valueOf(request.category().toUpperCase());
        Location location = Location.valueOf(request.location().toUpperCase());
        Integer shotsMade = request.shotsMade();
        Integer shotsTaken = request.shotsTaken();

        Instant timestamp = request.timestamp() != null ? request.timestamp() : Instant.now();

        return new Shots(
                category,
                location,
                shotsTaken,
                shotsMade,
                request.notes(),
                timestamp
        );
    }

    public ShotsResponse toResponse(Shots shots) {
        return new ShotsResponse(
                shots.getId(),
                shots.getCategory(),
                shots.getLocation(),
                shots.getShotsTaken(),
                shots.getShotsMade(),
                shots.getShootingPercentage(),
                shots.getAssessment(),
                shots.getNotes(),
                shots.getTimestamp()
        );
    }
}
