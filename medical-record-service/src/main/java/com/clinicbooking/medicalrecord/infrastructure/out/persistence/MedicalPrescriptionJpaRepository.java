package com.clinicbooking.medicalrecord.infrastructure.out.persistence;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface MedicalPrescriptionJpaRepository extends JpaRepository<MedicalPrescriptionEntity,UUID> { List<MedicalPrescriptionEntity> findByEncounterIdAndActiveTrue(UUID encounterId); void deleteByEncounterId(UUID encounterId); }
