package br.com.marvel.core.service;

import br.com.marvel.application.utils.Constants;
import br.com.marvel.utils.WireMockServers;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class MarvelCharacterServiceTest {

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

    @Test
    public void testHello() {
        wireMockServers.serverCharacterName(Constants.CHARACTER_1_JSON);
        assertTrue(true);
    }

}
