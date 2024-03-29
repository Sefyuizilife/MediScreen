package com.mediscreen.client.proxies;

import com.mediscreen.client.models.Note;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@FeignClient(name = "note", url = "${note.host}", path = "/notes")
public interface NoteProxy {

    @GetMapping("/all/patients/{patientId}")
    List<Note> browseByPatientId(@PathVariable Long patientId);

    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Note create(@Valid Note note);

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Note update(@PathVariable BigInteger id, Note note);
}
