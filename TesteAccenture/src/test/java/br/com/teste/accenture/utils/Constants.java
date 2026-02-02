package br.com.teste.accenture.utils;

public class Constants {

    //CONSTANTES REFERENTE A HEADER
    public static final String HEADER_ACCEPT_KEY = "accept";
    public static final String HEADER_ACCEPT_VALUE = "application/json";
    public static final String HEADER_AUTHORIZATION_KEY = "Authorization";
    public static final String HEADER_AUTHORIZATION_KEY_MIN = "authorization";
    public static final String HEADER_AUTHORIZATION_VALUE = "Bearer ";
    public static final String HEADER_AUTHORIZATION_BASIC_VALUE = "Basic ";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";

    //CONSTANTES REFERENTE A PROPERTIES
    public static final String URL_BASE_DEMOQA = "URL_BASE_DEMOQA";
    public static final String ENDPOINT_BOOKS = "ENDPOINT_BOOKS";
    public static final String ENDPOINT_USER_DETAILS = "ENDPOINT_USER_DETAILS";
    public static final String ENDPOINT_CREATE_USER = "ENDPOINT_CREATE_USER";
    public static final String ENDPOINT_GENERATE_TOKEN = "ENDPOINT_GENERATE_TOKEN";
    public static final String ENDPOINT_AUTHORIZED = "ENDPOINT_AUTHORIZED";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "USER_PASSWORD";
    public static final String PROPERTIES_CAMINHO = "properties/";
    public static final String PROPERTIES_EXTENSAO = ".properties";

    //CONSTANTES ARQUIVO JSON
    public static final String RENT_BOOKS_JSON = "rentBooks.json";
    public static final String USER_JSON = "user.json";
    public static final String TEST_DATA_JSON = "testData.json";

    //CONSTANTES STATUS
    public static final String SUCCESS_VALUE = "SUCCESS";
    public static final String FAILED_VALUE = "FAILED";
    public static final String CREATED = "CREATED";
    public static final String ALREADY_EXISTS = "ALREADY_EXISTS";

    //CONSTANTES REFERENTE A TOKEN
    public static final String TOKEN = "token";
    public static final String TOKEN_GENERATION = "TOKEN_GENERATION";
    public static final String EXPIRES = "expires";
    public static final String STATUS_KEY = "status";
    public static final String RESULT = "result";
    public static final String LAST_GENERATED_TOKEN = "LAST_GENERATED_TOKEN";
    public static final String TOKEN_EXPIRES = "TOKEN_EXPIRES";
    public static final String TOKEN_STATUS = "TOKEN_STATUS";
    public static final String TOKEN_RESULT = "TOKEN_RESULT";
    public static final String LAST_TOKEN_GENERATION_DATE = "LAST_TOKEN_GENERATION_DATE";
    public static final String TOKEN_GENERATION_STATUS = "TOKEN_GENERATION_STATUS";
    public static final String LAST_TOKEN_RESPONSE_CODE = "LAST_TOKEN_RESPONSE_CODE";
    public static final String CURRENT_FULL_TOKEN = "CURRENT_FULL_TOKEN";

    //CONSTANTES REFERENTE A AUTORIZACAO
    public static final String AUTHORIZED_MIN = "authorized";
    public static final String USER_AUTHORIZED = "USER_AUTHORIZED";
    public static final String LAST_AUTHORIZATION_CHECK = "LAST_AUTHORIZATION_CHECK";
    public static final String AUTHORIZATION_STATUS = "AUTHORIZATION_STATUS";
    public static final String AUTHORIZATION_CHECK_STATUS = "AUTHORIZATION_CHECK_STATUS";
    public static final String LAST_AUTHORIZATION_RESPONSE_CODE = "LAST_AUTHORIZATION_RESPONSE_CODE";
    public static final String AUTHORIZED = "AUTHORIZED";
    public static final String NOT_AUTHORIZED = "NOT_AUTHORIZED";
    public static final String AUTHORIZATION_CHECK = "AUTHORIZATION_CHECK";

    //CONSTANTES BOOLEANAS
    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";

