package com.alooa.swish.mapper;

import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.entity.Shots;

public class ShotsMapper {
    public Shots toEntity(CreateShotsRequest request) {
        return new Shots(
                request.category(), // errors
                request.location(), // errors
                request.shotsTaken(),
                request.shotsMade(),
                request.notes(),
                request.timestamp()
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
                shots.getNotes(),
                shots.getTimestamp()
        );
    }
}
