package br.com.marvel.repository;

import br.com.marvel.repository.entity.MarvelCharacterEntity;
import br.com.marvel.repository.entity.ThumbnailCharacterEntity;
import br.com.marvel.repository.entity.UrlCharacterEntity;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.exception.CharactersNotFoundException;
import br.com.marvel.utils.MarvelCharacterConverter;
import br.com.marvel.utils.MarvelCharacterEntityConverter;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Traced
@ApplicationScoped
public class MarvelCharacterRepository {

    @Transactional
    public MarvelCharacter save(MarvelCharacter marvelCharacter) {
        ThumbnailCharacterEntity thumbnailCharacterEntity = MarvelCharacterEntityConverter
                .toThumbnailCharacterEntity(marvelCharacter.getThumbnail());

        Set<UrlCharacterEntity> urlCharacterEntities = new LinkedHashSet<>();
        marvelCharacter.getUrlCharacters().forEach(p -> {
            UrlCharacterEntity urlCharacterEntity = MarvelCharacterEntityConverter.toUrlCharacterEntity(p);
            urlCharacterEntity.persist();
            urlCharacterEntities.add(urlCharacterEntity);
        });

        MarvelCharacterEntity marvelCharacterEntity = MarvelCharacterEntityConverter.toMarvelCharacterEntity(marvelCharacter);
        marvelCharacterEntity.setThumbnail(thumbnailCharacterEntity);
        marvelCharacterEntity.setUrlCharacterEntities(urlCharacterEntities);
        marvelCharacterEntity.persist();

        return MarvelCharacterConverter.toMarvelCharacter(marvelCharacterEntity);
    }

    @Transactional
    public MarvelCharacter update(MarvelCharacter marvelCharacter) {
        Optional<MarvelCharacterEntity> optionalMarvelCharacterEntity = MarvelCharacterEntity.findByIdOptional(marvelCharacter.getId().longValue());
        if (optionalMarvelCharacterEntity.isEmpty()) {
            throw new CharactersNotFoundException("Personagem não encontrado");
        }

        MarvelCharacterEntity marvelCharacterEntity = optionalMarvelCharacterEntity.get();
        UrlCharacterEntity.delete("id_marvel_character", marvelCharacter.getId());

        Set<UrlCharacterEntity> urlCharacterEntities = marvelCharacter.getUrlCharacters().stream()
                .map(p -> {
                    UrlCharacterEntity urlCharacterEntity = MarvelCharacterEntityConverter.toUrlCharacterEntity(p);
                    urlCharacterEntity.setId(null);
                    return urlCharacterEntity;
                }).collect(Collectors.toSet());
        UrlCharacterEntity.persist(urlCharacterEntities);

        ThumbnailCharacterEntity thumbnailCharacterEntity = MarvelCharacterEntityConverter
                .toThumbnailCharacterEntity(marvelCharacterEntity.getThumbnail(), marvelCharacter.getThumbnail());
        ThumbnailCharacterEntity.persist(thumbnailCharacterEntity);

        MarvelCharacterEntity marvelCharacterEntityUpdate = MarvelCharacterEntityConverter
                .toMarvelCharacterEntity(marvelCharacterEntity, marvelCharacter);
        marvelCharacterEntity.setThumbnail(thumbnailCharacterEntity);
        marvelCharacterEntity.setUrlCharacterEntities(urlCharacterEntities);
        MarvelCharacterEntity.persist(marvelCharacterEntityUpdate);

        return MarvelCharacterConverter.toMarvelCharacter(marvelCharacterEntity);
    }

    @Transactional
    public void delete(Long id) {
        Optional<MarvelCharacterEntity> optionalMarvelCharacterEntity = MarvelCharacterEntity.findByIdOptional(id);
        if (optionalMarvelCharacterEntity.isEmpty()) {
            throw new CharactersNotFoundException("Personagem não encontrado");
        }

        MarvelCharacterEntity marvelCharacterEntity = optionalMarvelCharacterEntity.get();
        UrlCharacterEntity.delete("id_marvel_character", marvelCharacterEntity.getId());
        MarvelCharacterEntity.delete("id", marvelCharacterEntity.getId());
        ThumbnailCharacterEntity.delete("id", marvelCharacterEntity.getThumbnail().getId());
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