    //CONSTANTES GERAIS
    public static final String DEMOQA_VALUE = "demoqa";
    public static final String BOOKS_LIST = "BOOKS_LIST";
    public static final String BOOKS_RENTAL = "BOOK_RENTAL";
    public static final String LAST_CREATED_USER_ID = "LAST_CREATED_USER_ID";
    public static final String LAST_CREATED_USERNAME = "LAST_CREATED_USERNAME";
    public static final String LAST_CREATED_PASSWORD = "LAST_CREATED_PASSWORD";
    public static final String LAST_ERROR_OPERATION = "LAST_ERROR_OPERATION";
    public static final String LAST_ERROR_MESSAGE = "LAST_ERROR_MESSAGE";
    public static final String LAST_ERROR_DATE = "LAST_ERROR_DATE";
    public static final String LAST_USER_CREATION_DATE = "LAST_USER_CREATION_DATE";
    public static final String USER_CREATION_STATUS = "USER_CREATION_STATUS";
    public static final String LAST_USER_RESPONSE_CODE = "LAST_USER_RESPONSE_CODE";
    public static final String ISBN = "isbn";
    public static final String TITLE = "title";
    public static final String ISBN_MAIUSC = "_ISBN";
    public static final String TITLE_MAIUSC = "_TITLE";
    public static final String USER_ID = "userId";
    public static final String USER_ID_MAIUSC = "userID";
    public static final String USER_DETAILS = "USER_DETAILS";
    public static final String USER_DETAILS_LAST_CHECK = "USER_DETAILS_LAST_CHECK";
    public static final String USER_DETAILS_USER_ID = "USER_DETAILS_USER_ID";
    public static final String USER_DETAILS_USERNAME = "USER_DETAILS_USERNAME";
    public static final String USER_DETAILS_BOOKS_COUNT = "USER_DETAILS_BOOKS_COUNT";
    public static final String USER_DETAILS_STATUS = "USER_DETAILS_STATUS";
    public static final String USER_DETAILS_RESPONSE_CODE = "USER_DETAILS_RESPONSE_CODE";
    public static final String USER_BOOK = "USER_BOOK_";
    public static final String USER_CREATION = "USER_CREATION";
    public static final String COLLECTION_OF_ISBNS = "collectionOfIsbns";
    public static final String N_A = "N/A";
    public static final String USERNAME = "username";
    public static final String USER_NAME_KEY = "userName";
    public static final String PASSWORD_KEY = "password";
    public static final String BOOKS = "books";
    public static final String DATE_HOUR_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String BOOK_KEY = "bookKey";

    //CONSTANTES REFERENTE A MONTAGEM DAS EVIDENCIAS
    public static final String CENARIO_DESCONHECIDO = "cenario_desconhecido";
    public static final String REPORTS = "reports";
    public static final String SUCESSO_VALUE = "SUCESSO";
    public static final String FALHA_VALUE = "FALHA";
    public static final String SUCESSOS_VALUE = "SUCESSOS: ";
    public static final String FALHAS_VALUE = " | FALHAS: ";
    public static final String STATUS_PASSOU_TEXT = "STATUS: PASSOU";
    public static final String STATUS_FALHOU_TEXT = "STATUS: FALHOU";
    public static final String MOTIVO_FALHA_TEXT = "MOTIVO DA FALHA";
    public static final String DATE_HOUR_FORMAT_1 = "yyyyMMdd_HHmmss";
    public static final String DATE_HOUR_FORMAT_2 = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_HOUR_FORMAT_3 = "dd/MM/yyyy HH:mm:ss.SSS";
    public static final String REGEX_VALUE_1 = "[^a-zA-Z0-9\s]";
    public static final String REGEX_VALUE_2 = "\s+";
    public static final String BARRA_VALUE = "/";
    public static final String COMPLETO_VALUE = "_COMPLETO_";
    public static final String EXTENSAO_PDF = ".pdf";
    public static final String VAZIO_VALUE = "";
    public static final String UNDERLINE_VALUE = "_";
    public static final String EVIDENCIAS_COPLETEAS_VALUE = "EVIDENCIAS COMPLETAS - ";
    public static final String DATA_HORA_TEXT = "Data/Hora: ";
    public static final String TOTAL_REQUISICOES_TEXT = "Total de Requisições: ";
    public static final String REQUISICAO_TEXT_FORMATED = "=== REQUISICAO ";
    public static final String FORMATACAO = " ===";
    public static final String TIMESTAMP_KEY = "Timestamp: ";
    public static final String REQUEST_URL_MAIUSC = "REQUEST URL";
    public static final String REQUEST_BODY_MAIUSC = "REQUEST BODY";
    public static final String TEXT_PLAIN_KEY = "text/plain";
    public static final String REQUEST_URL = "Request URL";
    public static final String REQUEST_BODY = "Request Body";
    public static final String RESPONSE_STATUS_CODE = "Response Status Code";
    public static final String STATUS_CODE_KEY = "Status Code: ";
    public static final String STATUS_LINE_TEXT = "Status Line: ";
    public static final String RESPONSE_STATUS_TEXT = "RESPONSE STATUS";
    public static final String RESPONSE_BODY_TEXT = "RESPONSE BODY";

    //CONSTANTES REFERENTE A CAMINHO DE ARQUIVOS
    public static final String CAMINHO_ARQUIVOS_DATA = "/src/test/resources/data/";
    public static final String CAMINHO_ARQUIVO_USER_JSON = "src/test/resources/data/demoqa/user.json";

}
