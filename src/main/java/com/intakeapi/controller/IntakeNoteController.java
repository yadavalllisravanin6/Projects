package com.intakeapi.controller;

import com.intakeapi.model.IntakeNote;
import com.intakeapi.repository.IntakeNoteRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/intake-notes")
public class IntakeNoteController {

    private final IntakeNoteRepository intakeNoteRepository;

    // Constructor injection - Spring wires the repository in automatically
    public IntakeNoteController(IntakeNoteRepository intakeNoteRepository) {
        this.intakeNoteRepository = intakeNoteRepository;
    }

    // POST /intake-notes - create a new intake note
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IntakeNote createIntakeNote(@Valid @RequestBody IntakeNote note) {
        // Ignore any id/status/createdAt the client might send - always start fresh
        IntakeNote newNote = new IntakeNote(note.getPatientAge(), note.getSymptomText());
        return intakeNoteRepository.save(newNote);
    }

    // GET /intake-notes - list all intake notes
    @GetMapping
    public List<IntakeNote> getAllIntakeNotes() {
        return intakeNoteRepository.findAll();
    }

    // GET /intake-notes/{id} - get a single intake note
    @GetMapping("/{id}")
    public IntakeNote getIntakeNote(@PathVariable Long id) {
        return intakeNoteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Intake note " + id + " not found"));
    }

    // PUT /intake-notes/{id} - update an existing intake note
    @PutMapping("/{id}")
    public IntakeNote updateIntakeNote(@PathVariable Long id, @Valid @RequestBody IntakeNote updated) {
        IntakeNote existing = intakeNoteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Intake note " + id + " not found"));

        existing.setPatientAge(updated.getPatientAge());
        existing.setSymptomText(updated.getSymptomText());
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        return intakeNoteRepository.save(existing);
    }

    // DELETE /intake-notes/{id} - delete an intake note
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIntakeNote(@PathVariable Long id) {
        if (!intakeNoteRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Intake note " + id + " not found");
        }
        intakeNoteRepository.deleteById(id);
    }
}
