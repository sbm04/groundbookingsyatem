package com.hcl.BookMyGround;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.BookMyGround.controller.BookingController;
import com.hcl.BookMyGround.dto.*;
import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    // Mock JwtHelper to fix context error
    @MockBean
    private com.hcl.BookMyGround.security.JwtHelper jwtHelper;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        // Prepare a sample BookingDTO for return values
        UserDTO userDTO = UserDTO.builder()
                .userId(1L)
                .name("Test User")
                .contactNumber("1234567890")
                .email("test@example.com")
                .build();

        GroundDTO groundDTO = new GroundDTO(2L, "Ground 1", "Cricket", "City", 100.0, 150.0, "Manager", "9876543210");
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO(3L, LocalTime.of(9, 0), LocalTime.of(10, 0));
        PaymentDTO paymentDTO = new PaymentDTO(4L, 100.0, "CARD", "SUCCESS", LocalDate.now());

        bookingDTO = BookingDTO.builder()
                .bookingId(1L)
                .user(userDTO)
                .bookingDate(LocalDate.now())
                .status(BookingStatus.CONFIRMED)
                .totalAmount(100.0)
                .ground(groundDTO)
                .timeSlot(timeSlotDTO)
                .payment(paymentDTO)
                .build();
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("userId", 1L);
        bookingData.put("groundId", 2L);
        bookingData.put("slotId", 3L);
        bookingData.put("bookingDate", LocalDate.now().toString());
        bookingData.put("totalAmount", 100.0);
        bookingData.put("paymentMethod", "CARD");

        when(bookingService.createBooking(anyLong(), anyLong(), anyLong(), any(), any()))
                .thenReturn(bookingDTO);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1L))
                .andExpect(jsonPath("$.user.name").value("Test User"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.ground.name").value("Ground 1"))
                .andExpect(jsonPath("$.timeSlot.id").value(3L))
                .andExpect(jsonPath("$.payment.paymentMethod").value("CARD"));
    }

    @Test
    void testCreateBooking_MissingFields() throws Exception {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("userId", 1L); // missing other fields

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing required fields in the booking data."));
    }

    @Test
    void testCreateBooking_ServiceThrowsException() throws Exception {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("userId", 1L);
        bookingData.put("groundId", 2L);
        bookingData.put("slotId", 3L);
        bookingData.put("bookingDate", LocalDate.now().toString());
        bookingData.put("totalAmount", 100.0);
        bookingData.put("paymentMethod", "CARD");

        when(bookingService.createBooking(anyLong(), anyLong(), anyLong(), any(), any()))
                .thenThrow(new RuntimeException("Some error"));

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error creating booking: Some error")));
    }

    @Test
    void testCancelBooking() throws Exception {
        when(bookingService.cancelBooking(1L, 2L)).thenReturn("Booking cancelled.");

        mockMvc.perform(post("/bookings/cancel/1/user/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking cancelled."));
    }

    @Test
    void testGetSlotAvailability() throws Exception {
        List<TimeSlotAvailabilityDTO> slots = Arrays.asList(
                new TimeSlotAvailabilityDTO(1L, LocalTime.of(9, 0), LocalTime.of(10, 0), false)
        );
        when(bookingService.getAvailableSlotsForGround(2L, LocalDate.of(2024, 6, 1))).thenReturn(slots);

        mockMvc.perform(get("/bookings/availability")
                        .param("groundId", "2")
                        .param("date", "2024-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value(1L))
                .andExpect(jsonPath("$[0].booked").value(false));
    }

    @Test
    void testGetUpcomingBookings() throws Exception {
        when(bookingService.getUpcomingBookingsByUser(1L)).thenReturn(Collections.singletonList(bookingDTO));

        mockMvc.perform(get("/bookings/1/bookings/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1L));
    }

    @Test
    void testGetPastBookings() throws Exception {
        when(bookingService.getPastBookings(1L)).thenReturn(Collections.singletonList(bookingDTO));

        mockMvc.perform(get("/bookings/1/past"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1L));
    }
}