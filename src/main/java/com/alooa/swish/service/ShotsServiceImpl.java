package com.alooa.swish.service;
import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.entity.Shots;
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
    public List<Shots> findAll() {
        return shotsRepository.findAll();
    }

    @Override
    public Shots findById(Long id) {
        return shotsRepository.findById(id).orElse(null);
    }
    @Override
    public Shots createShots(CreateShotsRequest request) {
        Shots shots = shotsMapper.toEntity(request);
        return shotsRepository.save(shots);
    }
    @Override
    public void deleteById(Long id) {
        shotsRepository.deleteById(id);
    }
//    @Override
//    public Shots updateShots(Long id, UpdateShotsRequest request) {
//        Shots shots = shotsMapper.toEntity(request);
//        return shotsRepository.save(shots);
//    }
}
