package com.clinicbooking.medicalrecord.domain;
import java.time.LocalDateTime;
import java.util.*;
/** Aggregate with pure clinical rules; infrastructure concerns stay outside domain. */
public class MedicalRecord {
 private final UUID id; private final UUID patientId; private final UUID doctorId; private final LocalDateTime createdAt;
 private final int systolic; private final int diastolic; private final double temperature; private final int heartRate; private final int spo2;
 private final TriageLevel triage; private final boolean clinicalAlert; private boolean closed; private String diagnosisCode; private String evolution; private int disabilityDays;
 private final List<String> prescriptions=new ArrayList<>(); private final Set<String> allergyTerms=new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
 public MedicalRecord(UUID id,UUID patientId,UUID doctorId,int systolic,int diastolic,double temperature,int heartRate,int spo2,LocalDateTime createdAt){ this(id,patientId,doctorId,systolic,diastolic,temperature,heartRate,spo2,createdAt,List.of()); }
 public MedicalRecord(UUID id,UUID patientId,UUID doctorId,int systolic,int diastolic,double temperature,int heartRate,int spo2,LocalDateTime createdAt,List<String> allergies){
  if(patientId==null||doctorId==null) throw new IllegalArgumentException("Paciente y médico son obligatorios");
  if(systolic<50||systolic>260||diastolic<30||diastolic>160||temperature<30||temperature>45||heartRate<20||heartRate>250||spo2<50||spo2>100) throw new IllegalArgumentException("Signos vitales fuera de rango");
  this.id=id==null?UUID.randomUUID():id; this.patientId=patientId; this.doctorId=doctorId; this.systolic=systolic; this.diastolic=diastolic; this.temperature=temperature; this.heartRate=heartRate; this.spo2=spo2; this.createdAt=createdAt==null?LocalDateTime.now():createdAt;
  this.triage=classify(systolic,diastolic,temperature,heartRate,spo2); this.clinicalAlert=triage==TriageLevel.I||triage==TriageLevel.II; if(allergies!=null)allergies.stream().filter(a->a!=null&&!a.isBlank()).map(String::trim).forEach(allergyTerms::add);
 }
 public static TriageLevel classify(int s,int d,double t,int hr,int o){ if(o<85||s<70||hr<40||hr>150||t>=40.0)return TriageLevel.I; if(o<90||s<90||s>=180||d>=120||hr<50||hr>130||t>=39.0||t<35.0)return TriageLevel.II; if(o<94||s>=160||s<100||hr>110||t>=38.0)return TriageLevel.III; if(t>=37.5||hr>100)return TriageLevel.IV; return TriageLevel.V; }
 public void prescribe(String medicine){ if(closed)throw new IllegalStateException("El encuentro debe estar abierto"); if(medicine==null||medicine.isBlank())throw new IllegalArgumentException("La fórmula es obligatoria"); String normalized=medicine.trim(); if(hasAllergyAlert(normalized))throw new IllegalStateException("Alerta de alergia: prescripción bloqueada"); prescriptions.add(normalized); }
 public boolean hasAllergyAlert(String medicine){if(medicine==null)return false;String normalized=medicine.toLowerCase(Locale.ROOT);return allergyTerms.stream().anyMatch(a->normalized.contains(a.toLowerCase(Locale.ROOT)));}
 public Set<String> getAllergyTerms(){return Collections.unmodifiableSet(allergyTerms);}
 public void restorePrescription(String medicine){ if(medicine!=null&&!medicine.isBlank())prescriptions.add(medicine.trim()); }
 public void close(String cie10,String evolution,int days){ if(closed)throw new IllegalStateException("El encuentro ya está cerrado"); if(cie10==null||!cie10.matches("[A-Z][0-9A-Z]{2}(\\.[0-9A-Z]{1,4})?"))throw new IllegalArgumentException("Código CIE-10 inválido"); if(evolution==null||evolution.isBlank())throw new IllegalArgumentException("La evolución es obligatoria"); if(days<0||days>180)throw new IllegalArgumentException("Días de incapacidad inválidos"); diagnosisCode=cie10; this.evolution=evolution.trim(); disabilityDays=days; closed=true; }
 public UUID getId(){return id;} public UUID getPatientId(){return patientId;} public UUID getDoctorId(){return doctorId;} public LocalDateTime getCreatedAt(){return createdAt;} public int getSystolic(){return systolic;} public int getDiastolic(){return diastolic;} public double getTemperature(){return temperature;} public int getHeartRate(){return heartRate;} public int getSpo2(){return spo2;} public TriageLevel getTriage(){return triage;} public boolean isClinicalAlert(){return clinicalAlert;} public boolean isClosed(){return closed;} public String getDiagnosisCode(){return diagnosisCode;} public String getEvolution(){return evolution;} public int getDisabilityDays(){return disabilityDays;} public List<String> getPrescriptions(){return Collections.unmodifiableList(prescriptions);}
}
