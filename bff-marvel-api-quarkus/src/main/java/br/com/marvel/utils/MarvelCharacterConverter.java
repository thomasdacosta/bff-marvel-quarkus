package br.com.marvel.utils;

import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.dto.characters.UrlCharacter;
import br.com.marvel.repository.entity.MarvelCharacterEntity;
import br.com.marvel.repository.entity.ThumbnailCharacterEntity;
import br.com.marvel.repository.entity.UrlCharacterEntity;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class MarvelCharacterConverter {

    public static MarvelCharacter toMarvelCharacter(MarvelCharacterEntity marvelCharacterEntity) {
        MarvelCharacter marvelCharacter = new MarvelCharacter();
        marvelCharacter.setDescription(marvelCharacterEntity.getDescription());
        marvelCharacter.setName(marvelCharacterEntity.getName());
        marvelCharacter.setId(BigDecimal.valueOf(marvelCharacterEntity.getId()));
        marvelCharacter.setModified(marvelCharacterEntity.getDateModified().toString());
        marvelCharacter.setThumbnail(toThumbnailCharacter(marvelCharacterEntity.getThumbnail()));
        marvelCharacter.setUrlCharacters(marvelCharacterEntity.getUrlCharacterEntities().stream().map(MarvelCharacterConverter::toUrlCharacter)
                .collect(Collectors.toList()));

        return marvelCharacter;
    }

    public static ThumbnailCharacter toThumbnailCharacter(ThumbnailCharacterEntity thumbnailCharacterEntity) {
        ThumbnailCharacter thumbnailCharacter = new ThumbnailCharacter();
        thumbnailCharacter.setExtension(thumbnailCharacterEntity.getExtension());
        thumbnailCharacter.setUrl(thumbnailCharacterEntity.getUrl());
        return thumbnailCharacter;
    }

    public static UrlCharacter toUrlCharacter(UrlCharacterEntity urlCharacterEntity) {
        UrlCharacter urlCharacter = new UrlCharacter();
        urlCharacter.setUrl(urlCharacterEntity.getUrl());
        urlCharacter.setType(urlCharacterEntity.getType());
        return urlCharacter;
    }

}
