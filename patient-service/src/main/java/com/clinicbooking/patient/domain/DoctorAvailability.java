package com.clinicbooking.patient.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record DoctorAvailability(Doctor doctor, Map<LocalDate, List<LocalDateTime>> slotsByDate) {
}
