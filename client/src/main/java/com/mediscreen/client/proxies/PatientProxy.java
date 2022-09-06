package com.mediscreen.client.proxies;

import com.mediscreen.client.models.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patient", url = "${patient.host}", path = "/patients")
public interface PatientProxy {

    @GetMapping()
    List<Patient> browse();

    @GetMapping("/search")
    List<Patient> search(@RequestParam String lastname, @RequestParam String firstname);

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Patient create(Patient patient);

    @GetMapping("/{id}")
    Patient read(@PathVariable Long id);

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Patient update(Patient patient, @PathVariable Long id);

    @DeleteMapping("/{id}")
    Patient delete(@PathVariable Long id);
}
