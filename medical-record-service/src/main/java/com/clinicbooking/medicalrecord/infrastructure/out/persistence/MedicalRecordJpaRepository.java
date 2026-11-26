package com.clinicbooking.medicalrecord.infrastructure.out.persistence;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecordEntity,UUID>{List<MedicalRecordEntity> findByPatientId(UUID patientId); List<MedicalRecordEntity> findByDoctorId(UUID doctorId);}
