package io.quarkiverse.jef.java.embedded.framework.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SpiBusResourceTest {
    private final static String PATH = "/java-embedded-framework/spi/";

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
                .body(is("/dev/spidev-0/40000/SPI_MODE_1/7/0"));
    }

    @Test
    public void testSpi1BusInjection() {
        given()
                .when().get(PATH + "spi1")
                .then()
                .statusCode(200)
                .body(is("/dev/spidev-1/50000/SPI_MODE_3/8/1"));
    }
}
