# Teste Accenture - Automação DemoQA BookStore API

Este projeto é uma automação de testes para a API DemoQA BookStore, desenvolvido em Java com Cucumber, REST Assured e JUnit. O framework realiza testes end-to-end do fluxo completo de gerenciamento de usuários e aluguel de livros.

## Funcionalidades Testadas

O projeto testa o seguinte fluxo principal:
1. Criar Usuário - Cadastro de novo usuário na plataforma
2. Gerar Token - Autenticação e geração de token JWT
3. Verificar Autorização - Validação se usuário está autorizado
4. Listar Livros - Consulta de livros disponíveis
5. Alugar Livros - Aluguel de livros específicos
6. Detalhes do Usuário - Consulta de informações e livros alugados

## Tecnologias Utilizadas

- Java 17
- Maven 3.x
- Cucumber 7.18.0 (BDD e Gherkin)
- REST Assured 5.3.2 (Testes de API)
- JUnit 4.13.2 (Framework de testes)
- Jackson 2.15.2 (Manipulação JSON)
- PDFBox 2.0.29 (Geração de evidências PDF)
- SLF4J + Logback 2.0.9 (Sistema de logs)

## Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- IDE (IntelliJ IDEA recomendado)

## Configuração Inicial

**IMPORTANTE:** Antes de executar os testes, você DEVE alterar o nome do usuário no arquivo:
src/test/resources/properties/demoqa.properties


Altere apenas a linha USER_NAME para um nome único:

```properties
USER_NAME=SeuNomeUnico123
USER_PASSWORD=Teste33*
 ```
## Como Executar
Via IDE (IntelliJ):
Execute a classe Runner localizada em: src/test/java/br/com/teste/accenture/runner/Runner.java

## Evidências Automáticas
As evidências são geradas automaticamente após a execução dos testes e salvas em: IdeaProjects/TesteAccenture/TesteAccenture/reports/

Características das evidências PDF:

- PDF único por cenário com todas as requisições
- Status visual (SUCESSO/FALHA) para cada requisição
- Resumo estatístico (sucessos vs falhas)
- Motivos detalhados das falhas
- Timestamp de cada requisição
- Dados completos: URL, Body, Status, Response

## Endpoints Testados
- POST (Criar usuário)
- POST (Gerar token)
- POST (Verificar autorização)
- GET (Listar livros)
- POST (Alugar livros)
- GET (Detalhes do usuário)

## Tags Disponíveis
- @DemoQA_User_Management_01
- @DemoQA_Token_Management_01
- @DemoQA_Authorization_01
- @DemoQA_Books_Management_01
- @DemoQA_Book_Rental_Multiple
- @DemoQA_User_Details_01
- @DemoQA_Complete_Flow_01