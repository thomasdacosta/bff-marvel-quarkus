# BFF Marvel API - 1.0.0

BFF (Backends For Frontends) desenvolvido em Quarkus que efeuta o acesso as API´s Oficiais da Marvel e busca o personagem, HQ´s e eventos de acordo com o nome pesquisado.

## Componentes da Aplicação

- RESTEasy, RESTEasy JSON-B e RESTEasy Jackson para [Endpoint Rest](https://quarkus.io/guides/rest-json)
- RESTEasy Client para [API Client](https://quarkus.io/guides/rest-client)
- Panache com Hibernate para [Camada de Banco de Dados](https://quarkus.io/guides/hibernate-orm-panache)
- Hibernate-Validator para [Validação de Dados](https://quarkus.io/guides/validation)
- Smallrye OpenTracing para [Tracing de Aplicação com Jaeger](https://quarkus.io/guides/opentracing)
- OpenTracing JDBC [Tracing de Banco de Dados no Jaeger](https://quarkus.io/guides/opentracing)
- SmallRye Fault-Tolerance para [Retry e Circuit Breaker](https://quarkus.io/guides/smallrye-fault-tolerance)
- SmallRye OpenApi para [Open API e Swagger](https://quarkus.io/guides/openapi-swaggerui)
- REST Assured para [Testes Unitários e Integrados da API](https://quarkus.io/guides/getting-started-testing)
- Wiremock para [Simular servidores HTTP em Testes](https://wiremock.org/)
- JUnit 5 para [Testes Unitários e Integrados](https://junit.org/junit5/) 
- Jacoco para [Cobertura de Teste](https://quarkus.io/guides/tests-with-coverage)
- CDI para [Injeção de Dependência](https://quarkus.io/guides/cdi)
- Log4j para [Logging](https://quarkus.io/guides/logging)

---
Thomás da Costa - [https://thomasdacosta.com.br](https://thomasdacosta.com.br)