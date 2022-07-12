package br.com.marvel.adapters.outbound.restclient;

import br.com.marvel.application.ports.out.CharacterApiClientPort;
import br.com.marvel.client.MarvelApiClient;
import br.com.marvel.client.dto.InlineResponse200;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class CharacterApiClientImpl implements CharacterApiClientPort {

    @Inject
    @RestClient
    MarvelApiClient marvelApiClient;

    @Override
    public InlineResponse200 listCharacters(String ts,
                                            String apikey,
                                            String hash,
                                            String name,
                                            String nameStartsWith,
                                            String modifiedSince,
                                            BigDecimal comics,
                                            BigDecimal series,
                                            BigDecimal events,
                                            BigDecimal stories,
                                            String orderBy,
                                            BigDecimal limit,
                                            BigDecimal offset) {
        return marvelApiClient.listCharacters(ts,
                apikey,
                hash,
                name,
                nameStartsWith,
                modifiedSince,
                comics,
                series,
                events,
                stories,
                orderBy,
                limit,
                offset);
    }
}
