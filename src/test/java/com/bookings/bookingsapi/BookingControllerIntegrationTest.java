package com.bookings.bookingsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    public void testCreateBooking() throws Exception {
        Booking booking = new Booking(1L,123L, 123L, LocalDate.now(), LocalDate.now().plusDays(5), 456L, 500L);
        when(bookingService.createBooking(any())).thenReturn(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(456L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(500L));
    }
}
