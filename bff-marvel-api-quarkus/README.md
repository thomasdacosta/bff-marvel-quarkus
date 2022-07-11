# BFF Marvel API - 1.0.0

[![codecov](https://codecov.io/gh/thomasdacosta/bff-marvel-quarkus/branch/main/graph/badge.svg?token=JP3hk79XBZ)](https://codecov.io/gh/thomasdacosta/bff-marvel-quarkus)

BFF (Backends For Frontends) desenvolvido em Quarkus que efeuta o acesso as API´s Oficiais da Marvel e busca o personagem, HQ´s e eventos de acordo com o nome pesquisado.

## Tecnologias

- Java 17
- Quarkus
- MySQL
- Jaeger
- Prometheus
- Grafana
- Graylog
- ElasticSearch

## Componentes da Aplicação

- RESTEasy, RESTEasy JSON-B e RESTEasy Jackson para [Endpoint Rest](https://quarkus.io/guides/rest-json)
- RESTEasy Client para [API Client](https://quarkus.io/guides/rest-client)
- CDI para [Injeção de Dependência](https://quarkus.io/guides/cdi)
- Hibernate com Panache para [Camada de Banco de Dados](https://quarkus.io/guides/hibernate-orm-panache)
- Hibernate Validator para [Validação de Dados](https://quarkus.io/guides/validation)
- SmallRye OpenTracing para [Tracing de Aplicação com Jaeger](https://quarkus.io/guides/opentracing)
- OpenTracing JDBC [Tracing de Banco de Dados no Jaeger](https://quarkus.io/guides/opentracing)
- SmallRye Fault-Tolerance para [Retry e Circuit Breaker](https://quarkus.io/guides/smallrye-fault-tolerance)
- SmallRye OpenApi para [Open API e Swagger](https://quarkus.io/guides/openapi-swaggerui)
- SmallRye Health para [Health da Aplicação](https://quarkus.io/guides/smallrye-health)
- Micrometer para [Métricas](https://quarkus.io/guides/micrometer)
- REST Assured para [Testes Unitários e Integrados da API](https://quarkus.io/guides/getting-started-testing)
- Wiremock para [Simular servidores HTTP em Testes](https://wiremock.org/)
- JUnit 5 para [Testes Unitários e Integrados](https://junit.org/junit5/) 
- Jacoco para [Cobertura de Teste](https://quarkus.io/guides/tests-with-coverage)
- Log4j para [Logging](https://quarkus.io/guides/logging)
- Quarkus Logging GELF para [Logging Centralizado](https://quarkus.io/guides/centralized-log-management)

## Links

- Prometheus - [http://localhost:9090/](http://localhost:9090/)
- Grafana - [http://localhost:3000/](http://localhost:3000/)
- Jaeger - [http://localhost:16686/](http://localhost:16686/)
- Graylog - [http://localhost:9000/](http://localhost:9000/)
- ElasticSearch - [http://localhost:9200/](http://localhost:9200/)
- Metrics do Quarkus - [http://localhost:8080/q/metrics](http://localhost:8080/q/metrics)
- Liveness - [http://localhost:8080/q/health/live](http://localhost:8080/q/health/live)
- Readiness - [http://localhost:8080/q/health/ready](http://localhost:8080/q/health/ready)
- Started - [http://localhost:8080/q/health/started](http://localhost:8080/q/health/started)
- Health - [http://localhost:8080/q/health](http://localhost:8080/q/health)
- Health UI - [http://localhost:8080/q/health-ui/](http://localhost:8080/q/health-ui/)

---
Thomás da Costa - [https://thomasdacosta.com.br](https://thomasdacosta.com.br)