package com.clinicbooking.medicalrecord.domain;
import org.junit.jupiter.api.Test; import java.util.UUID; import static org.junit.jupiter.api.Assertions.*;
class MedicalRecordTest {
 @Test void triageAndAlertAreCalculated(){var m=new MedicalRecord(null,UUID.randomUUID(),UUID.randomUUID(),80,50,39.5,140,88,null);assertEquals(TriageLevel.II,m.getTriage());assertTrue(m.isClinicalAlert());}
 @Test void validatesVitals(){assertThrows(IllegalArgumentException.class,()->new MedicalRecord(null,UUID.randomUUID(),UUID.randomUUID(),10,50,37,80,99,null));}
 @Test void prescriptionChecksEncounterAndAllergy(){var m=record();m.prescribe("Amoxicilina");assertThrows(IllegalStateException.class,()->m.prescribe("penicilina"));m.prescribe("Acetaminofén");assertEquals(2,m.getPrescriptions().size());}
 @Test void closingEncounterValidatesAndPreventsSecondClose(){var m=record();assertThrows(IllegalArgumentException.class,()->m.close("bad","evolución",0));m.close("J06.9","Paciente estable",2);assertTrue(m.isClosed());assertThrows(IllegalStateException.class,()->m.close("J06.9","otra",0));assertThrows(IllegalStateException.class,()->m.prescribe("X"));}
 @Test void exposesEncounterDataAndDiagnosis(){var m=record();m.close("J06.9","evolución completa",3);assertNotNull(m.getId());assertNotNull(m.getPatientId());assertNotNull(m.getDoctorId());assertNotNull(m.getCreatedAt());assertEquals(120,m.getSystolic());assertEquals(80,m.getDiastolic());assertEquals(36.8,m.getTemperature());assertEquals(75,m.getHeartRate());assertEquals(98,m.getSpo2());assertEquals("J06.9",m.getDiagnosisCode());assertEquals("evolución completa",m.getEvolution());assertEquals(3,m.getDisabilityDays());assertTrue(m.getAllergyTerms().contains("penicilina"));assertEquals(1,m.getAllergyTerms().size());}
 @Test void restoresPrescriptionForPersistence(){var m=record();m.restorePrescription("Medicina");assertEquals("Medicina",m.getPrescriptions().get(0));}
 private MedicalRecord record(){return new MedicalRecord(null,UUID.randomUUID(),UUID.randomUUID(),120,80,36.8,75,98,null,List.of("penicilina"));}
}
