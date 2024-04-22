package com.bookings.bookingsapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            return ResponseEntity.ok(booking.get());
        } else {
            LOGGER.error("Booking with ID {} not found.", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking with ID " + id + " not found.");
        }
    }

    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<Page<Booking>> getBookingsByAgency(@PathVariable Long agencyId, Pageable pageable) {
        Page<Booking> bookingsPage = bookingService.getBookingsByAgency(agencyId, pageable);

        if (bookingsPage.isEmpty()) {
            LOGGER.error("Agency bookings with ID {} was not found on current page", agencyId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency bookings with ID " + agencyId + " was not found on current page");
        }

        return ResponseEntity.ok(bookingsPage);
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            LOGGER.info("Created new booking with ID {}", createdBooking.getId());
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error creating booking: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating booking: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        try {
            Booking booking = bookingService.updateBooking(id, updatedBooking);
            if (booking != null) {
                LOGGER.info("Updated booking with ID {}", id);
                return ResponseEntity.ok(booking);
            } else {
                LOGGER.error("Booking with ID {} not found for update.", id);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking with ID " + id + " not found for update.");
            }
        } catch (Exception e) {
            LOGGER.error("Error updating booking with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating booking with ID " + id + ": " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            Optional<Booking> booking = bookingService.getBookingById(id);
            if (booking == null) {
                LOGGER.error("Booking with ID {} not found for update.", id);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking with ID " + id + " not found for update.");
            }

            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Booking.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    Object newValue = field.getType().equals(Long.class) && value instanceof Integer ? Long.valueOf((Integer) value) : value;
                    ReflectionUtils.setField(field, booking.get(), newValue);
                }
            });

            Booking updatedBooking = bookingService.updateBooking(booking.get());
            LOGGER.info("Updated booking with ID {}", id);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            LOGGER.error("Error updating booking with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating booking with ID " + id + ": " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            LOGGER.info("Deleted booking with ID {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOGGER.error("Error deleting booking with ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting booking with ID " + id + ": " + e.getMessage());
        }
    }
}

