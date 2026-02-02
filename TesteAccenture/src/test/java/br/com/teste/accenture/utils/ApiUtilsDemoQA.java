package br.com.teste.accenture.utils;

import br.com.teste.accenture.exceptions.ExceptionMessages;
import br.com.teste.accenture.exceptions.TesteAccentureExceptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Base64;

import static br.com.teste.accenture.utils.Constants.*;

public class ApiUtilsDemoQA extends ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiUtilsDemoQA.class);


    public static String getBookDataFromJson(String bookKey, String field) {
        try {
            String jsonContent = getBody(DEMOQA_VALUE, TEST_DATA_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            JsonNode bookNode = rootNode.get(BOOKS).get(bookKey).get(field);
            return bookNode.asText();
        } catch (Exception e) {
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_OBTER_DADOS_DO_LIVRO + bookKey + ", campo: " + field, e);
        }
    }

    public static String generateBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    //METODO PARA SALVAR USUÁRIO NO ARQUIVO JSON
    public static void salvarNoArquivoJson(String userName, String password) {
        try {
            // Caminho do arquivo
            String filePath = CAMINHO_ARQUIVO_USER_JSON;

            // Criar JSON
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put(USER_NAME_KEY, userName);
            userNode.put(PASSWORD_KEY, password);

            // Escrever no arquivo com formatação
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), userNode);

            logger.info("Dados do usuário salvos no arquivo: " + filePath);
            logger.info("Username: " + userName);
            logger.info("Password: ***");

        } catch (Exception e) {
            logger.error("Erro ao salvar dados no arquivo JSON", e);
            throw new TesteAccentureExceptions(ExceptionMessages.ERRO_AO_SALVAR_DADOS_NO_ARQUIVO_JSON, e);
        }
    }
}