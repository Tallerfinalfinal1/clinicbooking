package com.clinicbooking.medicalrecord.infrastructure.out.persistence;
import jakarta.persistence.*; import java.time.LocalDateTime; import java.util.UUID;
@Entity @Table(name="his_encuentros") public class MedicalRecordEntity {
 @Id public UUID id; public UUID patientId; public UUID doctorId; public LocalDateTime createdAt; public int systolic; public int diastolic; public double temperature; public int heartRate; public int spo2; public String triage; public boolean clinicalAlert; public boolean closed; public String diagnosisCode; @Column(length=4000) public String evolution; public int disabilityDays; @Column(length=2000) public String allergies;
 public MedicalRecordEntity(){}
}
