package io.quarkiverse.jef.java.embedded.framework.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GPIOResourceTest {
    private final static String PATH = "/java-embedded-framework/gpio/";

    @Test
    public void testManagerInjection() {
        given()
                .when().get(PATH + "manager")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Test
    public void testDefaultGPIOInjection() {
        given()
                .when().get(PATH + "default")
                .then()
                .statusCode(200)
                .body(is("dummy pin/dummy/null/32"));
    }

    @Test
    public void testGPIO1Injection() {
        given()
                .when().get(PATH + "pin1")
                .then()
                .statusCode(200)
                .body(is("dummy pin/dummy/null/32"));
    }
}
