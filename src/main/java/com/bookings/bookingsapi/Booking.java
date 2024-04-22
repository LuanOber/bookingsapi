package com.bookings.bookingsapi;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private @Id @GeneratedValue Long id;

    @NotNull(message = "Agency ID is required")
    private Long agencyId;

    @NotNull(message = "Hotel ID is required")
    private Long hotelId;

    @NotNull(message = "Check-In date is required")
    private LocalDate checkIn;

    @NotNull(message = "Check-Out date is required")
    private LocalDate checkOut;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Price is required")
    private Long price;
}
