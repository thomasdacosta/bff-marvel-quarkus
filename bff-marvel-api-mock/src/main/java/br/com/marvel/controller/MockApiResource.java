package br.com.marvel.controller;

import br.com.marvel.utils.ResourceLoader;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class MockApiResource {

    @Inject
    Logger LOGGER;

    @GET
    @Path("/v1/public/characters")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String mockApi() {
        LOGGER.info("MockApiResource#mock invocation...");
        return ResourceLoader.loadFile("character_simulate.json");
    }

}