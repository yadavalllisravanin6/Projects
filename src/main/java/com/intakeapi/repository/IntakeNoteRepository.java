package com.intakeapi.repository;

import com.intakeapi.model.IntakeNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Extending JpaRepository gives us save(), findAll(), findById(),
 * deleteById() etc. for free - no SQL to write for basic CRUD.
 */
@Repository
public interface IntakeNoteRepository extends JpaRepository<IntakeNote, Long> {
}
