package br.com.teste.accenture.hooks;

import br.com.teste.accenture.utils.ApiUtils;
import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    public static Scenario currentScenario;

    //antes de cada cenario
    @Before
    public void init(Scenario scenario) {
        currentScenario = scenario;
        ApiUtils.EvidenciasCenario.iniciarCenario(scenario.getName());
        logger.info("=== INICIANDO CENÁRIO: " + scenario.getName() + " ===");
        logger.info("Tags do cenário: " + scenario.getSourceTagNames());
    }

    //depois de cada cenario
    @After
    public void tearDown(Scenario scenario) {
        ApiUtils.EvidenciasCenario.finalizarCenarioEGerarPDF();
        try {
            if (scenario.isFailed()) {
                logger.error("=== CENÁRIO FALHOU: " + scenario.getName() + " ===");
                logger.error("Status: " + scenario.getStatus());
            } else {
                logger.info("=== CENÁRIO PASSOU: " + scenario.getName() + " ===");
                logger.info("Status: " + scenario.getStatus());
            }
        } catch (Exception e) {
            logger.error("Erro no tearDown do cenário", e);
        } finally {
            currentScenario = null;
        }
    }

}