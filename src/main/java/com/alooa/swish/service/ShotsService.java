package com.alooa.swish.service;

import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.dto.request.CreateShotsRequest;

import java.util.List;

public interface ShotsService {
    // list all shot logs
    public List<ShotsResponse> findAll();
    // find a shot log by its ID
    public ShotsResponse findById(Long id);
    // create a new shot log
    public ShotsResponse createShots(CreateShotsRequest request);
    // delete a shot log by its ID
    public void deleteById(Long id);
    // update a shot log by its ID
   public ShotsResponse updateShots(Long id, UpdateShotsRequest request);
}
