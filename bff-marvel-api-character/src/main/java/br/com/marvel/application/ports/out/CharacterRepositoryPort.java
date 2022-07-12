package br.com.marvel.application.ports.out;

import br.com.marvel.controller.dto.characters.Character;

import javax.transaction.Transactional;
import java.util.List;

public interface CharacterRepositoryPort {
    @Transactional
    Character save(Character character);

    @Transactional
    Character update(Character character);

    @Transactional
    void delete(Long id);

    List<Character> findAll(Integer offset, Integer limit);

    List<Character> findByName(String name, Integer offset, Integer limit);

    List<Character> findByNameStartsWith(String name, Integer offset, Integer limit);
}
