package br.com.marvel.adapters.inbound.mapper;

import br.com.marvel.adapters.outbound.entity.MarvelCharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.resource.dto.characters.UrlCharacter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class MarvelCharacterConverter {

    public static MarvelCharacter toMarvelCharacter(MarvelCharacterEntity marvelCharacterEntity) {
        MarvelCharacter marvelCharacter = new MarvelCharacter();
        marvelCharacter.setId(BigDecimal.valueOf(marvelCharacterEntity.getId()));
        marvelCharacter.setDescription(marvelCharacterEntity.getDescription());
        marvelCharacter.setName(marvelCharacterEntity.getName());
        marvelCharacter.setModified(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(marvelCharacterEntity.getDateModified()));
        marvelCharacter.setThumbnail(toThumbnailCharacter(marvelCharacterEntity.getThumbnail()));
        marvelCharacter.setUrlCharacters(marvelCharacterEntity.getUrlCharacterEntities().stream().map(MarvelCharacterConverter::toUrlCharacter)
                .collect(Collectors.toList()));
        return marvelCharacter;
    }

    public static ThumbnailCharacter toThumbnailCharacter(ThumbnailCharacterEntity thumbnailCharacterEntity) {
        ThumbnailCharacter thumbnailCharacter = new ThumbnailCharacter();
        thumbnailCharacter.setId(BigDecimal.valueOf(thumbnailCharacterEntity.getId()));
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
