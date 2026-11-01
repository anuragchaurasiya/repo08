package com.anurag.campusconnect.registration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void attendeeCanRegisterAndDuplicateEmailIsRejected() throws Exception {
        String eventJson = """
                {
                  "title": "Campus Tech Day",
                  "description": "A technology event",
                  "venue": "Main Hall",
                  "eventDate": "2027-02-15T10:00:00"
                }
                """;

        MvcResult eventResult = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode event = objectMapper.readTree(eventResult.getResponse().getContentAsString());
        long eventId = event.get("id").asLong();

        String registrationJson = """
                {
                  "participantName": "Aarav Student",
                  "email": "aarav@example.com"
                }
                """;

        mockMvc.perform(post("/api/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.participantName").value("Aarav Student"))
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.email").doesNotExist());

        mockMvc.perform(post("/api/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/events/{eventId}/registrations/count", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    void invalidEmailIsRejected() throws Exception {
        String eventJson = """
                {
                  "title": "Design Meetup",
                  "description": "",
                  "venue": "Room 12",
                  "eventDate": "2027-03-01T12:00:00"
                }
                """;
        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andReturn();
        long eventId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(post("/api/events/{eventId}/registrations", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"participantName":"Student","email":"not-an-email"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
