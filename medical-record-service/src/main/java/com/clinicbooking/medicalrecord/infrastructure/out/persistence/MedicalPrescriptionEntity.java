package com.clinicbooking.medicalrecord.infrastructure.out.persistence;
import jakarta.persistence.*; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="his_prescripciones") public class MedicalPrescriptionEntity {
 @Id public UUID id; public UUID encounterId; @Column(nullable=false,length=500) public String medication; public LocalDateTime issuedAt; public boolean active;
 public MedicalPrescriptionEntity(){}
}
