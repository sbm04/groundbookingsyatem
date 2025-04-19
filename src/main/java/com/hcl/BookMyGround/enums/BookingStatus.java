package com.hcl.BookMyGround.enums;



public enum BookingStatus {
    Pending,
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    public static BookingStatus fromString(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Booking status cannot be null.");
        }
        try {
            return BookingStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid booking status: " + status);
        }
    }
}

