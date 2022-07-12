package br.com.marvel;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MockApiResourceTest {

    @Test
    @DisplayName("1 - Mock da API da Marvel")
    public void testMockApi() {
        given()
                .when().get("/v1/public/characters")
                .then()
                .statusCode(200);
    }

}