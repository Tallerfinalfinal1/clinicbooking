package com.clinicbooking.patient.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleBlockJpaRepository extends JpaRepository<ScheduleBlockEntity, Long> {
    List<ScheduleBlockEntity> findByDoctorIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long doctorId, LocalDateTime to, LocalDateTime from);

    void deleteByAppointmentId(Long appointmentId);
}
