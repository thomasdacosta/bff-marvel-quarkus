package br.com.marvel.application.ports.in;

import br.com.marvel.controller.dto.characters.Character;
import br.com.marvel.controller.dto.pagination.Pagination;

import java.math.BigDecimal;
import java.util.List;

public interface CharacterServicePort {
    Character save(Character character);

    Character update(Character character);

    void delete(Long id);

    Pagination findCharactersLocal(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);

    List<Character> findAll(Integer offset, Integer limit);

    List<Character> findByNameStartsWith(String name, Integer offset, Integer limit);

    List<Character> findByName(String name, Integer offset, Integer limit);

    Pagination findCharactersApi(String name, String nameStartsWith, BigDecimal offset, BigDecimal limit);
}
