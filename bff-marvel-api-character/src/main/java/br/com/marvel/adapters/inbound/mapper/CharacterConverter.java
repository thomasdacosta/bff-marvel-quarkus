package br.com.marvel.adapters.inbound.mapper;

import br.com.marvel.adapters.outbound.entity.CharacterEntity;
import br.com.marvel.adapters.outbound.entity.ThumbnailCharacterEntity;
import br.com.marvel.adapters.outbound.entity.UrlCharacterEntity;
import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.characters.ThumbnailCharacter;
import br.com.marvel.controller.dto.characters.UrlCharacter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

public class CharacterConverter {

    public static Character toMarvelCharacter(CharacterEntity characterEntity) {
        Character character = new Character();
        character.setId(BigDecimal.valueOf(characterEntity.getId()));
        character.setDescription(characterEntity.getDescription());
        character.setName(characterEntity.getName());
        character.setModified(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(characterEntity.getDateModified()));
        character.setThumbnail(toThumbnailCharacter(characterEntity.getThumbnail()));
        character.setUrlCharacters(characterEntity.getUrlCharacterEntities().stream().map(CharacterConverter::toUrlCharacter)
                .collect(Collectors.toList()));
        return character;
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
