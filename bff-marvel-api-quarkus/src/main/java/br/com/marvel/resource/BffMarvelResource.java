package br.com.marvel.resource;

import br.com.marvel.resource.dto.Pagination;
import br.com.marvel.resource.dto.characters.MarvelCharacter;
import br.com.marvel.resource.exception.CharactersNotFoundException;
import br.com.marvel.service.MarvelCharacterService;
import br.com.marvel.utils.PaginationUtils;

import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BffMarvelResource {

    @Inject
    MarvelCharacterService marvelCharacterService;

    @GET
    @Path("/characters")
    public Response findCharacterse(@QueryParam("name") String name,
                                    @QueryParam("nameStartsWith") String nameStartsWith,
                                    @HeaderParam("offset") @DefaultValue("1") @Min(0) BigDecimal offset,
                                    @HeaderParam("limit") @DefaultValue("10") @Max(100) BigDecimal limit) {
        return response(marvelCharacterService.listCharacters(name, nameStartsWith, offset, limit));
    }

    // TODO validar os campos do JSON tipo
    @POST
    @Path("/characters")
    public Response saveCharacters(MarvelCharacter marvelCharacter) {
        return Response.ok(marvelCharacterService.save(marvelCharacter)).build();
    }

    @PUT
    @Path("/characters")
    public Response updateCharacters(MarvelCharacter marvelCharacter) {
        return null;
    }

    @DELETE
    @Path("/characters")
    public Response deleteCharacters(MarvelCharacter marvelCharacter) {
        return null;
    }

    private Response response(Pagination pagination) {
        if (pagination == null) {
            throw new CharactersNotFoundException("Personagem não encontrado. Deve ser da concorrente!!!");
        }
        return PaginationUtils.pagination(pagination);
    }

}
