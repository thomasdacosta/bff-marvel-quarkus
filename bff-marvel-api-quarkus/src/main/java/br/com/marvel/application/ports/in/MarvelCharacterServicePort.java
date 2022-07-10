package br.com.marvel.application.ports.in;

import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;

import java.math.BigDecimal;

public interface MarvelCharacterServicePort {
    MarvelCharacter save(MarvelCharacter marvelCharacter);

    MarvelCharacter update(MarvelCharacter marvelCharacter);

    void delete(Long id);

    Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);

    Pagination findCharactersApi(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);
}
