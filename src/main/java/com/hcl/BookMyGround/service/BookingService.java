package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.*;
import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.exception.ResourceNotFoundException;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.*;
import com.hcl.BookMyGround.repository.BookingRepository;
import com.hcl.BookMyGround.repository.GroundRepository;
import com.hcl.BookMyGround.repository.TimeSlotRepository;
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GroundRepository groundRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private PaymentService paymentService;

    public BookingDTO createBooking(Long userId, Long groundId, Long timeSlotId, Booking booking, Payment payment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new IllegalArgumentException("Ground not found with ID: " + groundId));
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("TimeSlot not found with ID: " + timeSlotId));

        LocalDate today = LocalDate.now();
        if (booking.getBookingDate().isBefore(today)) {
            throw new IllegalArgumentException("Booking date cannot be in the past.");
        }


        boolean isBooked = bookingRepository.existsByGround_GroundIdAndBookingDateAndTimeSlot_Id(
                groundId, booking.getBookingDate(), timeSlotId);
        if (isBooked) {
            throw new IllegalArgumentException("This time slot is already booked for the selected ground and date.");
        }

        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentStatus("PENDING");
        Payment processedPayment = paymentService.processPayment(payment);

        if (!"SUCCESS".equalsIgnoreCase(processedPayment.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment failed. Booking cannot be completed.");
        }

        booking.setUser(user);
        booking.setGround(ground);
        booking.setTimeSlot(timeSlot);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPayment(processedPayment);

        Booking savedBooking = bookingRepository.save(booking);

        processedPayment.setBooking(savedBooking);
        paymentService.updatePayment(processedPayment);
        return mapToBookingDTO(savedBooking);
    }

    public String cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized cancellation attempt");
        }

        if (booking.getBookingDate().atStartOfDay().isBefore(LocalDateTime.now().plusHours(12))) {
            throw new RuntimeException("Booking can only be cancelled at least 12 hours in advance.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        double refundedAmount = booking.getTotalAmount();
        booking.setTotalAmount(0);

        bookingRepository.save(booking);
        return "Booking cancelled. ₹" + refundedAmount + " will be refunded to your account.";
    }
    public List<TimeSlotAvailabilityDTO> getAvailableSlotsForGround(Long groundId, LocalDate date) {
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new IllegalArgumentException("Ground not found with ID: " + groundId));

        List<TimeSlot> allSlots = timeSlotRepository.findAll(); // or use ground.getTimeSlots() if defined
        List<Long> bookedSlotIds = bookingRepository.findBookedTimeSlotIds(groundId, date);

        return allSlots.stream().map(slot -> {
            boolean isBooked = bookedSlotIds.contains(slot.getId());
            return new TimeSlotAvailabilityDTO(slot.getId(), slot.getStartTime(), slot.getEndTime(), isBooked);
        }).collect(Collectors.toList());
    }
    public List<BookingDTO> getUpcomingBookingsByUser(Long userId) {
        LocalDate today = LocalDate.now();
        return bookingRepository.findUpcomingConfirmedBookingsByUserId(userId, today)
                .stream().map(this::mapToBookingDTO).collect(Collectors.toList());
    }

    public List<BookingDTO> getPastBookings(Long userId) {
        return bookingRepository.findByUserUserIdAndBookingDateBefore(userId, LocalDate.now())
                .stream().map(this::mapToBookingDTO).collect(Collectors.toList());
    }


    public BookingDTO mapToBookingDTO(Booking booking) {
        if (booking == null) {
            return null;
        }

        Ground ground = booking.getGround();
        TimeSlot timeSlot = booking.getTimeSlot();
        Payment payment = booking.getPayment();
        User user = booking.getUser();

        GroundDTO groundDTO = null;
        if (ground != null) {
            groundDTO = new GroundDTO(
                    ground.getGroundId(), ground.getName(), ground.getType(),
                    ground.getLocation(), ground.getWeekdayRate(), ground.getWeekendRate(),
                    ground.getContactPerson(), ground.getContactNumber()
            );
        }

        TimeSlotDTO timeSlotDTO = null;
        if (timeSlot != null) {
            timeSlotDTO = new TimeSlotDTO(
                    timeSlot.getId(), timeSlot.getStartTime(), timeSlot.getEndTime()
            );
        }

        PaymentDTO paymentDTO = null;
        if (payment != null) {
            paymentDTO = new PaymentDTO(
                    payment.getPaymentId(), payment.getAmount(),
                    payment.getPaymentMethod(), payment.getPaymentStatus(), payment.getPaymentDate()
            );
        }

        UserDTO userDTO = null;
        if (user != null) {
            userDTO = new UserDTO(
                    user.getUserId(), user.getName(), user.getContactNumber(), user.getEmail()
            );
        }

        return BookingDTO.builder()
                .bookingId(booking.getBookingId())
                .user(userDTO)
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .ground(groundDTO)
                .timeSlot(timeSlotDTO)
                .payment(paymentDTO)
                .build();
    }




//    private BookingDTO mapToDTO(Booking booking) {
//        User user = booking.getUser();
//        UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), user.getContactNumber(), user.getEmail());
//
//        Payment payment = booking.getPayment();
//        PaymentDTO paymentDTO = null;
//        if (payment != null) {
//            paymentDTO = new PaymentDTO();
//            paymentDTO.setPaymentId(payment.getPaymentId());
//            paymentDTO.setPaymentStatus(payment.getPaymentStatus());
//            paymentDTO.setPaymentDate(payment.getPaymentDate());
//            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
//            paymentDTO.setAmount(payment.getAmount());
//        }
//
//        BookingDTO dto = new BookingDTO();
//        dto.setBookingId(booking.getBookingId());
//        dto.setUser(userDTO);
//        dto.setBookingDate(booking.getBookingDate());
//        dto.setStatus(String.valueOf(booking.getStatus()));
//        dto.setTotalAmount(booking.getTotalAmount());
//        dto.setPayment(paymentDTO);
//
//        return dto;
//    }


}