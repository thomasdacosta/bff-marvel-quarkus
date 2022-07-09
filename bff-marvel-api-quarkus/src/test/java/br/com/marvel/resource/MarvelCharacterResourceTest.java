package br.com.marvel.resource;

import br.com.marvel.repository.entity.MarvelCharacterEntity;
import br.com.marvel.repository.entity.ThumbnailCharacterEntity;
import br.com.marvel.repository.entity.UrlCharacterEntity;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.utils.JsonUtils;
import br.com.marvel.utils.ResourceLoader;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class MarvelCharacterResourceTest {

    @BeforeEach
    @Transactional
    public void before() {
        UrlCharacterEntity.deleteAll();
        MarvelCharacterEntity.deleteAll();
        ThumbnailCharacterEntity.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("1 - Criando Personagem")
    public void testCreateCharacter() {
        Response response = createCharacter("json/character2.json");
        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(response.body().print(), MarvelCharacter.class);

        assertNotNull(marvelCharacter);
        assertNotNull(marvelCharacter.getId());
        assertNotNull(marvelCharacter.getThumbnail());
        assertEquals(3, marvelCharacter.getUrlCharacters().size());
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());
    }

    @Test
    @Order(2)
    @DisplayName("2 - Atualizando Personagem")
    public void testUpdateCharacter() {
        Response response = createCharacter("json/character3.json");
        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(response.body().print(), MarvelCharacter.class);

        assertNotNull(marvelCharacter);
        assertNotNull(marvelCharacter.getId());
        assertNotNull(marvelCharacter.getThumbnail());
        assertEquals(3, marvelCharacter.getUrlCharacters().size());
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());

        marvelCharacter.setName("Spider Man");

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.createJson(marvelCharacter))
                .when()
                .put("/characters")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseUpdate.statusCode());
        assertEquals("Spider Man", responseUpdate.jsonPath().getString("name"));
        assertEquals(3, responseUpdate.jsonPath().getList("urls").size());
    }

    @Test
    @Order(3)
    @DisplayName("3 - Excluindo Personagem")
    public void testDeleteCharacter() {
        Response response = createCharacter("json/character1.json");
        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(response.body().print(), MarvelCharacter.class);

        assertNotNull(marvelCharacter);
        assertNotNull(marvelCharacter.getId());
        assertNotNull(marvelCharacter.getThumbnail());
        assertEquals(3, marvelCharacter.getUrlCharacters().size());
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/characters/" + marvelCharacter.getId())
                .then()
                .extract().response();

        assertEquals(Status.NO_CONTENT.getStatusCode(), responseUpdate.statusCode());

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/characters/local?name=" + marvelCharacter.getName())
                .then()
                .extract().response();

        assertEquals(Status.NOT_FOUND.getStatusCode(), responseSearch.statusCode());
    }

    private Response createCharacter(String file) {
        return given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(file))
                .when()
                .post("/characters")
                .then()
                .extract().response();
    }

}