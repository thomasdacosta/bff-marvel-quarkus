package br.com.marvel.application.health;

import br.com.marvel.application.ports.out.MarvelApiClientPort;
import br.com.marvel.client.dto.InlineResponse200;
import br.com.marvel.config.ApplicationConfig;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.inject.Inject;
import java.math.BigDecimal;

@Readiness
public class MarvelApiHealth implements HealthCheck {

    private static final String LABEL = "Marvel API Client";

    @Inject
    ApplicationConfig applicationConfig;

    @Inject
    MarvelApiClientPort marvelApiClientPort;

    @Override
    public HealthCheckResponse call() {
        try {
            InlineResponse200 listCharacters = marvelApiClientPort.listCharacters(applicationConfig.getTs(), applicationConfig.getApiKey(), applicationConfig.getHash(),
                    "", "", null, null, null, null, null, null, BigDecimal.valueOf(1), BigDecimal.valueOf(1));
            return HealthCheckResponse.builder().name(LABEL).up().build();
        } catch (Exception ex) {
            return HealthCheckResponse.builder().name(String.format("%s - %s", LABEL, ex.getMessage())).down().build();
        }
    }

}
