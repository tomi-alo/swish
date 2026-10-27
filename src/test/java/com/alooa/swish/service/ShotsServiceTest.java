package com.alooa.swish.service;

import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.model.Shots;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class ShotsServiceTest {

    private final Shots shots1 = new Shots(Category.MIDDY, Location.LEFT_WING, 10, 7, "No notes", null);
    private final Shots shots2 = new Shots(Category.LAYUP, Location.RIGHT_WING, 15, 15, "No notes", null);

    @Test
    void ServiceShouldGetAllShots() {
        // Test case for getting all shots
        // Implement the test logic here


    }

}
