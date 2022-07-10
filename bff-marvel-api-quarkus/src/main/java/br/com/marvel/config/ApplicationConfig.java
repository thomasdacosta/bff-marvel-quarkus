package br.com.marvel.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApplicationConfig {

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.ts")
    String ts;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.apiKey")
    String apiKey;

    @ConfigProperty(name = "quarkus.rest-client.marvel-api.hash")
    String hash;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}
