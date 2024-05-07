package org.cynic.data_query.it;

import io.restassured.RestAssured;
import java.time.Clock;
import org.cynic.data_query.Configuration;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = {Configuration.class}
)
@ActiveProfiles("it")
@Tag("it")
@ExtendWith(InstancioExtension.class)
public class BaseIT {

    @LocalServerPort
    private int serverPort;

    @Autowired
    protected Clock clock;

    @BeforeEach
    public void setup() {
        RestAssured.port = serverPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
