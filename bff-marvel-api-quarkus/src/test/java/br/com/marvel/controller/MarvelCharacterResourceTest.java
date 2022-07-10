package br.com.marvel.controller;

import br.com.marvel.adapters.outbound.entity.MarvelCharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.MarvelCharacter;
import br.com.marvel.application.utils.Constants;
import br.com.marvel.application.utils.JsonUtils;
import br.com.marvel.utils.ResourceLoader;
import br.com.marvel.utils.WireMockServers;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import javax.inject.Inject;
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

    @Inject
    WireMockServers wireMockServers;

    @BeforeAll
    public static void beforeAll() {
        WireMockServers.start();
    }

    @AfterAll
    public static void afterAll() {
        WireMockServers.stop();
    }

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
        createCharacter(Constants.CHARACTER_2_JSON);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Atualizando Personagem")
    public void testUpdateCharacter() {
        MarvelCharacter marvelCharacter = createCharacter(Constants.CHARACTER_3_JSON);
        marvelCharacter.setName(Constants.CHARACTER_UPDATE_NAME);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.createJson(marvelCharacter))
                .when()
                .put("/characters")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseUpdate.statusCode());
        assertEquals(Constants.CHARACTER_UPDATE_NAME, responseUpdate.jsonPath().getString("name"));
        assertEquals(3, responseUpdate.jsonPath().getList("urls").size());
    }

    @Test
    @Order(3)
    @DisplayName("3 - Atualizando Personagem Não Existe")
    public void testUpdateCharacterNotFound() {
        MarvelCharacter marvelCharacter = (MarvelCharacter) JsonUtils
                .createObject(ResourceLoader.loadFile(Constants.CHARACTER_1_JSON), MarvelCharacter.class);

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
        MarvelCharacter marvelCharacter = createCharacter(Constants.CHARACTER_1_JSON);

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
    @DisplayName("6 - Pesquisando Personagem Por Nome (local)")
    public void testFindByNameLocal() {
        MarvelCharacter marvelCharacter = createCharacter(Constants.CHARACTER_1_JSON);

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

        assertEquals(Constants.CHARACTER_NAME, name.get(0).toLowerCase());
        assertEquals(3, urls.get(0).size());
    }

    @Test
    @Order(7)
    @SuppressWarnings("rawtypes")
    @DisplayName("7 - Pesquisando Personagem Por Inicio do Nome (local)")
    public void testFindByNameStartsWithLocal() {
        createCharacter(Constants.CHARACTER_1_JSON);
        createCharacter(Constants.CHARACTER_2_JSON);
        createCharacter(Constants.CHARACTER_3_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("nameStartsWith", Constants.CHARACTER_NAME)
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

    @Test
    @Order(8)
    @DisplayName("8 - Pesquisando Personagem Por Nome (api)")
    public void testFindByNameApi() {
        wireMockServers.serverCharacterName(Constants.CHARACTER_4_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("name", Constants.CHARACTER_NAME)
                .get("/characters/api")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseSearch.statusCode());
        List<String> name = responseSearch.jsonPath().getList("name");
        List<ArrayList> urls = responseSearch.jsonPath().getList("urls");

        assertEquals("Thor", name.get(0));
        assertEquals(3, urls.get(0).size());
    }

    @Test
    @Order(9)
    @SuppressWarnings("rawtypes")
    @DisplayName("9 - Pesquisando Personagem Por Inicio do Nome (api)")
    public void testFindByNameStartsWithApi() {
        wireMockServers.serverCharacterNameStartsWith(Constants.CHARACTER_5_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("nameStartsWith", Constants.CHARACTER_NAME)
                .get("/characters/api")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseSearch.statusCode());
        List<String> name = responseSearch.jsonPath().getList("name");
        List<ArrayList> urls = responseSearch.jsonPath().getList("urls");

        assertEquals(1, name.stream().filter(p -> p.equals("Thor")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Goddess of Thunder)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (MAA)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Marvel Heroes)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Marvel War of Heroes)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Marvel: Avengers Alliance)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor (Ultimate)")).count());
        assertEquals(1, name.stream().filter(p -> p.equals("Thor Girl")).count());
        assertEquals(8, urls.size());
    }

    @Test
    @Order(10)
    @SuppressWarnings("rawtypes")
    @DisplayName("10 - Pesquisando Todos os Personagem (local)")
    public void testFindAllLocal() {
        createCharacter(Constants.CHARACTER_1_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/characters/local")
                .then()
                .extract().response();

        assertEquals(Status.OK.getStatusCode(), responseSearch.statusCode());
        List<String> name = responseSearch.jsonPath().getList("name");
        List<ArrayList> urls = responseSearch.jsonPath().getList("urls");

        assertEquals(Constants.CHARACTER_NAME, name.get(0).toLowerCase());
        assertEquals(3, urls.get(0).size());
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