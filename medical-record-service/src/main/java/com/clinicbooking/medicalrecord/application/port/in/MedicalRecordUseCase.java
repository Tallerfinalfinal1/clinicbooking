package com.clinicbooking.medicalrecord.application.port.in;
import com.clinicbooking.medicalrecord.domain.*; import java.util.*;
public interface MedicalRecordUseCase {
 MedicalRecord triage(UUID patient,UUID doctor,int systolic,int diastolic,double temperature,int heartRate,int spo2,List<String> allergies);
 MedicalRecord prescribe(UUID id,String medicine);
 MedicalRecord close(UUID id,String cie10,String evolution,int days);
 List<MedicalRecord> timeline(UUID patient);
 Map<String,Object> productivity(UUID doctor);
}
