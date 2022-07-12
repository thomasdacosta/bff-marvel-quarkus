package br.com.marvel.application.core.service;

import br.com.marvel.adapters.inbound.mapper.CharacterConverter;
import br.com.marvel.adapters.outbound.entity.CharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.adapters.outbound.mapper.CharacterEntityConverter;
import br.com.marvel.application.ports.in.CharacterServicePort;
import br.com.marvel.application.ports.out.CharacterApiClientPort;
import br.com.marvel.client.dto.InlineResponse200;
import br.com.marvel.config.ApplicationConfig;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.dto.characters.UrlCharacter;
import br.com.marvel.controller.dto.pagination.Pagination;
import br.com.marvel.controller.exception.core.CharactersNotFoundException;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Counted
@Traced
@ApplicationScoped
public class CharacterServiceImpl implements CharacterServicePort {

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    CharacterApiClientPort characterApiClientPort;

    @Override
    @Transactional
    public Character save(Character character) {
        ThumbnailCharacterEntity thumbnailCharacterEntity = CharacterEntityConverter
                .toThumbnailCharacterEntity(character.getThumbnail());

        Set<UrlCharacterEntity> urlCharacterEntities = new LinkedHashSet<>();
        character.getUrlCharacters().forEach(p -> {
            UrlCharacterEntity urlCharacterEntity = CharacterEntityConverter.toUrlCharacterEntity(p);
            urlCharacterEntity.persist();
            urlCharacterEntities.add(urlCharacterEntity);
        });

        CharacterEntity characterEntity = CharacterEntityConverter.toMarvelCharacterEntity(character);
        characterEntity.setThumbnail(thumbnailCharacterEntity);
        characterEntity.setUrlCharacterEntities(urlCharacterEntities);
        characterEntity.persist();

        return CharacterConverter.toMarvelCharacter(characterEntity);
    }

    @Override
    @Transactional
    public Character update(Character character) {
        Optional<CharacterEntity> optionalMarvelCharacterEntity = CharacterEntity.findByIdOptional(character.getId().longValue());
        if (optionalMarvelCharacterEntity.isEmpty()) {
            throw new CharactersNotFoundException("Personagem não encontrado");
        }

        CharacterEntity characterEntity = optionalMarvelCharacterEntity.get();
        UrlCharacterEntity.delete("id_character", character.getId());

        Set<UrlCharacterEntity> urlCharacterEntities = character.getUrlCharacters().stream()
                .map(p -> {
                    UrlCharacterEntity urlCharacterEntity = CharacterEntityConverter.toUrlCharacterEntity(p);
                    urlCharacterEntity.setId(null);
                    return urlCharacterEntity;
                }).collect(Collectors.toSet());
        UrlCharacterEntity.persist(urlCharacterEntities);

        ThumbnailCharacterEntity thumbnailCharacterEntity = CharacterEntityConverter
                .toThumbnailCharacterEntity(characterEntity.getThumbnail(), character.getThumbnail());
        ThumbnailCharacterEntity.persist(thumbnailCharacterEntity);

        CharacterEntity characterEntityUpdate = CharacterEntityConverter
                .toMarvelCharacterEntity(characterEntity, character);
        characterEntity.setThumbnail(thumbnailCharacterEntity);
        characterEntity.setUrlCharacterEntities(urlCharacterEntities);
        CharacterEntity.persist(characterEntityUpdate);

        return CharacterConverter.toMarvelCharacter(characterEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<CharacterEntity> optionalMarvelCharacterEntity = CharacterEntity.findByIdOptional(id);
        if (optionalMarvelCharacterEntity.isEmpty()) {
            throw new CharactersNotFoundException("Personagem não encontrado");
        }

        CharacterEntity characterEntity = optionalMarvelCharacterEntity.get();
        UrlCharacterEntity.delete("id_character", characterEntity.getId());
        CharacterEntity.delete("id", characterEntity.getId());
        ThumbnailCharacterEntity.delete("id", characterEntity.getThumbnail().getId());
    }

    @Override
    public List<Character> findAll(Integer offset, Integer limit) {
        PanacheQuery<CharacterEntity> marvelCharacterEntityQuery = CharacterEntity.findAll();
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    @Override
    public List<Character> findByName(String name, Integer offset, Integer limit) {
        PanacheQuery<CharacterEntity> marvelCharacterEntityQuery = CharacterEntity
                .find("lower(name)", name.toLowerCase());
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    @Override
    public List<Character> findByNameStartsWith(String name, Integer offset, Integer limit) {
        PanacheQuery<CharacterEntity> marvelCharacterEntityQuery = CharacterEntity
                .find("lower(name) like ?1", name.toLowerCase() + "%");
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    @Override
    public Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit) {
        List<Character> characters;

        if (!StringUtils.isEmpty(name))
            characters = findByName(name, offset.intValue(), limit.intValue());
        else if (!StringUtils.isEmpty(nameStartsWith)) {
            characters = findByNameStartsWith(nameStartsWith, offset.intValue(), limit.intValue());
        } else {
            characters = findAll(offset.intValue(), limit.intValue());
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

    public List<Character> toListMarvelCharacter(PanacheQuery<CharacterEntity> marvelCharacterEntityQuery,
                                                 Integer offset, Integer limit) {
        List<CharacterEntity> marvelCharacterEntities = marvelCharacterEntityQuery.range(offset,limit).list();
        List<Character> list = new ArrayList<>();
        for (CharacterEntity p : marvelCharacterEntities) {
            Character character = CharacterConverter.toMarvelCharacter(p);
            list.add(character);
        }
        return list;
    }

}
