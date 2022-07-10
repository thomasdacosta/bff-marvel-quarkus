# BFF Marvel API - 1.0.0

BFF (Backends For Frontends) desenvolvido em Quarkus que efeuta o acesso as API´s Oficiais da Marvel e busca o personagem, HQ´s e eventos de acordo com o nome pesquisado.

## Componentes da Aplicação

- RESTEasy, RESTEasy JSON-B e RESTEasy Jackson para ***[Endpoint Rest]***
- RESTEasy Client para **[API Client]**
- Panache com Hibernate para **[Camada de Banco de Dados]**
- Hibernate-Validator para **[Validação de Dados]**
- Smallrye Opentracing para **[Tracing de Aplicação com Jaeger]**
- Opentracing JDBC **[Tracing de Banco de Dados no Jaeger]**
- Smallrye Fault-Tolerance para **[Retry e Circuit Breaker]**
- Smallrye Openapi para **[Open API e Swagger]**
- REST Assured para **[Testes Unitários e Integrados da API]**
- Wiremock para **[Simular servidores HTTP em Testes]**
- JUnit 5 para **[Testes Unitários e Integrados]** 

---
Thomás da Costa - [https://thomasdacosta.com.br](https://thomasdacosta.com.br)