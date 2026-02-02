package br.com.teste.accenture.services;

import br.com.teste.accenture.exceptions.ExceptionMessages;
import br.com.teste.accenture.exceptions.TesteAccentureExceptions;
import br.com.teste.accenture.headers.HeaderConfigurationDemoQA;
import br.com.teste.accenture.utils.ApiUtilsDemoQA;
import br.com.teste.accenture.tools.PropertiesLoader;
import br.com.teste.accenture.utils.ApiUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import static br.com.teste.accenture.utils.Constants.*;

public class BookService extends ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    // Lê configurações fixas do arquivo
    private static final String endpointBooks = PropertiesLoader.getApiPropertie(ENDPOINT_BOOKS, DEMOQA_VALUE);
    private static final String endpointUserDetails = PropertiesLoader.getApiPropertie(ENDPOINT_USER_DETAILS, DEMOQA_VALUE);

    public void listarLivros(ApiUtils apiUtils) {
        try {
            logger.info("Listando livros disponíveis");

            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderBasicoDemoQA(request, endpointBooks);

            Response response = request
                    .log().all()
                    .get();

            logResponseBody(response);
            apiUtils.setResponse(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointBooks;
            atualizarEvidencia(request, response, requestUrl, null);

            if (response.statusCode() != 200) {
                atualizarPropertiesComErro(BOOKS_LIST, "Status: " + response.statusCode());
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_AO_LISTAR_LIVROS + response.statusCode() + " - " + response.getBody().asString());
            }

            //atualizarPropertiesComLivros(response, "SUCCESS");

            logger.info("Livros listados com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao listar livros", e);
            atualizarPropertiesComErro(BOOKS_LIST, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_LISTAR_LIVROS, e);
        }
    }



    public void alugarLivro(ApiUtils apiUtils, String bookKey, String token) {
        try {
            String userId = PropertiesLoader.getFromSystem(LAST_CREATED_USER_ID, DEMOQA_VALUE);

            if (userId == null || userId.isEmpty() || "N/A".equals(userId)) {
                throw new RuntimeException("User ID válido não encontrado no System Properties. Execute a criação de usuário primeiro.");
            }

            logger.info("Alugando livro: " + bookKey + " para usuário: " + userId);

            String payloadJson = getBody(DEMOQA_VALUE, RENT_BOOKS_JSON);
            String isbn = ApiUtilsDemoQA.getBookDataFromJson(bookKey, ISBN);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(payloadJson);
            ObjectNode rootObjectNode = (ObjectNode) rootNode;
            rootObjectNode.put(USER_ID, userId);

            ObjectNode isbnNode = (ObjectNode) rootNode.get(COLLECTION_OF_ISBNS).get(0);
            isbnNode.put(ISBN, isbn);

            String updatedPayloadJson = rootNode.toString();

            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderComAutorizacao(request, endpointBooks, token);

            //PEGAR USERNAME E PASSWORD DO SYSTEM PROPERTIES
            String userName = PropertiesLoader.getFromSystem(LAST_CREATED_USERNAME, DEMOQA_VALUE);
            String password = PropertiesLoader.getFromSystem(LAST_CREATED_PASSWORD, DEMOQA_VALUE);

            if (userName == null || password == null) {
                throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_REALIZAR_LOGIN);
            }

            String basicAuth = ApiUtilsDemoQA.generateBasicAuth(userName, password);
            request = request.header(HEADER_AUTHORIZATION_KEY_MIN, HEADER_AUTHORIZATION_BASIC_VALUE + basicAuth);

            Response response = request
                    .body(updatedPayloadJson)
                    .log().all()
                    .post();

            logResponseBody(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointBooks;
            atualizarEvidencia(request, response, requestUrl, updatedPayloadJson);

            if (response.statusCode() == 201) {
                //atualizarPropertiesComAluguel(response, userId, bookKey, isbn, "SUCCESS");
                apiUtils.setResponse(response);
                logger.info("✅ Livro '" + bookKey + "' alugado com sucesso");
            } else {
                //atualizarPropertiesComAluguel(response, userId, bookKey, isbn, "FAILED");
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_AO_ALUGAR_LIVRO + bookKey + "': " + response.statusCode() + " - " + response.getBody().asString());
            }

        } catch (Exception e) {
            logger.error("Erro ao alugar livro: " + bookKey, e);
            atualizarPropertiesComErro(BOOKS_RENTAL, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_ALUGAR_LIVRO + bookKey, e);
        }
    }


    public void listarDetalhesUsuario(ApiUtils apiUtils, String token) {
        try {
            // 🔥 PEGAR USER ID DO SYSTEM PROPERTIES
            String userId = PropertiesLoader.getFromSystem(LAST_CREATED_USER_ID, DEMOQA_VALUE);

            if (userId == null || userId.isEmpty() || N_A.equals(userId)) {
                throw new RuntimeException("User ID válido não encontrado no System Properties. Execute a criação de usuário primeiro.");
            }

            logger.info("Listando detalhes do usuário: " + userId);

            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderComAutorizacao(request, endpointUserDetails + userId, token);

            // 🔥 PEGAR USERNAME E PASSWORD DO SYSTEM PROPERTIES
            String userName = PropertiesLoader.getFromSystem(LAST_CREATED_USERNAME, DEMOQA_VALUE);
            String password = PropertiesLoader.getFromSystem(LAST_CREATED_PASSWORD, DEMOQA_VALUE);

            if (userName == null || password == null) {
                throw new RuntimeException("Username ou Password não encontrados no System Properties. Execute a criação de usuário primeiro.");
            }

            String basicAuth = ApiUtilsDemoQA.generateBasicAuth(userName, password);
            request = request.header(HEADER_AUTHORIZATION_KEY_MIN, HEADER_AUTHORIZATION_BASIC_VALUE + basicAuth);

            Response response = request
                    .log().all()
                    .get();

            logResponseBody(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointUserDetails + userId;
            atualizarEvidencia(request, response, requestUrl, "");

            if (response.statusCode() == 200) {
                atualizarPropertiesComDetalhesUsuario(response, userId, SUCCESS_VALUE);
                apiUtils.setResponse(response);
                logger.info("Detalhes do usuário listados com sucesso");
            } else {
                atualizarPropertiesComDetalhesUsuario(response, userId, FAILED_VALUE);
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_AO_LISTAR_DETALHES_USUARIO + response.statusCode() + " - " + response.getBody().asString());
            }

        } catch (Exception e) {
            logger.error("Erro ao listar detalhes do usuário", e);
            atualizarPropertiesComErro(USER_DETAILS, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_LISTAR_DETALHES_USUARIO, e);
        }
    }

    private void atualizarPropertiesComDetalhesUsuario(Response response, String userId, String status) {
        try {
            String username = response.body().jsonPath().getString(USERNAME);
            List<Object> books = response.body().jsonPath().getList(BOOKS);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT));

            Properties userDetailsProperties = new Properties();
            userDetailsProperties.setProperty(USER_DETAILS_LAST_CHECK, timestamp);
            userDetailsProperties.setProperty(USER_DETAILS_USER_ID, userId);
            userDetailsProperties.setProperty(USER_DETAILS_USERNAME, username != null ? username : N_A);
            userDetailsProperties.setProperty(USER_DETAILS_BOOKS_COUNT, String.valueOf(books != null ? books.size() : 0));
            userDetailsProperties.setProperty(USER_DETAILS_STATUS, status);
            userDetailsProperties.setProperty(USER_DETAILS_RESPONSE_CODE, String.valueOf(response.statusCode()));

            if (books != null && !books.isEmpty()) {
                for (int i = 0; i < Math.min(3, books.size()); i++) {
                    Object book = books.get(i);
                    if (book instanceof java.util.Map) {
                        java.util.Map<String, Object> bookMap = (java.util.Map<String, Object>) book;
                        String isbn = (String) bookMap.get(ISBN);
                        String title = (String) bookMap.get(TITLE);

                        userDetailsProperties.setProperty(USER_BOOK + (i + 1) + ISBN_MAIUSC, isbn != null ? isbn : N_A);
                        userDetailsProperties.setProperty(USER_BOOK + (i + 1) + TITLE_MAIUSC, title != null ? title : N_A);
                    }
                }
            }

            PropertiesLoader.setMultipleSystemProperties(userDetailsProperties, DEMOQA_VALUE);
            logger.info("System Properties atualizadas com detalhes do usuário. Livros: " + (books != null ? books.size() : 0));

        } catch (Exception e) {
            logger.error("Erro ao atualizar properties com detalhes do usuário", e);
        }
    }

    private void atualizarPropertiesComErro(String operacao, String erro) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT));

            Properties errorProperties = new Properties();
            errorProperties.setProperty(LAST_ERROR_OPERATION, operacao);
            errorProperties.setProperty(LAST_ERROR_MESSAGE, erro);
            errorProperties.setProperty(LAST_ERROR_DATE, timestamp);

            PropertiesLoader.setMultipleSystemProperties(errorProperties, DEMOQA_VALUE);

            logger.error("System Properties atualizadas com erro da operação: " + operacao);

        } catch (Exception e) {
            logger.error("Erro ao atualizar properties com dados de erro", e);
        }
    }
}