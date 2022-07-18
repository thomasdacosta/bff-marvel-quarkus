package br.com.marvel.adapters.outbound.mapper;

import br.com.marvel.adapters.outbound.entity.CharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.dto.characters.UrlCharacter;

import java.util.Date;

// TODO mapstruct
public class CharacterEntityConverter {

    /**
     * Criando novos objetos
     */
    public static CharacterEntity toMarvelCharacterEntity(Character character) {
        CharacterEntity characterEntity = new CharacterEntity();
        characterEntity.setDescription(character.getDescription());
        characterEntity.setName(character.getName());
        characterEntity.setDateModified(new Date());
        characterEntity.setDateCreated(new Date());
        return characterEntity;
    }

    public static ThumbnailCharacterEntity toThumbnailCharacterEntity(ThumbnailCharacter thumbnailCharacter) {
        ThumbnailCharacterEntity thumbnailCharacterEntity = new ThumbnailCharacterEntity();
        thumbnailCharacterEntity.setExtension(thumbnailCharacter.getExtension());
        thumbnailCharacterEntity.setUrl(thumbnailCharacter.getUrl());
        thumbnailCharacterEntity.persist();
        return thumbnailCharacterEntity;
    }

    public static UrlCharacterEntity toUrlCharacterEntity(UrlCharacter urlCharacter) {
        UrlCharacterEntity urlCharacterEntity = new UrlCharacterEntity();
        urlCharacterEntity.setUrl(urlCharacter.getUrl());
        urlCharacterEntity.setType(urlCharacter.getType());
        return urlCharacterEntity;
    }

    /**
     * Atualizando os objetos
     */
    public static CharacterEntity toMarvelCharacterEntity(CharacterEntity characterEntity, Character character) {
        characterEntity.setDescription(character.getDescription());
        characterEntity.setName(character.getName());
        characterEntity.setDateModified(new Date());
        return characterEntity;
    }

    public static ThumbnailCharacterEntity toThumbnailCharacterEntity(ThumbnailCharacterEntity thumbnailCharacterEntity,
                                                                      ThumbnailCharacter thumbnailCharacter) {
        thumbnailCharacterEntity.setExtension(thumbnailCharacter.getExtension());
        thumbnailCharacterEntity.setUrl(thumbnailCharacter.getUrl());
        return thumbnailCharacterEntity;
    }

}
