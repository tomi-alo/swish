package com.alooa.swish.controller;

import com.alooa.swish.config.SecurityConfig;
import com.alooa.swish.dto.request.CreateShotsRequest;
import com.alooa.swish.dto.request.UpdateShotsRequest;
import com.alooa.swish.dto.response.ShotsResponse;
import com.alooa.swish.exception.InvalidShotsException;
import com.alooa.swish.exception.ShotsNotFoundException;
import com.alooa.swish.model.Category;
import com.alooa.swish.model.Location;
import com.alooa.swish.service.ShotsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Starts only the web layer (this controller + GlobalExceptionHandler + security).
// The service is a fake, so no real service, repository or database runs.
@WebMvcTest(controllers = ShotsController.class, properties = {
        "swish.security.username=test",
        "swish.security.password=secret"
})
@Import(SecurityConfig.class)
class ShotsControllerTest {

    private static final String BASE_URL = "/swish/api/v1/shots";

    // A valid request body, written as raw JSON like Postman would send it.
    private static final String VALID_BODY = """
            {
              "category": "MIDDY",
              "location": "LEFT_WING",
              "shotsTaken": 10,
              "shotsMade": 7,
              "notes": "No notes"
            }
            """;

    // What the fake service hands back.
    private final ShotsResponse shotResponse = new ShotsResponse(1L, Category.MIDDY, Location.LEFT_WING,
            10, 7, new BigDecimal("70.00"), "Good", "No notes", null);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShotsService shotsService;

    // ---------- GET ----------

    @Test
    void getShotByIdShouldReturnShot() throws Exception {
        when(shotsService.findById(1L)).thenReturn(shotResponse);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("MIDDY"))
                .andExpect(jsonPath("$.shotsMade").value(7));
    }

    @Test
    void getAllShotsShouldReturnList() throws Exception {
        when(shotsService.findAll()).thenReturn(List.of(shotResponse, shotResponse));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("MIDDY"));
    }

    @Test
    void getShotByIdWithNonNumberIdShouldReturn400() throws Exception {
        // "abc" can't become a Long → MethodArgumentTypeMismatchException → your handler → 400
        mockMvc.perform(get(BASE_URL + "/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getShotByIdWithNonexistentIdShouldReturn404() throws Exception {
        when(shotsService.findById(999L)).thenThrow(new ShotsNotFoundException(999L));

        mockMvc.perform(get(BASE_URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shots with id 999 not found"));
    }

    // ---------- POST ----------

    @Test
    void createShotShouldReturn201WithLocation() throws Exception {
        when(shotsService.createShots(any(CreateShotsRequest.class))).thenReturn(shotResponse);

        mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/swish/api/v1/shots/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShotWithoutLoginShouldReturn401() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isUnauthorized());

        verify(shotsService, never()).createShots(any());
    }

    @Test
    void createShotWithWrongPasswordShouldReturn401() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("test", "wrong"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createShotWithInvalidFieldShouldReturn400() throws Exception {
        String badBody = """
                {
                  "category": "MIDDY",
                  "location": "LEFT_WING",
                  "shotsTaken": 0,
                  "shotsMade": 0
                }
                """;

        mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Shots taken must be at least 1"));

        // @Valid failed before the method body ran, so the service was never called
        verify(shotsService, never()).createShots(any());
    }

    @Test
    void createShotWithBrokenJsonShouldReturn400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ this is not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShotWithTextPlainShouldReturn415() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(VALID_BODY))
                .andExpect(status().isUnsupportedMediaType());
    }

    // ---------- PATCH ----------

    @Test
    void updateShotShouldReturn200() throws Exception {
        when(shotsService.updateShots(eq(1L), any(UpdateShotsRequest.class))).thenReturn(shotResponse);

        mockMvc.perform(patch(BASE_URL + "/1")
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"notes\": \"No notes\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("No notes"));
    }

    @Test
    void updateShotThatBreaksARuleShouldReturn400() throws Exception {
        // The service's checkIfValid() throws → GlobalExceptionHandler turns it into 400
        when(shotsService.updateShots(eq(1L), any(UpdateShotsRequest.class)))
                .thenThrow(new InvalidShotsException("Shots made cannot be greater than shots taken"));

        mockMvc.perform(patch(BASE_URL + "/1")
                        .with(httpBasic("test", "secret"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"shotsMade\": 50 }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Shots made cannot be greater than shots taken"));
    }

    // ---------- DELETE ----------

    @Test
    void deleteShotShouldReturn204() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1")
                        .with(httpBasic("test", "secret")))
                .andExpect(status().isNoContent());

        verify(shotsService).deleteById(1L);
    }
}
