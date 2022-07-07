package br.com.marvel.repository;

import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.dto.characters.ThumbnailCharacter;
import br.com.marvel.repository.entity.MarvelCharacterEntity;
import br.com.marvel.repository.entity.ThumbnailCharacterEntity;
import br.com.marvel.repository.entity.UrlCharacterEntity;
import br.com.marvel.utils.MarvelCharacterConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.*;

@ApplicationScoped
public class MarvelCharacterRepository {

    @Transactional
    public MarvelCharacter save(MarvelCharacter marvelCharacter) {
        ThumbnailCharacterEntity thumbnailCharacterEntity = new ThumbnailCharacterEntity();
        ThumbnailCharacter thumbnailCharacter = marvelCharacter.getThumbnail();
        thumbnailCharacterEntity.setExtension(thumbnailCharacter.getExtension());
        thumbnailCharacterEntity.setUrl(thumbnailCharacter.getUrl());
        thumbnailCharacterEntity.setDateModified(new Date());
        thumbnailCharacterEntity.setDateCreated(new Date());
        thumbnailCharacterEntity.persist();

        MarvelCharacterEntity marvelCharacterEntity = new MarvelCharacterEntity();
        marvelCharacterEntity.setDateCreated(new Date());
        marvelCharacterEntity.setDateModified(new Date());
        marvelCharacterEntity.setDescription(marvelCharacter.getDescription());
        marvelCharacterEntity.setName(marvelCharacter.getName());
        marvelCharacterEntity.setThumbnail(thumbnailCharacterEntity);

        Set<UrlCharacterEntity> urlCharacterEntities = new LinkedHashSet<>();
        marvelCharacter.getUrlCharacters().forEach(p -> {
            UrlCharacterEntity urlCharacterEntity = new UrlCharacterEntity();
            urlCharacterEntity.setUrl(p.getUrl());
            urlCharacterEntity.setType(p.getType());
            urlCharacterEntity.setDateCreated(new Date());
            urlCharacterEntity.persist();
            urlCharacterEntities.add(urlCharacterEntity);
        });

        marvelCharacterEntity.setUrlCharacterEntities(urlCharacterEntities);
        marvelCharacterEntity.persist();

        return MarvelCharacterConverter.toMarvelCharacter(marvelCharacterEntity);
    }

    public List<MarvelCharacter> findAll(Integer offset, Integer limit) {
        PanacheQuery<MarvelCharacterEntity> marvelCharacterEntityQuery = MarvelCharacterEntity.findAll();
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    public List<MarvelCharacter> findByName(String name, Integer offset, Integer limit) {
        PanacheQuery<MarvelCharacterEntity> marvelCharacterEntityQuery = MarvelCharacterEntity
                .find("lower(name)", name.toLowerCase());
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    public List<MarvelCharacter> findByNameStartsWith(String name, Integer offset, Integer limit) {
        PanacheQuery<MarvelCharacterEntity> marvelCharacterEntityQuery = MarvelCharacterEntity
                .find("lower(name) like ?1", name.toLowerCase() + "%");
        return toListMarvelCharacter(marvelCharacterEntityQuery, offset, limit);
    }

    private List<MarvelCharacter> toListMarvelCharacter(PanacheQuery<MarvelCharacterEntity> marvelCharacterEntityQuery,
                                                        Integer offset, Integer limit) {
        List<MarvelCharacterEntity> marvelCharacterEntities = marvelCharacterEntityQuery.range(offset,limit).list();
        List<MarvelCharacter> list = new ArrayList<>();
        for (MarvelCharacterEntity p : marvelCharacterEntities) {
            MarvelCharacter marvelCharacter = MarvelCharacterConverter.toMarvelCharacter(p);
            list.add(marvelCharacter);
        }
        return list;
    }

}
