package io.quarkiverse.jef.java.embedded.framework.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SerialBusResourceTest {
    private final static String PATH = "/java-embedded-framework/serial/";

    @Test
    public void testManagerInjection() {
        given()
                .when().get(PATH + "manager")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testDefaultBusInjection() {
        given()
                .when().get(PATH + "default")
                .then()
                .statusCode(200)
                .body(is("/dev/serial0/B38400"));
    }

    @Test
    public void testSerial1BusInjection() {
        given()
                .when().get(PATH + "serial1")
                .then()
                .statusCode(200)
                .body(is("/dev/serial1/B1152000"));
    }
}
