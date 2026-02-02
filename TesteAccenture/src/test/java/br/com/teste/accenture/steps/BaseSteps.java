package br.com.teste.accenture.steps;

import br.com.teste.accenture.utils.ApiUtils;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSteps {

    private static final Logger logger = LoggerFactory.getLogger(BaseSteps.class);

    private static final ApiUtils apiUtils = new ApiUtils();

    public static ApiUtils getApiUtils() {
        return apiUtils;
    }

    @Quando("valido o status code com sucesso {string}")
    public void validarStatusCode(String statusCode) {
        int esperado;
        try {
            esperado = Integer.parseInt(statusCode);
        } catch (NumberFormatException e) {
            logger.error("Status code inválido: {}", statusCode, e);
            Assert.fail("Status code inválido: " + statusCode);
            return;
        }
        logger.info("Validando se o status code da resposta é: {}", esperado);
        apiUtils.getResponse().then().log().all().assertThat().statusCode(esperado);
    }
}