package com.clinicbooking.patient.domain;

import java.time.LocalDate;

public record AttendanceRecord(Long id, Long patientId, Long appointmentId, AttendanceStatus status, LocalDate date) {
}
