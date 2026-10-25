package com.alooa.swish.model;
import static org.junit.jupiter.api.Assertions.*;
import com.alooa.swish.exception.InvalidShotsException;
import org.junit.jupiter.api.Test;

class ShotsModelTest {

    private final Shots validShots = new Shots(Category.MIDDY, Location.LEFT_WING, 10, 7, "No notes", null);
    private final Shots perfectShots = new Shots(Category.LAYUP, Location.RIGHT_WING, 15, 15, "No notes", null);
    private final Shots madeGreaterThanTaken = new Shots(Category.LAYUP, Location.RIGHT_WING, 5, 12, null, null);
    private final Shots negativeShotsTaken = new Shots(Category.THREE, Location.TOP_OF_KEY, -5, 3, null, null);
    private final Shots negativeShotsMade = new Shots(Category.MIDDY, Location.LEFT_WING, 10, -2, null, null);
    private final Shots zeroShotsTaken = new Shots(Category.LAYUP, Location.RIGHT_WING, 0, 0, null, null);

    @Test
    void ModelShouldCreateValidShotsObject() {
        assertEquals(Category.MIDDY, validShots.getCategory());
        assertEquals(Location.LEFT_WING, validShots.getLocation());
        assertEquals(10, validShots.getShotsTaken());
        assertEquals(7, validShots.getShotsMade());
        assertEquals(70.0, validShots.getShootingPercentage());

    }

    @Test
    void ModelShouldNotThrowExceptionForValidShots() {
        // Test case for creating a valid Shots object
        assertDoesNotThrow(() -> {
            validShots.checkIfValid();
        });
    }

    @Test
    void ModelShouldNotThrowExceptionForPerfectShots() {
        // Test case for creating a valid Shots object with perfect shooting percentage
        assertDoesNotThrow(() -> {
            perfectShots.checkIfValid();
        });
    }

    @Test
    void ModelShouldThrowExceptionZeroShotsTaken() {
        // Test case for creating an invalid Shots object with zero shots taken
        assertThrows(InvalidShotsException.class, () -> {
            zeroShotsTaken.checkIfValid();
        });
    }

    @Test
    void ModelShouldThrowExceptionNegativeShotsTaken() {
        // Test case for creating an invalid Shots object with negative shots taken
        assertThrows(InvalidShotsException.class, () -> {
            negativeShotsTaken.checkIfValid();
        });
    }

    @Test
    void ModelShouldThrowExceptionNegativeShotsMade() {
        // Test case for creating an invalid Shots object with negative shots made
        assertThrows(InvalidShotsException.class, () -> {
            negativeShotsMade.checkIfValid();
        });
    }

    @Test
    void ModelShouldThrowExceptionShotsMadeGreaterThanShotsTaken() {
        // Test case for creating an invalid Shots object with shots made greater than shots taken
        assertThrows(InvalidShotsException.class, () -> {
            madeGreaterThanTaken.checkIfValid();
        });
    }

    @Test
    void ModelShouldUpdateValidShotsObjectCorrectly() {
        // Test case for updating a Shots object with valid values
        validShots.setShotsTaken(15);
        validShots.setShotsMade(10);
        assertEquals(15, validShots.getShotsTaken());
        assertEquals(10, validShots.getShotsMade());
        assertEquals(66.66, validShots.getShootingPercentage(), 0.01);
    }

    @Test
    void ModelShouldThrowExceptionWhenUpdatingShotsToInvalidValues() {
        // Test case for updating a Shots object with shots made greater than shots taken
        assertThrows(InvalidShotsException.class, () -> {
            validShots.setShotsTaken(5);
            validShots.setShotsMade(10);
            validShots.checkIfValid();
        });
    }
}