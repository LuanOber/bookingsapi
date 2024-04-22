package com.bookings.bookingsapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Page<Booking> getBookingsByAgency(Long agencyId, Pageable pageable) {
        return bookingRepository.findByAgencyId(agencyId, pageable);
    }

    public Booking createBooking(Booking booking) throws Exception {
        if (booking.getAgencyId() == null || booking.getHotelId() == null || booking.getCheckIn() == null ||
                booking.getCheckOut() == null || booking.getCustomerId() == null || booking.getPrice() == null) {
            throw new Exception("All fields are required");
        }

        Optional<Booking> existingBooking = bookingRepository.findDuplicateBooking(
                booking.getAgencyId(), booking.getHotelId(), booking.getCheckIn(), booking.getCheckOut(), booking.getCustomerId(), booking.getPrice());

        if (existingBooking.isPresent()) {
            throw new Exception("Duplicate booking is not allowed");
        }

        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Booking updatedBooking) {
        Optional<Booking> optionalBooking = bookingRepository.findById(updatedBooking.getId());

        if (optionalBooking.isPresent()) {
            Booking existingBooking = optionalBooking.get();

            existingBooking.setCheckIn(updatedBooking.getCheckIn() != null ? updatedBooking.getCheckIn() : existingBooking.getCheckIn());
            existingBooking.setCheckOut(updatedBooking.getCheckOut() != null ? updatedBooking.getCheckOut() : existingBooking.getCheckOut());
            existingBooking.setCustomerId(updatedBooking.getCustomerId() != null ? updatedBooking.getCustomerId() : existingBooking.getCustomerId());
            existingBooking.setHotelId(updatedBooking.getHotelId() != null ? updatedBooking.getHotelId() : existingBooking.getHotelId());
            existingBooking.setPrice(updatedBooking.getPrice() != null ? updatedBooking.getPrice() : existingBooking.getPrice());

            return bookingRepository.save(existingBooking);
        }

        throw new RuntimeException("Booking not found with id: " + updatedBooking.getId());
    }
    public Booking updateBooking(Long id, Booking updatedBooking) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking existingBooking = optionalBooking.get();
            existingBooking.setHotelId(updatedBooking.getAgencyId());
            existingBooking.setHotelId(updatedBooking.getHotelId());
            existingBooking.setCheckIn(updatedBooking.getCheckIn());
            existingBooking.setCheckOut(updatedBooking.getCheckOut());
            existingBooking.setCustomerId(updatedBooking.getCustomerId());
            return bookingRepository.save(existingBooking);
        }
        throw new RuntimeException("Booking not found with id: " + id);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }


}
