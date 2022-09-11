package com.mediscreen.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"patient.host = test", "note.host = test", "report.host = test"})
class ClientApplicationTests {

    @Test
    void contextLoads() {

    }

}
