package com.mediscreen.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"patient.host = test", "note.host = test"})
class ReportApplicationTests {

    @Test
    void contextLoads() {

    }

}
