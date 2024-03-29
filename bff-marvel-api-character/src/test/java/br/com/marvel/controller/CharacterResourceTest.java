package br.com.marvel.controller;

import br.com.marvel.adapters.outbound.entity.CharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.application.utils.Constants;
import br.com.marvel.application.utils.JsonUtils;
import br.com.marvel.controller.exception.dto.ApiMessageStatus;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class CharacterResourceTest {

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
        CharacterEntity.deleteAll();
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
        Character character = createCharacter(Constants.CHARACTER_3_JSON);
        character.setName(Constants.CHARACTER_UPDATE_NAME);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.createJson(character))
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
        Character character = (Character) JsonUtils
                .createObject(ResourceLoader.loadFile(Constants.CHARACTER_1_JSON), Character.class);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .body(JsonUtils.createJson(character))
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
        Character character = createCharacter(Constants.CHARACTER_1_JSON);

        Response responseUpdate = given()
                .contentType(ContentType.JSON)
                .when()
                .pathParam("id", character.getId())
                .delete("/characters/{id}")
                .then()
                .extract().response();

        assertEquals(Status.NO_CONTENT.getStatusCode(), responseUpdate.statusCode());

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("name", character.getName())
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
        Character character = createCharacter(Constants.CHARACTER_1_JSON);

        Response responseSearch = given()
                .contentType(ContentType.JSON)
                .when()
                .param("name", character.getName())
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

    @Test
    @Order(11)
    @DisplayName("11 - Validando os campos de entrada estão Vazio ou Nulo")
    public void testValidateEmptyFields() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(Constants.CHARACTER_VALIDATE_EMPTY_FIELDS))
                .when()
                .post("/characters")
                .then()
                .extract().response();

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.statusCode());

        ApiMessageStatus apiMessageStatus = (ApiMessageStatus) JsonUtils
                .createObject(response.body().prettyPrint(), ApiMessageStatus.class);
        String[] errors = apiMessageStatus.getDetail().split(",");

        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.description: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.description: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.name: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.name: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.extension: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.extension: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.url: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.url: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].type: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].type: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].url: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].url: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].type: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].type: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].url: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].url: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].type: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].type: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].url: não deve estar em branco")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].url: não deve estar vazio")).count());
    }

    @Test
    @Order(12)
    @DisplayName("12 - Validando os campos de entrada Thumbnail e Urls")
    public void testValidateThumbnailUrlsFields() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(Constants.CHARACTER_VALIDATE_THUMBNAIL_URLS_FIELDS))
                .when()
                .post("/characters")
                .then()
                .extract().response();

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.statusCode());

        ApiMessageStatus apiMessageStatus = (ApiMessageStatus) JsonUtils
                .createObject(response.body().prettyPrint(), ApiMessageStatus.class);
        String[] errors = apiMessageStatus.getDetail().split(",");

        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters: não deve estar vazio")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail: não deve ser nulo")).count());
    }

    @Test
    @Order(13)
    @DisplayName("13 - Validando os tamanhos dos campos de entrada")
    public void testValidateSizeFields() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(Constants.CHARACTER_VALIDATE_SIZE_FIELDS))
                .when()
                .post("/characters")
                .then()
                .extract().response();

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.statusCode());

        ApiMessageStatus apiMessageStatus = (ApiMessageStatus) JsonUtils
                .createObject(response.body().prettyPrint(), ApiMessageStatus.class);
        String[] errors = apiMessageStatus.getDetail().split(",");

        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.url: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].url: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].type: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[2].url: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].type: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[1].url: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.name: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.description: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.thumbnail.extension: tamanho deve ser entre 0 e 255")).count());
        assertEquals(1, Arrays.stream(errors).filter(p -> p.trim().equals("saveCharacters.character.urlCharacters[0].type: tamanho deve ser entre 0 e 255")).count());
    }

    private Character createCharacter(String file) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(ResourceLoader.loadFile(file))
                .when()
                .post("/characters")
                .then()
                .extract().response();

        Character character = (Character) JsonUtils
                .createObject(response.body().print(), Character.class);

        assertNotNull(character);
        assertNotNull(character.getId());
        assertNotNull(character.getThumbnail());
        assertEquals(3, character.getUrlCharacters().size());
        assertEquals(Status.CREATED.getStatusCode(), response.statusCode());

        return character;
    }

}