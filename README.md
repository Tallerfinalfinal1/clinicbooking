# ClinicBooking — plataforma clínica por microservicios

Java 21 · Spring Boot 3.3.5 · Arquitectura hexagonal · PostgreSQL/Neon · JUnit 5 + Mockito · JaCoCo ≥ 80 % · Docker/Render

## Servicios (exactamente cuatro)

| Servicio | Puerto | Esquema PostgreSQL | Tablas |
|---|---:|---|---|
| `patient-service` | 8081 | `schema_pacientes` | `pac_*` |
| `appointment-service` | 8082 | `schema_citas` | `cit_*` |
| `notification-service` | 8083 | `schema_notificaciones` | `not_*` |
| `medical-record-service` | 8084 | `schema_historias` | `his_*` |

Los servicios comparten una única base `neondb` en Neon, aislados por esquema. No se hacen joins entre esquemas; las integraciones entre servicios se realizan por REST. Cada módulo separa reglas puras (`domain`), casos de uso y puertos (`application`) y adaptadores REST/JPA/HTTP (`infrastructure`).

## Ejecutar localmente

```bash
docker compose up --build
```

El compose inicia PostgreSQL local y los cuatro servicios. Alternativamente, con Maven instalado:

```bash
mvn clean verify
mvn -pl patient-service spring-boot:run
mvn -pl appointment-service spring-boot:run
mvn -pl notification-service spring-boot:run
mvn -pl medical-record-service spring-boot:run
```

JaCoCo genera el reporte en `<servicio>/target/site/jacoco/index.html`; el `verify` exige cobertura mínima de instrucciones y líneas del 80 %.

## APIs no-CRUD

- **Patient (8081):** `GET /api/v1/doctors/{id}/available-slots`; `POST /api/v1/doctors/{id}/block-schedule`; `GET /api/v1/doctors/by-specialty-and-date`; `POST /api/v1/doctors/{id}/booked-slots`; `GET /api/v1/patients/{id}/clinical-attendance-summary`; `POST /api/v1/patients/{id}/verify-insurance`; `POST /api/v1/patients/{id}/attendance`.
- **Appointment (8082):** `POST /api/v1/appointments/{id}/confirm`; `POST /api/v1/appointments/{id}/reschedule`; `POST /api/v1/appointments/{id}/cancel`; `POST /api/v1/appointments/{id}/mark-no-show`; `GET /api/v1/appointments/daily-agenda`; `POST /api/v1/appointments/{id}/complete`; `POST /api/v1/appointments/reminders/dispatch-due`.
- **Notification (8083):** `POST /api/v1/notifications/send-email`; `POST /api/v1/notifications/send-sms`; `POST /api/v1/notifications/dispatch-appointment-reminder`; `GET /api/v1/notifications/delivery-stats`; `POST /api/v1/notifications/{id}/retry`.
- **Medical record (8084):** `POST /api/v1/medical-records/triage-assessment`; `POST /api/v1/medical-records/{id}/prescriptions`; `POST /api/v1/medical-records/{id}/close-encounter`; `GET /api/v1/medical-records/patient/{patientId}/clinical-timeline`; `GET /api/v1/medical-records/doctor/{doctorId}/productivity-report`.

## Neon y variables

Neon: host `ep-frosty-paper-bb4zuk5w.c-15.us-east-1.aws.neon.tech`, puerto `5432`, base `neondb`, usuario `neondb_owner`; usar siempre `sslmode=require`. `render.yaml` define los cuatro servicios web Docker y no provisiona una base de datos Render. En Render ingresa `SPRING_DATASOURCE_PASSWORD` como secreto para cada servicio.

| Variable | Uso |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC a Neon con `currentSchema` del servicio; en local se configura en Compose |
| `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` | Usuario y contraseña de Neon; contraseña como secreto |
| `TEXTBEE_API_KEY` | Credencial TextBee; vacío activa SMS simulado |
| `TEXTBEE_DEVICE_ID` | Dispositivo TextBee asociado; opcional si la cuenta usa dispositivo predeterminado |
| `TEXTBEE_BASE_URL` | Por defecto `https://api.textbee.dev/api/v1` |
| `NOTIFICATION_INTERNAL_KEY` | Opcional: si se configura, `POST /send-sms` exige `X-Internal-Key` |
| `BREVO_API_KEY`, `MAIL_FROM_EMAIL`, `MAIL_FROM_NAME` | Envío de correo mediante Brevo; sin clave permanece simulado |
| `PATIENT_SERVICE_HOSTPORT`, `NOTIFICATION_SERVICE_HOSTPORT` | Direcciones internas usadas por appointment-service |
| `HOLIDAYS_API_URL`, `HOLIDAYS_COUNTRY`, `REMINDER_CRON` | Configuración de festivos y recordatorios |
| `HTTP_CONNECT_TIMEOUT_MS`, `HTTP_READ_TIMEOUT_MS` | Timeouts HTTP externos |

En `patient-service`, `appointment-service` y `notification-service`, HikariCP usa `maximum-pool-size: 5` y `minimum-idle: 1`. El módulo adicional `medical-record-service` conserva su pool independiente de 4 conexiones. Los esquemas se crean si faltan y Hibernate mantiene las tablas mediante `ddl-auto: update` (para producción estricta se recomienda migrar a Flyway/Liquibase).

## Despliegue en Render con Neon

La base de datos PostgreSQL es externa y ya está alojada en Neon. Render no debe crear ni administrar una base de datos para este proyecto: `render.yaml` no contiene una sección `databases:`. Antes de aplicar el Blueprint, confirma que existen estos tres esquemas:

- `schema_pacientes`
- `schema_citas`
- `schema_notificaciones`

1. Publica el repositorio y crea/aplica un Blueprint desde `render.yaml`.
2. Render define las URLs JDBC y el usuario `neondb_owner` para los tres servicios. Cuando el Blueprint solicite el secreto, pega la contraseña de Neon en `SPRING_DATASOURCE_PASSWORD` para cada servicio que lo solicite. No pegues la contraseña en el repositorio ni en `application.yml`.
3. El cuarto módulo, `medical-record-service`, permanece declarado como servicio adicional y utiliza `schema_historias`; asegúrate de que ese esquema también exista si vas a desplegarlo.
4. En `notification-service`, configura `TEXTBEE_API_KEY` y `TEXTBEE_DEVICE_ID` para SMS real; si quedan vacías, se conserva el modo simulado. `TEXTBEE_BASE_URL` ya tiene un valor predeterminado.
5. Configura las credenciales de Brevo si deseas correo real.
6. Verifica `/actuator/health` en los servicios desplegados.
