package com.clinicbooking.medicalrecord.infrastructure.in.rest;
import com.clinicbooking.medicalrecord.application.port.in.MedicalRecordUseCase; import com.clinicbooking.medicalrecord.domain.MedicalRecord; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/api/v1/medical-records") public class MedicalRecordController {
 private final MedicalRecordUseCase service; public MedicalRecordController(MedicalRecordUseCase service){this.service=service;}
 public record TriageRequest(@NotNull UUID patientId,@NotNull UUID doctorId,@Min(50)@Max(260) int systolic,@Min(30)@Max(160) int diastolic,@DecimalMin("30")@DecimalMax("45") double temperature,@Min(20)@Max(250) int heartRate,@Min(50)@Max(100) int spo2,List<String> allergies){}
 public record PrescriptionRequest(@NotBlank String medication){}
 public record CloseRequest(@NotBlank String cie10,@NotBlank String evolution,@Min(0)@Max(180) int disabilityDays){}
 @PostMapping("/triage-assessment") public MedicalRecord triage(@Valid @RequestBody TriageRequest r){return service.triage(r.patientId(),r.doctorId(),r.systolic(),r.diastolic(),r.temperature(),r.heartRate(),r.spo2(),r.allergies());}
 @PostMapping("/{id}/prescriptions") public MedicalRecord prescription(@PathVariable UUID id,@Valid @RequestBody PrescriptionRequest r){return service.prescribe(id,r.medication());}
 @PostMapping("/{id}/close-encounter") public MedicalRecord close(@PathVariable UUID id,@Valid @RequestBody CloseRequest r){return service.close(id,r.cie10(),r.evolution(),r.disabilityDays());}
 @GetMapping("/patient/{patientId}/clinical-timeline") public List<MedicalRecord> timeline(@PathVariable UUID patientId){return service.timeline(patientId);}
 @GetMapping("/doctor/{doctorId}/productivity-report") public Map<String,Object> productivity(@PathVariable UUID doctorId){return service.productivity(doctorId);}
}
