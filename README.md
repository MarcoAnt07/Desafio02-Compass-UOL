# Desafio 02
Projeto para execução do Desafio 02 do programa de bolsas Compass.UOL.  
  
Ambiente: Java (JDK 17), e banco de dados H2 (em memória).  
  
## Contexto 
O desafio consiste em desenvolver dois microsserviços rodando localmente.

Os microsserviços devem trabalhar juntos para manipular posts e comments da API JSONPlaceholder.

Os microsserviços são:
- Microsserviço B: Consome API JSONPlaceholder e encapsula lógica de negócio (porta 8081);
- Microsserviço A: Consome as rotas disponibilizadas pelo Microsserviço B (porta 8080).  
  
## Para testes e execução

1. Executar microsservicob na porta 8081 e executar a requisição '/sync-data' via Postman ou Insomnia para popular banco de dados.  
2. Executar microsservicoa na porta 8080 e executar as requisições disponibilizadas no PostController do microsservicoa.

### Swagger
Foi disponibilizada documentação da API com OpenAPI e Swagger. Para consulta, executar os passos para testes e execução e acessar:
- Microsserviço A: http://localhost:8080/swagger-ui/index.html  
- Microsserviço B: http://localhost:8081/swagger-ui/index.html  
