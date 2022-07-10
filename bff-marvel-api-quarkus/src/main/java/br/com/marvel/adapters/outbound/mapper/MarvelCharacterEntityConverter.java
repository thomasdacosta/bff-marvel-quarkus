package br.com.marvel.adapters.outbound.mapper;

import br.com.marvel.adapters.outbound.entity.MarvelCharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.MarvelCharacter;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.dto.characters.UrlCharacter;

import java.util.Date;

public class MarvelCharacterEntityConverter {

    /**
     * Criando novos objetos
     */
    public static MarvelCharacterEntity toMarvelCharacterEntity(MarvelCharacter marvelCharacter) {
        MarvelCharacterEntity marvelCharacterEntity = new MarvelCharacterEntity();
        marvelCharacterEntity.setDescription(marvelCharacter.getDescription());
        marvelCharacterEntity.setName(marvelCharacter.getName());
        marvelCharacterEntity.setDateModified(new Date());
        marvelCharacterEntity.setDateCreated(new Date());
        return marvelCharacterEntity;
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
    public static MarvelCharacterEntity toMarvelCharacterEntity(MarvelCharacterEntity marvelCharacterEntity, MarvelCharacter marvelCharacter) {
        marvelCharacterEntity.setDescription(marvelCharacter.getDescription());
        marvelCharacterEntity.setName(marvelCharacter.getName());
        marvelCharacterEntity.setDateModified(new Date());
        return marvelCharacterEntity;
    }

    public static ThumbnailCharacterEntity toThumbnailCharacterEntity(ThumbnailCharacterEntity thumbnailCharacterEntity,
                                                                      ThumbnailCharacter thumbnailCharacter) {
        thumbnailCharacterEntity.setExtension(thumbnailCharacter.getExtension());
        thumbnailCharacterEntity.setUrl(thumbnailCharacter.getUrl());
        return thumbnailCharacterEntity;
    }

}
