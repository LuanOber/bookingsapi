package com.bookings.bookingsapi;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByAgencyId(Long agencyId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.agencyId = ?1 AND b.hotelId = ?2 AND b.checkIn = ?3 AND b.checkOut = ?4 AND b.customerId = ?5 AND b.price = ?6")
    Optional<Booking> findDuplicateBooking(Long agencyId, Long hotelId, LocalDate checkIn, LocalDate checkOut, Long customerId, Long price);
}
