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
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class MarvelCharacterResourceTest {

    private static final String CHARACTER_1_JSON = "json/character1.json";
    private static final String CHARACTER_2_JSON = "json/character2.json";
    private static final String CHARACTER_3_JSON = "json/character3.json";

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
        createCharacter(CHARACTER_2_JSON);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Atualizando Personagem")
    public void testUpdateCharacter() {
        MarvelCharacter marvelCharacter = createCharacter(CHARACTER_3_JSON);
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
    @DisplayName("3 - Atualizando Personagem Não Existe")
    public void testUpdateCharacterNotFound() {
        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(ResourceLoader.loadFile(CHARACTER_1_JSON), MarvelCharacter.class);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.createJson(marvelCharacter))
                .when()
                .put("/characters")
                .then()
                .extract().response();

        assertEquals(Status.NOT_FOUND.getStatusCode(), responseUpdate.statusCode());
    }

    @Test
    @Order(4)
    @DisplayName("4 - Excluindo Personagem")
    public void testDeleteCharacter() {
        MarvelCharacter marvelCharacter = createCharacter(CHARACTER_1_JSON);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("id", marvelCharacter.getId())
                .delete("/characters/{id}")
                .then()
                .extract().response();

        assertEquals(Status.NO_CONTENT.getStatusCode(), responseUpdate.statusCode());

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("name", marvelCharacter.getName())
                .get("/characters/local")
                .then()
                .extract().response();

        assertEquals(Status.NOT_FOUND.getStatusCode(), responseSearch.statusCode());
    }

    @Test
    @Order(5)
    @DisplayName("5 - Excluindo Personagem Não Existe")
    public void testDeleteCharacterNotFound() {
        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/characters/42")
                .then()
                .extract().response();

        assertEquals(Status.NOT_FOUND.getStatusCode(), responseUpdate.statusCode());
    }

    @Test
    @Order(6)
    @SuppressWarnings("rawtypes")
    @DisplayName("6 - Pesquisando Personagem Por Nome")
    public void testFindByName() {
        MarvelCharacter marvelCharacter = createCharacter(CHARACTER_1_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("name", marvelCharacter.getName())
                .get("/characters/local")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseSearch.statusCode());
        List<String> name = responseSearch.jsonPath().getList("name");
        List<ArrayList> urls = responseSearch.jsonPath().getList("urls");

        assertEquals("Thor", name.get(0));
        assertEquals(3, urls.get(0).size());
    }

    @Test
    @Order(7)
    @SuppressWarnings("rawtypes")
    @DisplayName("7 - Pesquisando Personagem Por Inicio do Nome")
    public void testFindByNameStartsWith() {
        createCharacter(CHARACTER_1_JSON);
        createCharacter(CHARACTER_2_JSON);
        createCharacter(CHARACTER_3_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("nameStartsWith", "Thor")
                .get("/characters/local")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseSearch.statusCode());
        List<String> name = responseSearch.jsonPath().getList("name");
        List<ArrayList> urls = responseSearch.jsonPath().getList("urls");

        assertEquals(1, name.stream().filter(p -> p.equals("Thor")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Goddess of Thunder)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (MAA)")).count());
        assertEquals(3, urls.size());
    }

    private MarvelCharacter createCharacter(String file) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(file))
                .when()
                .post("/characters")
                .then()
                .extract().response();

        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(response.body().print(), MarvelCharacter.class);

        assertNotNull(marvelCharacter);
        assertNotNull(marvelCharacter.getId());
        assertNotNull(marvelCharacter.getThumbnail());
        assertEquals(3, marvelCharacter.getUrlCharacters().size());
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());

        return marvelCharacter;
    }

}