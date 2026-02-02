package br.com.teste.accenture.headers;

import br.com.teste.accenture.tools.PropertiesLoader;
import io.restassured.specification.RequestSpecification;

import static br.com.teste.accenture.utils.Constants.*;

public class HeaderConfigurationDemoQA {

    private final String baseUrlDemoQA = PropertiesLoader.getApiPropertie(URL_BASE_DEMOQA, DEMOQA_VALUE);

    public String getBaseUrlDemoQA() {
        return baseUrlDemoQA;
    }

    public RequestSpecification aplicarHeaderBasicoDemoQA(RequestSpecification request, String endpoint) {
        return request
                .baseUri(baseUrlDemoQA)
                .basePath(endpoint)
                .header(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE)
                .header(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
    }

    public RequestSpecification aplicarHeaderComAutorizacao(RequestSpecification request, String endpoint, String token) {
        return request
                .baseUri(baseUrlDemoQA)
                .basePath(endpoint)
                .header(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE)
                .header(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE)
                .header(HEADER_AUTHORIZATION_KEY, HEADER_AUTHORIZATION_VALUE + token);
    }

}