package com.alooa.swish.controller;

import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.service.ShotsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
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
    public ResponseEntity<ShotsResponse> getShotById(@PathVariable Long id) {
        ShotsResponse shot = shotsService.findById(id);
        return ResponseEntity.ok(shot);
    }

    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ShotsResponse> createShots(@Valid @RequestBody CreateShotsRequest request) {
        ShotsResponse createdShot = shotsService.createShots(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdShot.id())
                .toUri();
        return ResponseEntity.created(location).body(createdShot);
    }

    @SecurityRequirement(name = "basicAuth")
    @PatchMapping("/{id}")
    public ResponseEntity<ShotsResponse> updateShots(@PathVariable Long id, @Valid @RequestBody UpdateShotsRequest request) {
        ShotsResponse updatedShot = shotsService.updateShots(id, request);
        return ResponseEntity.ok(updatedShot);
    }

    @SecurityRequirement(name = "basicAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShots(@PathVariable Long id) {
        shotsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
