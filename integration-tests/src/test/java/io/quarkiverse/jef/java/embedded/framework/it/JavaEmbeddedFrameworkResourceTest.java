package io.quarkiverse.jef.java.embedded.framework.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JavaEmbeddedFrameworkResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/java-embedded-framework")
                .then()
                .statusCode(200)
                .body(is("Hello java-embedded-framework"));
    }
}
