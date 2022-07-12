package br.com.marvel.adapters.outbound.repository;

import br.com.marvel.application.ports.out.CharacterRepositoryPort;
import br.com.marvel.adapters.outbound.entity.CharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.exception.core.CharactersNotFoundException;
import br.com.marvel.adapters.inbound.mapper.CharacterConverter;
import br.com.marvel.adapters.outbound.mapper.CharacterEntityConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Counted
@Traced
@ApplicationScoped
public class CharacterRepository implements CharacterRepositoryPort {

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

    private List<Character> toListMarvelCharacter(PanacheQuery<CharacterEntity> marvelCharacterEntityQuery,
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
