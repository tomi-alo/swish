package com.alooa.swish.service;

import com.alooa.swish.entity.Shots;
import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import java.util.List;

public interface ShotsService {
    // list all shot logs
    public List<Shots> findAll();
    // find a shot log by its ID
    public Shots findById(Long id);
    // create a new shot log
    public Shots createShots(CreateShotsRequest request);
    // delete a shot log by its ID
    public void deleteById(Long id);
    // update a shot log by its ID
//    public Shots updateShots(Long id, UpdateShotsRequest request);
}
