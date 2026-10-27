package com.alooa.swish.controller;

import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.service.ShotsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("swish/api/v1/shots")
public class ShotsController {
    private final ShotsService shotsService;

    public ShotsController(ShotsService shotsService) {
        this.shotsService = shotsService;
    }

    @GetMapping
    public ResponseEntity<List<ShotsResponse>> getAllShots() {
        List<ShotsResponse> shots = shotsService.findAll();
        return ResponseEntity.ok(shots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShotsResponse> getShotById(Long id) {
        ShotsResponse shot = shotsService.findById(id);
        return ResponseEntity.ok(shot);
    }

    @PostMapping
    public ResponseEntity<ShotsResponse> createShot(@Valid @RequestBody CreateShotsRequest request) {
        ShotsResponse createdShot = shotsService.createShots(request);
        return ResponseEntity.ok(createdShot);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShotsResponse> updateShot(@PathVariable Long id, @Valid @RequestBody UpdateShotsRequest request) {
        ShotsResponse updatedShot = shotsService.updateShots(id, request);
        return ResponseEntity.ok(updatedShot);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShot(@PathVariable Long id) {
        shotsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
