package br.com.marvel.client.impl;

import br.com.marvel.client.dto.InlineResponse200;
import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.dto.characters.UrlCharacter;
import br.com.marvel.client.MarvelApiClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MarvelApiClientImpl {

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.ts")
    String ts;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.apiKey")
    String apiKey;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.hash")
    String hash;

    @Inject
    @RestClient
    MarvelApiClient marvelApiClient;

    public Pagination listCharacters(String name, String nameStartsWith, BigDecimal limit, BigDecimal offset) {
        InlineResponse200 listCharacters = marvelApiClient.listCharacters(ts, apiKey, hash, name, nameStartsWith, null, null, null,
                null, null, null, limit, offset);

        Pagination pagination = listCharacters.getData();

        if (!listCharacters.getData().getResults().isEmpty()) {
            List<MarvelCharacter> characters = listCharacters.getData().getResults().stream().map(c -> {
                MarvelCharacter marvelCharacter = new MarvelCharacter();

                marvelCharacter.setId(c.getId());
                marvelCharacter.setName(c.getName());
                marvelCharacter.setDescription(c.getDescription());
                marvelCharacter.setModified(c.getModified());

                ThumbnailCharacter thumbnailCharacter = new ThumbnailCharacter();
                thumbnailCharacter.setUrl(c.getThumbnail().getPath());
                thumbnailCharacter.setExtension(c.getThumbnail().getExtension());

                marvelCharacter.setThumbnail(thumbnailCharacter);

                List<UrlCharacter> urlCharacters = c.getUrls().stream().map(u -> {
                    UrlCharacter urlCharacter = new UrlCharacter();
                    urlCharacter.setType(u.getType());
                    urlCharacter.setUrl(u.getUrl());
                    return urlCharacter;
                }).collect(Collectors.toList());

                marvelCharacter.setUrlCharacters(urlCharacters);

                return marvelCharacter;
            }).collect(Collectors.toList());

            pagination.setData(characters);
            return pagination;
        } else {
            return null;
        }
    }

}
