# Patient Intake API — Step 1: Plain CRUD

This is Step 1 of the **Patient Intake & Symptom Triage Assistant** project:
a plain Spring Boot REST API with no AI in it yet. The goal is a solid,
well-understood foundation before adding any LLM calls in Step 2.

> ⚠️ **Use synthetic/fake data only.** Never enter real patient information
> into this app — there is no authentication, encryption, or audit logging
> here. That's expected for a portfolio project, but it means the data must
> always be made-up. This is worth stating explicitly in your project README
> later — it shows awareness of healthcare data handling, which is a plus in
> interviews, not just a disclaimer.

## Stack
- Java 17
- Spring Boot 3.3 (Web + Data JPA + Validation)
- H2 in-memory database (swap for Postgres in Step 3)
- Maven

## Run it

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080`.

> Note: data lives in memory only — every time you stop the app, all intake
> notes are wiped. That's expected and fine for this stage.

## Endpoints

| Method | Path                 | Description            |
|--------|----------------------|--------------------------|
| POST   | `/intake-notes`      | Create an intake note    |
| GET    | `/intake-notes`      | List all intake notes    |
| GET    | `/intake-notes/{id}` | Get one intake note      |
| PUT    | `/intake-notes/{id}` | Update an intake note    |
| DELETE | `/intake-notes/{id}` | Delete an intake note    |

## Try it with curl

**Create an intake note (fake data only):**
```bash
curl -X POST http://localhost:8080/intake-notes \
  -H "Content-Type: application/json" \
  -d '{"patientAge":45,"symptomText":"Chest tightness for 2 days, worse with exertion, mild shortness of breath"}'
```

**List all intake notes:**
```bash
curl http://localhost:8080/intake-notes
```

**Get one intake note (replace 1 with a real id from the response above):**
```bash
curl http://localhost:8080/intake-notes/1
```

**Update an intake note:**
```bash
curl -X PUT http://localhost:8080/intake-notes/1 \
  -H "Content-Type: application/json" \
  -d '{"patientAge":45,"symptomText":"Updated: chest tightness resolved after rest","status":"IN_REVIEW"}'
```

**Delete an intake note:**
```bash
curl -X DELETE http://localhost:8080/intake-notes/1
```

## Browse the database in a browser

While the app is running, go to:

```
http://localhost:8080/h2-console
```

Use JDBC URL: `jdbc:h2:mem:intakedb`, username `sa`, password blank (empty).
Click "Connect" — you'll see an `INTAKE_NOTES` table you can query directly
with SQL. This is a good way to *see* what your API is actually doing to the
database.

## Project structure

```
src/main/java/com/intakeapi/
├── IntakeApiApplication.java             # entry point
├── model/IntakeNote.java                 # the entity (maps to the intake_notes table)
├── repository/IntakeNoteRepository.java  # gives us CRUD for free (Spring Data JPA)
└── controller/IntakeNoteController.java  # the actual REST endpoints
```

## What's next (Step 2)

Once this CRUD API feels solid and you can explain every file above, we add:
- `POST /intake-notes/{id}/classify` — one endpoint that calls an LLM API to
  classify the note (likely department + urgency level) and saves the result.

No agents, no LangChain, no vector DB yet — just one plain HTTP call to an
LLM, same shape as any other external API call you've made before.
