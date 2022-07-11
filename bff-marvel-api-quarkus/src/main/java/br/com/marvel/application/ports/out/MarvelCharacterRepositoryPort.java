package br.com.marvel.application.ports.out;

import br.com.marvel.controller.dto.characters.MarvelCharacter;

import javax.transaction.Transactional;
import java.util.List;

public interface MarvelCharacterRepositoryPort {
    @Transactional
    MarvelCharacter save(MarvelCharacter marvelCharacter);

    @Transactional
    MarvelCharacter update(MarvelCharacter marvelCharacter);

    @Transactional
    void delete(Long id);

    List<MarvelCharacter> findAll(Integer offset, Integer limit);

    List<MarvelCharacter> findByName(String name, Integer offset, Integer limit);

    List<MarvelCharacter> findByNameStartsWith(String name, Integer offset, Integer limit);
}
