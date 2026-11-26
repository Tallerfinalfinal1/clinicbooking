package com.clinicbooking.medicalrecord.infrastructure.in.rest;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class MedicalRecordExceptionHandlerTest {
 @Test void mapsMissingAndValidationErrors(){var handler=new MedicalRecordExceptionHandler();assertEquals(404,handler.missing(new java.util.NoSuchElementException("missing")).getStatusCode().value());assertEquals(400,handler.invalid(new IllegalArgumentException("invalid")).getStatusCode().value());assertEquals("invalid",handler.invalid(new IllegalStateException("invalid")).getBody().get("error"));}
}
