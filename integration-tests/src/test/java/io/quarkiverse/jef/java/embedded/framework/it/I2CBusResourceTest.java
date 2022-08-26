package io.quarkiverse.jef.java.embedded.framework.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class I2CBusResourceTest {
    private final static String PATH = "/java-embedded-framework/i2c/";

    @Test
    public void testManagerInjection() {
        given()
                .when().get(PATH + "manager")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testSpi1BusInjection() {
        given()
                .when().get(PATH + "i2c1")
                .then()
                .statusCode(200)
                .body(is("/dev/i2c-1/false/10/15"));
    }
}
