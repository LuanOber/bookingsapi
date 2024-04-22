package com.bookings.bookingsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    public void testGetAllBookings() throws Exception {
        Booking booking1 = new Booking(1L,123L, 123L, LocalDate.now(), LocalDate.now().plusDays(5), 456L, 500L);
        Booking booking2 = new Booking(2L,124L, 124L, LocalDate.now(), LocalDate.now().plusDays(3), 457L, 400L);
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    public void testCreateBooking() throws Exception {
        Booking booking = new Booking(1L,123L, 123L, LocalDate.now(), LocalDate.now().plusDays(5), 456L, 500L);
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        String bookingJson = objectMapper.writeValueAsString(booking);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(456L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(500L));
    }


    @Test
    public void testGetBookingById() throws Exception {
        Long bookingId = 1L;
        Booking booking = new Booking(bookingId, 123L,123L, LocalDate.now(), LocalDate.now().plusDays(5), 456L, 500L);
        when(bookingService.getBookingById(bookingId)).thenReturn(Optional.of(booking));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookingId));
    }

    @Test
    public void testUpdateBooking() throws Exception {
        Booking booking = new Booking(1L,123L, 123L, LocalDate.now(), LocalDate.now().plusDays(5), 456L, 500L);
        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(booking);

        String bookingJson = objectMapper.writeValueAsString(booking);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(500L));
    }

    @Test
    public void testDeleteBooking() throws Exception {
        Long bookingId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookings/{id}", bookingId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(bookingService, times(1)).deleteBooking(bookingId);
    }
}
