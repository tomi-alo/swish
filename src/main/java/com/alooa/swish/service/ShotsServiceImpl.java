package com.alooa.swish.service;
import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.model.Shots;
import com.alooa.swish.mapper.ShotsMapper;
import com.alooa.swish.repository.ShotsRepository;

import java.util.List;

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
        return shotsRepository.findById(id).orElse(null);
    }

    @Override
    public ShotsResponse createShots(CreateShotsRequest request) {
        Shots shots = shotsMapper.toEntity(request);
    }
    @Override
    public void deleteById(Long id) {
        shotsRepository.deleteById(id);
    }

    @Override
    public ShotsResponse updateShots(Long id, UpdateShotsRequest request) {
        Shots shots = shotsRepository.findById(id).orElse(null);
        if (shots == null) {
            return null;
        }
        shotsMapper.updateEntity(request, shots);
        Shots updatedShots = shotsRepository.save(shots);
        return shotsMapper.toResponse(updatedShots);
    }
}
