package br.com.marvel.application.core.service;

import br.com.marvel.application.ports.in.CharacterServicePort;
import br.com.marvel.application.ports.out.CharacterApiClientPort;
import br.com.marvel.application.ports.out.CharacterRepositoryPort;
import br.com.marvel.client.dto.InlineResponse200;
import br.com.marvel.config.ApplicationConfig;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.pagination.Pagination;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.dto.characters.UrlCharacter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Counted
@Traced
@ApplicationScoped
public class CharacterServiceImpl implements CharacterServicePort {

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    CharacterApiClientPort characterApiClientPort;

    @Inject
    CharacterRepositoryPort characterRepositoryPort;

    @Override
    public Character save(Character character) {
        return characterRepositoryPort.save(character);
    }

    @Override
    public Character update(Character character) {
        return characterRepositoryPort.update(character);
    }

    @Override
    public void delete(Long id) {
        characterRepositoryPort.delete(id);
    }

    @Override
    public Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit) {
        List<Character> characters;

        if (!StringUtils.isEmpty(name))
            characters = characterRepositoryPort.findByName(name, offset.intValue(), limit.intValue());
        else if (!StringUtils.isEmpty(nameStartsWith)) {
            characters = characterRepositoryPort.findByNameStartsWith(nameStartsWith, offset.intValue(), limit.intValue());
        } else {
            characters = characterRepositoryPort.findAll(offset.intValue(), limit.intValue());
        }

        if (!characters.isEmpty()) {
            Pagination pagination = new Pagination();
            pagination.setData(characters);
            pagination.setLimit(limit);
            pagination.setOffset(offset);
            pagination.setTotal(BigDecimal.valueOf(characters.size()));
            pagination.setCount(BigDecimal.valueOf(characters.size()));
            return pagination;
        } else {
            return null;
        }
    }

    @Override
    public Pagination findCharactersApi(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit) {
        InlineResponse200 listCharacters = characterApiClientPort.listCharacters(applicationConfig.getTs(), applicationConfig.getApiKey(), applicationConfig.getHash(),
                name, nameStartsWith, null, null, null, null, null, null, limit, offset);

        Pagination pagination = listCharacters.getData();

        if (!listCharacters.getData().getResults().isEmpty()) {
            List<Character> characters = listCharacters.getData().getResults().stream().map(c -> {
                Character character = new Character();

                character.setId(c.getId());
                character.setName(c.getName());
                character.setDescription(c.getDescription());
                character.setModified(c.getModified());

                ThumbnailCharacter thumbnailCharacter = new ThumbnailCharacter();
                thumbnailCharacter.setUrl(c.getThumbnail().getPath());
                thumbnailCharacter.setExtension(c.getThumbnail().getExtension());

                character.setThumbnail(thumbnailCharacter);

                List<UrlCharacter> urlCharacters = c.getUrls().stream().map(u -> {
                    UrlCharacter urlCharacter = new UrlCharacter();
                    urlCharacter.setType(u.getType());
                    urlCharacter.setUrl(u.getUrl());
                    return urlCharacter;
                }).collect(Collectors.toList());

                character.setUrlCharacters(urlCharacters);

                return character;
            }).collect(Collectors.toList());

            pagination.setData(characters);
            return pagination;
        } else {
            return null;
        }

    }

}
