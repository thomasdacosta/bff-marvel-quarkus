package br.com.marvel.application.core.service;

import br.com.marvel.application.ports.in.MarvelCharacterServicePort;
import br.com.marvel.application.ports.out.MarvelApiClientPort;
import br.com.marvel.application.ports.out.MarvelCharacterRepositoryPort;
import br.com.marvel.client.MarvelApiClient;
import br.com.marvel.client.dto.InlineResponse200;
import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.dto.characters.UrlCharacter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Counted
@Traced
@ApplicationScoped
public class MarvelCharacterServiceImpl implements MarvelCharacterServicePort {

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.ts")
    String ts;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.apiKey")
    String apiKey;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.hash")
    String hash;

    @Inject
    MarvelApiClientPort marvelApiClient;

    @Inject
    MarvelCharacterRepositoryPort marvelCharacterRepository;

    @Override
    public MarvelCharacter save(MarvelCharacter marvelCharacter) {
        return marvelCharacterRepository.save(marvelCharacter);
    }

    @Override
    public MarvelCharacter update(MarvelCharacter marvelCharacter) {
        return marvelCharacterRepository.update(marvelCharacter);
    }

    @Override
    public void delete(Long id) {
        marvelCharacterRepository.delete(id);
    }

    @Override
    public Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit) {
        List<MarvelCharacter> marvelCharacters;

        if (!StringUtils.isEmpty(name))
            marvelCharacters = marvelCharacterRepository.findByName(name, offset.intValue(), limit.intValue());
        else if (!StringUtils.isEmpty(nameStartsWith)) {
            marvelCharacters = marvelCharacterRepository.findByNameStartsWith(nameStartsWith, offset.intValue(), limit.intValue());
        } else {
            marvelCharacters = marvelCharacterRepository.findAll(offset.intValue(), limit.intValue());
        }

        if (!marvelCharacters.isEmpty()) {
            Pagination pagination = new Pagination();
            pagination.setData(marvelCharacters);
            pagination.setLimit(limit);
            pagination.setOffset(offset);
            pagination.setTotal(BigDecimal.valueOf(marvelCharacters.size()));
            pagination.setCount(BigDecimal.valueOf(marvelCharacters.size()));
            return pagination;
        } else {
            return null;
        }
    }

    @Override
    public Pagination findCharactersApi(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit) {
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
