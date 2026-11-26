package com.clinicbooking.medicalrecord.application.service;
import com.clinicbooking.medicalrecord.application.port.in.MedicalRecordUseCase; import com.clinicbooking.medicalrecord.application.port.out.MedicalRecordRepositoryPort; import com.clinicbooking.medicalrecord.domain.*; import java.time.*; import java.util.*; import java.util.stream.*;
public class MedicalRecordService implements MedicalRecordUseCase {
 private final MedicalRecordRepositoryPort repository; private final Clock clock;
 public MedicalRecordService(MedicalRecordRepositoryPort repository,Clock clock){this.repository=repository;this.clock=clock;}
 public MedicalRecord triage(UUID p,UUID d,int s,int dia,double t,int hr,int spo,List<String> allergies){return repository.save(new MedicalRecord(null,p,d,s,dia,t,hr,spo,LocalDateTime.now(clock),allergies));}
 public MedicalRecord prescribe(UUID id,String med){MedicalRecord m=repository.findById(id).orElseThrow(()->new NoSuchElementException("Encuentro no encontrado"));m.prescribe(med);return repository.save(m);}
 public MedicalRecord close(UUID id,String cie,String evo,int days){MedicalRecord m=repository.findById(id).orElseThrow(()->new NoSuchElementException("Encuentro no encontrado"));m.close(cie,evo,days);return repository.save(m);}
 public List<MedicalRecord> timeline(UUID p){return repository.findByPatient(p).stream().sorted(Comparator.comparing(MedicalRecord::getCreatedAt)).toList();}
 public Map<String,Object> productivity(UUID d){List<MedicalRecord> all=repository.findByDoctor(d);List<MedicalRecord> closed=all.stream().filter(MedicalRecord::isClosed).toList();Map<TriageLevel,Long> distribution=closed.stream().collect(Collectors.groupingBy(MedicalRecord::getTriage,()->new EnumMap<>(TriageLevel.class),Collectors.counting()));return Map.of("doctorId",d,"closedEncounters",closed.size(),"triageDistribution",distribution,"prescriptionsIssued",all.stream().mapToInt(x->x.getPrescriptions().size()).sum());}
}
