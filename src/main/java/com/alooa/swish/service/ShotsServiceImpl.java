package com.alooa.swish.service;
import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.exception.ShotsNotFoundException;
import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.model.Shots;
import com.alooa.swish.mapper.ShotsMapper;
import com.alooa.swish.repository.ShotsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShotsServiceImpl implements ShotsService {

    private final ShotsRepository shotsRepository;
    private final ShotsMapper shotsMapper;
    public ShotsServiceImpl(ShotsRepository shotsRepository, ShotsMapper shotsMapper) {
        this.shotsRepository = shotsRepository;
        this.shotsMapper = shotsMapper;
    }
    @Override
    public List<ShotsResponse> findAll() {
        return shotsRepository.findAll().stream().map(shotsMapper::toResponse).toList();
    }

    @Override
    public ShotsResponse findById(Long id) {
        return shotsRepository.findById(id).map(shotsMapper::toResponse).orElseThrow(() -> new ShotsNotFoundException(id));
    }

    @Override
    public ShotsResponse createShots(CreateShotsRequest request) {
        Shots shots = shotsMapper.toEntity(request);
        shots.checkIfValid();
        Shots savedShots = shotsRepository.save(shots);
        return shotsMapper.toResponse(savedShots);
    }
    @Override
    public void deleteById(Long id) {
        shotsRepository.deleteById(id);
    }

    @Override
    public ShotsResponse updateShots(Long id, UpdateShotsRequest request) {
        Shots shots = shotsRepository.findById(id).orElseThrow(() -> new ShotsNotFoundException(id));

        if (request.category() != null) {
            shots.setCategory(Category.valueOf(request.category()));
        }
        if (request.location() != null) {
            shots.setLocation(Location.valueOf(request.location()));
        }
        if (request.shotsTaken() != null) {
            shots.setShotsTaken(request.shotsTaken());
        }
        if (request.shotsMade() != null) {
            shots.setShotsMade(request.shotsMade());
        }
        if (request.notes() != null) {
            shots.setNotes(request.notes());
        }
        if (request.timestamp() != null) {
            shots.setTimestamp(request.timestamp());
        }

        shots.checkIfValid();
        Shots updatedShots = shotsRepository.save(shots);
        return shotsMapper.toResponse(updatedShots);
    }
}