package br.com.marvel.wiremock.simulate;

import br.com.marvel.utils.ResourceLoader;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.Scanner;

public class MarvelApiWireMock {

    public static void main(String[] args) {
        WireMockServer wireMockServer = new WireMockServer(8085);
        wireMockServer.start();
        wireMockServer.stubFor(WireMock
                .get(String.format("/v1/public/characters?ts=%s&apikey=%s&hash=%s&name=%s&limit=10&offset=0", "1",
                        "f59dbe01285f1d360542b5c47a9516e3", "0ea6be79e04ac1b0400d65ffc11088f9", "thor"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(ResourceLoader.loadFile("character_simulate.json"))));

        Scanner scanner = new Scanner(System.in);
        Integer value = 0;
        while (value != 1) {
            System.out.println("Digite 1 para terminar o servidor:");
            value = scanner.nextInt();
        }

        System.out.println("Parando servidor");
        wireMockServer.stop();
        System.out.println("Servidor Parado");
        scanner.close();
    }

}
