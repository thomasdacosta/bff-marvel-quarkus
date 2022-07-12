package br.com.marvel.application.ports.out;

import br.com.marvel.client.dto.InlineResponse200;

import javax.ws.rs.QueryParam;
import java.math.BigDecimal;

public interface CharacterApiClientPort {

    InlineResponse200 listCharacters(@QueryParam("ts") String ts,
                                     @QueryParam("apikey") String apikey,
                                     @QueryParam("hash") String hash,
                                     @QueryParam("name") String name,
                                     @QueryParam("nameStartsWith") String nameStartsWith,
                                     @QueryParam("modifiedSince") String modifiedSince,
                                     @QueryParam("comics") BigDecimal comics,
                                     @QueryParam("series") BigDecimal series,
                                     @QueryParam("events") BigDecimal events,
                                     @QueryParam("stories") BigDecimal stories,
                                     @QueryParam("orderBy") String orderBy,
                                     @QueryParam("limit") BigDecimal limit,
                                     @QueryParam("offset") BigDecimal offset);

}
