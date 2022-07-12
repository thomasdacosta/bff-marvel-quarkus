package br.com.marvel.application.ports.in;

import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.pagination.Pagination;

import java.math.BigDecimal;

public interface CharacterServicePort {
    Character save(Character character);

    Character update(Character character);

    void delete(Long id);

    Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);

    Pagination findCharactersApi(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);
}
