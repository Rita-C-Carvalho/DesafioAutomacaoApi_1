package br.com.teste.accenture.services;

import br.com.teste.accenture.exceptions.ExceptionMessages;
import br.com.teste.accenture.exceptions.TesteAccentureExceptions;
import br.com.teste.accenture.headers.HeaderConfigurationDemoQA;
import br.com.teste.accenture.tools.PropertiesLoader;
import br.com.teste.accenture.utils.ApiUtils;
import br.com.teste.accenture.utils.ApiUtilsDemoQA;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static br.com.teste.accenture.utils.Constants.*;

public class UserService extends ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Lê configurações fixas do arquivo
    private static final String endpointCreateUser = PropertiesLoader.getApiPropertie(ENDPOINT_CREATE_USER, DEMOQA_VALUE);
    private static final String endpointGenerateToken = PropertiesLoader.getApiPropertie(ENDPOINT_GENERATE_TOKEN, DEMOQA_VALUE);
    private static final String endpointAuthorized = PropertiesLoader.getApiPropertie(ENDPOINT_AUTHORIZED, DEMOQA_VALUE);

    public void criarUsuario(ApiUtils apiUtils) {
        try {
            logger.info("Iniciando criação de usuário");

            //PEGAR USERNAME E PASSWORD DO PROPERTIES
            String userName = PropertiesLoader.getApiPropertie(USER_NAME, DEMOQA_VALUE);
            String password = PropertiesLoader.getApiPropertie(USER_PASSWORD, DEMOQA_VALUE);

            //CRIAR JSON DINAMICAMENTE COM DADOS DO PROPERTIES
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put(USER_NAME_KEY, userName);
            userNode.put(PASSWORD_KEY, password);

            String payloadJson = userNode.toString();

            ApiUtilsDemoQA.salvarNoArquivoJson(userName, password);

            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderBasicoDemoQA(request, endpointCreateUser);

            Response response = request
                    .body(payloadJson)
                    .log().all()
                    .post();

            logResponseBody(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointCreateUser;
            atualizarEvidencia(request, response, requestUrl, payloadJson);

            if (response.statusCode() == 201) {
                atualizarPropertiesComDadosUsuario(response, CREATED);
                apiUtils.setResponse(response);
                logger.info("Usuário criado com sucesso");
            } else if (response.statusCode() == 406) {
                logger.info("Usuário já existe no sistema");
                atualizarPropertiesComDadosUsuario(response, ALREADY_EXISTS);
                throw new TesteAccentureExceptions(ExceptionMessages.USUARIO_JA_EXISTE + response.getBody().asString());
            } else {
                atualizarPropertiesComDadosUsuario(response, FAILED_VALUE);
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_AO_CRIAR_USUARIO + response.statusCode() + " - " + response.getBody().asString());
            }

        } catch (Exception e) {
            logger.error("Erro ao criar usuário", e);
            atualizarPropertiesComErro(USER_CREATION, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_CRIAR_USUARIO, e);
        }
    }


    private void atualizarPropertiesComDadosUsuario(Response response, String status) {
        try {
            String userId = response.body().jsonPath().getString(USER_ID_MAIUSC);
            String username = response.body().jsonPath().getString(USERNAME);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT));

            //EXTRAIR PASSWORD DO PAYLOAD ORIGINAL
            String password = extrairPasswordDoPayload();

            Properties userProperties = new Properties();
            userProperties.setProperty(LAST_CREATED_USER_ID, userId != null ? userId : N_A);
            userProperties.setProperty(LAST_CREATED_USERNAME, username != null ? username : N_A);
            userProperties.setProperty(LAST_CREATED_PASSWORD, password != null ? password : N_A);
            userProperties.setProperty(LAST_USER_CREATION_DATE, timestamp);
            userProperties.setProperty(USER_CREATION_STATUS, status);
            userProperties.setProperty(LAST_USER_RESPONSE_CODE, String.valueOf(response.statusCode()));

            PropertiesLoader.setMultipleSystemProperties(userProperties, DEMOQA_VALUE);

            logger.info("System Properties atualizadas com dados do usuário:");
            logger.info("User ID: " + userId);
            logger.info("Username: " + username);
            logger.info("Password: " + (password != null ? "***" : "N/A"));
            logger.info("Status: " + status);
            logger.info("Timestamp: " + timestamp);

            PropertiesLoader.printAllSystemProperties(DEMOQA_VALUE);

        } catch (Exception e) {
            logger.error("Erro ao atualizar properties com dados do usuário", e);
        }
    }

    private String extrairPasswordDoPayload() {
        try {
            String payloadJson = getBody(DEMOQA_VALUE, USER_JSON);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payloadJson);
            return rootNode.get(PASSWORD_KEY).asText();
        } catch (Exception e) {
            logger.error("Erro ao extrair password do payload", e);
            return null;
        }
    }

    public String gerarToken(ApiUtils apiUtils) {
        try {
            logger.info("Gerando token de acesso");

            String payloadJson = getBody(DEMOQA_VALUE, USER_JSON);
            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderBasicoDemoQA(request, endpointGenerateToken);

            Response response = request
                    .body(payloadJson)
                    .log().all()
                    .post();

            logResponseBody(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointGenerateToken;
            atualizarEvidencia(request, response, requestUrl, payloadJson);

            if (response.statusCode() != 200) {
                atualizarPropertiesComDadosToken(response, null, FAILED_VALUE);
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_AO_GERAR_TOKEN + response.statusCode() + " - " + response.getBody().asString());
            }

            apiUtils.setResponse(response);
            String token = response.body().jsonPath().getString(TOKEN);

            atualizarPropertiesComDadosToken(response, token, SUCCESS_VALUE);

            logger.info("Token gerado com sucesso");
            return token;

        } catch (Exception e) {
            logger.error("Erro ao gerar token", e);
            atualizarPropertiesComErro(TOKEN_GENERATION, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_GERAR_TOKEN, e);
        }
    }

    // Salvar dados so systems
    private void atualizarPropertiesComDadosToken(Response response, String token, String status) {
        try {
            String expires = response.body().jsonPath().getString(EXPIRES);
            String tokenStatus = response.body().jsonPath().getString(STATUS_KEY);
            String result = response.body().jsonPath().getString(RESULT);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT));

            Properties tokenProperties = new Properties();
            tokenProperties.setProperty(LAST_GENERATED_TOKEN, token != null ? token.substring(0, Math.min(token.length(), 50)) + "..." : N_A);
            tokenProperties.setProperty(TOKEN_EXPIRES, expires != null ? expires : N_A);
            tokenProperties.setProperty(TOKEN_STATUS, tokenStatus != null ? tokenStatus : N_A);
            tokenProperties.setProperty(TOKEN_RESULT, result != null ? result : N_A);
            tokenProperties.setProperty(LAST_TOKEN_GENERATION_DATE, timestamp);
            tokenProperties.setProperty(TOKEN_GENERATION_STATUS, status);
            tokenProperties.setProperty(LAST_TOKEN_RESPONSE_CODE, String.valueOf(response.statusCode()));


            if (token != null) {
                PropertiesLoader.setSystemProperty(CURRENT_FULL_TOKEN, token, DEMOQA_VALUE);
            }

            PropertiesLoader.setMultipleSystemProperties(tokenProperties, DEMOQA_VALUE);

            logger.info("System Properties atualizadas com dados do token gerado");
            logger.info("Token Status: " + status);

        } catch (Exception e) {
            logger.error("Erro ao atualizar properties com dados do token", e);
        }
    }

    public void verificarAutorizacao(ApiUtils apiUtils) {
        try {
            logger.info("Verificando autorização do usuário");

            String payloadJson = getBody(DEMOQA_VALUE, USER_JSON);
            HeaderConfigurationDemoQA headerConfiguration = new HeaderConfigurationDemoQA();
            RequestSpecification request = getRequestSpec();
            request = headerConfiguration.aplicarHeaderBasicoDemoQA(request, endpointAuthorized);

            Response response = request
                    .body(payloadJson)
                    .log().all()
                    .post();

            logResponseBody(response);

            String requestUrl = headerConfiguration.getBaseUrlDemoQA() + endpointAuthorized;
            atualizarEvidencia(request, response, requestUrl, payloadJson);

            if (response.statusCode() != 200) {
                atualizarPropertiesComStatusAutorizacao(response, false, FAILED_VALUE);
                throw new TesteAccentureExceptions(ExceptionMessages.FALHA_VERIFICAR_AUTORIZACAO + response.statusCode() + " - " + response.getBody().asString());
            }

            boolean authorized = false;
            try {
                // Primeiro tenta como JSON
                authorized = response.body().jsonPath().getBoolean(AUTHORIZED_MIN);
                logger.info("Response JSON válido - authorized: " + authorized);
            } catch (Exception e) {
                // Se falhar, trata como texto simples
                String responseText = response.getBody().asString().trim();
                logger.info("Response como texto simples: " + responseText);

                if (TRUE_VALUE.equalsIgnoreCase(responseText)) {
                    authorized = true;
                } else if (FALSE_VALUE.equalsIgnoreCase(responseText)) {
                    authorized = false;
                } else {
                    logger.warn("Response inesperado: " + responseText + ". Assumindo false.");
                    authorized = false;
                }
            }
            atualizarPropertiesComStatusAutorizacao(response, authorized, SUCCESS_VALUE);

            apiUtils.setResponse(response);
            logger.info("Autorização verificada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao verificar autorização", e);
            atualizarPropertiesComErro(AUTHORIZATION_CHECK, e.getMessage());
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_VERIFICAR_AUTORIZACAO, e);
        }
    }

    private void atualizarPropertiesComStatusAutorizacao(Response response, boolean authorized, String status) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT));

            Properties authProperties = new Properties();
            authProperties.setProperty(USER_AUTHORIZED, String.valueOf(authorized));
            authProperties.setProperty(LAST_AUTHORIZATION_CHECK, timestamp);
            authProperties.setProperty(AUTHORIZATION_STATUS, authorized ? AUTHORIZED : NOT_AUTHORIZED);
            authProperties.setProperty(AUTHORIZATION_CHECK_STATUS, status);
            authProperties.setProperty(LAST_AUTHORIZATION_RESPONSE_CODE, String.valueOf(response.statusCode()));

            PropertiesLoader.setMultipleSystemProperties(authProperties, DEMOQA_VALUE);

            logger.info("System Properties atualizadas com status de autorização: " + authorized);

        } catch (Exception e) {
            logger.error("Erro ao atualizar properties com status de autorização", e);
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