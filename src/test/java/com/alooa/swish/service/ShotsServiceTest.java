package com.alooa.swish.service;

import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.exception.InvalidShotsException;
import com.alooa.swish.exception.ShotsNotFoundException;
import com.alooa.swish.mapper.ShotsMapper;
import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.model.Shots;
import com.alooa.swish.repository.ShotsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShotsServiceTest {

    private final Shots shots1 = new Shots(Category.MIDDY, Location.LEFT_WING, 10, 7, "No notes", null);
    private final Shots shots2 = new Shots(Category.LAYUP, Location.RIGHT_WING, 15, 15, "No notes", null);

    @Mock
    private ShotsRepository shotsRepository;
    private ShotsServiceImpl shotsService;

    @BeforeEach
    void setUp() {
        shotsService = new ShotsServiceImpl(shotsRepository, new ShotsMapper());
    }

    @Test
    void serviceShouldFindShots() {
        when(shotsRepository.findById(1L)).thenReturn(Optional.of(shots1));
        ShotsResponse foundShot = shotsService.findById(1L);
        assertEquals(shots1.getCategory(), foundShot.category());
        assertEquals(shots1.getLocation(), foundShot.location());
        assertEquals(shots1.getShotsMade(), foundShot.shotsMade());
        assertEquals(shots1.getShotsTaken(), foundShot.shotsTaken());
        assertEquals(shots1.getNotes(), foundShot.notes());

    }

    @Test
    void serviceShouldNotFindShots() {
        when(shotsRepository.findById(3L)).thenReturn(Optional.empty());
        ShotsNotFoundException ex = assertThrows(ShotsNotFoundException.class, () -> shotsService.findById(3L));
        assertEquals("Shots with id 3 not found", ex.getMessage());
    }

    @Test
    void serviceShouldDeleteShots() {
        shotsService.deleteById(5L);
        verify(shotsRepository).deleteById(5L);
    }

    @Test
    void serviceShouldCreateShots(){
        CreateShotsRequest request = new CreateShotsRequest("MIDDY", "RIGHT_WING", 10, 7, "No notes", null);
        when(shotsRepository.save(any(Shots.class))).thenReturn(shots1);
        ShotsResponse response = shotsService.createShots(request);
        assertEquals(shots1.getCategory(), response.category());
        assertEquals(shots1.getLocation(), response.location());
        assertEquals(shots1.getShotsMade(), response.shotsMade());
        assertEquals(shots1.getShotsTaken(), response.shotsTaken());
        assertEquals(shots1.getNotes(), response.notes());
    }

    @Test
    void serviceShouldNotCreateInvalidShots() {
        CreateShotsRequest request = new CreateShotsRequest("MIDDY", "RIGHT_WING", 10, 15, "No notes", null);
        InvalidShotsException ex = assertThrows(InvalidShotsException.class, () -> shotsService.createShots(request));
        assertEquals("Shots made cannot be greater than shots taken", ex.getMessage());
        verify(shotsRepository, never()).save(any(Shots.class));
    }

    @Test
    void serviceShouldFindAllShots() {
        when(shotsRepository.findAll()).thenReturn(List.of(shots1, shots2));
        var responseList = shotsService.findAll();
        assertEquals(2, responseList.size());
        assertEquals(shots1.getCategory(), responseList.get(0).category());
        assertEquals(shots2.getCategory(), responseList.get(1).category());
    }

    @Test
    void serviceShouldUpdateShots() {
        // Arrange: shots1 is MIDDY, LEFT_WING, 10 taken, 7 made
        when(shotsRepository.findById(1L)).thenReturn(Optional.of(shots1));
        when(shotsRepository.save(any(Shots.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UpdateShotsRequest request = new UpdateShotsRequest("DUNK", "RIGHT_WING", null, null, null, null);

        // Act
        ShotsResponse response = shotsService.updateShots(1L, request);

        // Assert: fields in the request changed
        assertEquals(Category.DUNK, response.category());
        assertEquals(Location.RIGHT_WING, response.location());
        // Assert: null fields were left alone
        assertEquals(10, response.shotsTaken());
        assertEquals(7, response.shotsMade());
    }

    @Test
    void serviceShouldNotUpdateNonexistentShots() {
        when(shotsRepository.findById(3L)).thenReturn(Optional.empty());
        UpdateShotsRequest request = new UpdateShotsRequest("DUNK", "RIGHT_WING", null, null, null, null);
        ShotsNotFoundException ex = assertThrows(ShotsNotFoundException.class, () -> shotsService.updateShots(3L, request));
        assertEquals("Shots with id 3 not found", ex.getMessage());
        verify(shotsRepository, never()).save(any(Shots.class));
    }

    @Test
    void serviceShouldNotUpdateInvalidShots() {
        // Arrange: shots1 is MIDDY, LEFT_WING, 10 taken, 7 made
        when(shotsRepository.findById(1L)).thenReturn(Optional.of(shots1));
        UpdateShotsRequest request = new UpdateShotsRequest(null, null, 5, 10, null, null);

        // Act & Assert
        InvalidShotsException ex = assertThrows(InvalidShotsException.class, () -> shotsService.updateShots(1L, request));
        assertEquals("Shots made cannot be greater than shots taken", ex.getMessage());
        verify(shotsRepository, never()).save(any(Shots.class));
    }
}
