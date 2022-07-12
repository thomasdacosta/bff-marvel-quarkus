package br.com.marvel.utils;

import br.com.marvel.application.utils.Constants;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WireMockServers {

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.ts")
    String ts;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.apiKey")
    String apiKey;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.hash")
    String hash;

    static WireMockServer wireMockServer = new WireMockServer(8085);

    public static void start() {
        wireMockServer.start();
    }

    public static void stop() {
        wireMockServer.start();
    }

    public void serverCharacterName(String file) {
        wireMockServer.stubFor(WireMock
                .get(String.format("/v1/public/characters?ts=%s&apikey=%s&hash=%s&name=%s&limit=10&offset=0", ts,
                        apiKey, hash, Constants.CHARACTER_NAME))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(ResourceLoader.loadFile(file))));
    }

    public void serverCharacterNameStartsWith(String file) {
        wireMockServer.stubFor(WireMock
                .get(String.format("/v1/public/characters?ts=%s&apikey=%s&hash=%s&nameStartsWith=%s&limit=10&offset=0", ts,
                        apiKey, hash, Constants.CHARACTER_NAME))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(ResourceLoader.loadFile(file))));
    }

}
