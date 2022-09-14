package com.mediscreen.client.proxies;

import com.mediscreen.client.configurations.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "report", url = "${report.host}", path = "/assess", configuration = ClientConfiguration.class)
public interface ReportProxy {

    @GetMapping(value = "{patientId}")
    String getDiabetesReport(@PathVariable Long patientId);
}
