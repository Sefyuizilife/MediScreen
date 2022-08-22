package com.mediscreen.note.repositories;

import com.mediscreen.note.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

public interface NoteRepository extends MongoRepository<Note, BigInteger> {

    List<Note> findAllByPatientId(Long patientId);
}
