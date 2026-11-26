package com.clinicbooking.medicalrecord.application.port.out;
import com.clinicbooking.medicalrecord.domain.MedicalRecord; import java.util.*;
public interface MedicalRecordRepositoryPort { MedicalRecord save(MedicalRecord record); Optional<MedicalRecord> findById(UUID id); List<MedicalRecord> findByPatient(UUID patientId); List<MedicalRecord> findByDoctor(UUID doctorId); }
