package br.com.marvel.client;

import br.com.marvel.client.dto.InlineResponse200;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;

@Path("/")
@RegisterRestClient(configKey = "marvel-api")
public interface MarvelApiClient {

    @GET
    @Path("/characters")
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
