package br.com.teste.accenture.utils;

import br.com.teste.accenture.hooks.Hooks;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.io.FileUtils;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static br.com.teste.accenture.utils.Constants.*;
import static io.restassured.RestAssured.given;

public class ApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApiUtils.class);

    private Response response;
    private static RequestSpecification request;
    private ValidatableResponse json;

    public static RequestSpecification getRequestSpec() {
        logger.debug("Configurando RequestSpec padrão");

        RestAssuredConfig config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000));

        RequestSpecBuilder requestSpecification = new RequestSpecBuilder();
        requestSpecification.setConfig(config);
        requestSpecification.setRelaxedHTTPSValidation();

        return given().spec(requestSpecification.build());
    }

    public Response getResponse() {
        return this.response;
    }

    public RequestSpecification getRequest() {
        return request;
    }

    public ValidatableResponse getJson() {
        return json;
    }

    public void setResponse(Response response) {
        if (response != null && response.getStatusCode() > 0) {
            logger.info("Response atualizado com sucesso. Status Code: {}", response.getStatusCode());
        } else {
            logger.warn("Tentativa de atualizar o response com objeto nulo ou inválido.");
        }
        this.response = response;
    }



    public static void atualizarEvidencia(RequestSpecification request, Response response, String url, String body) {
        logger.debug("Coletando evidência para PDF final do cenário");

        //ANEXAR NO CUCUMBER
        Scenario scenario = Hooks.currentScenario;
        if (scenario != null) {
            scenario.attach(url, TEXT_PLAIN_KEY, REQUEST_URL);
            scenario.attach(body != null ? body : N_A, CONTENT_TYPE_VALUE, REQUEST_BODY);
            scenario.attach("" + response.getStatusCode(), TEXT_PLAIN_KEY, RESPONSE_STATUS_CODE);
            scenario.attach(response.getBody().asByteArray(), CONTENT_TYPE_VALUE, REQUEST_BODY);
        }

        //DETECTAR AUTOMATICAMENTE SE FOI SUCESSO OU FALHA
        boolean sucesso = response.getStatusCode() >= 200 && response.getStatusCode() < 300;
        String motivoFalha = null;

        if (!sucesso) {
            motivoFalha = STATUS_CODE_KEY+ response.getStatusCode() + " - " + response.getStatusLine();
            try {
                String responseBody = response.getBody().asString();
                if (responseBody != null && !responseBody.trim().isEmpty()) {
                    motivoFalha += "\nResponse: " + responseBody;
                }
            } catch (Exception e) {
                // Ignorar erro ao ler response body
            }
        }

        //ADICIONAR À LISTA DE EVIDÊNCIAS COM STATUS
        EvidenciasCenario.adicionarEvidencia(url, body, response, sucesso, motivoFalha);
    }

    public static class EvidenciasCenario {
        private static final Logger logger = LoggerFactory.getLogger(EvidenciasCenario.class);
        private static final List<EvidenciaRequest> evidencias = new ArrayList<>();
        private static String nomeCenario = "";

        public static void iniciarCenario(String nome) {
            evidencias.clear();
            nomeCenario = nome != null ? nome : CENARIO_DESCONHECIDO;
            logger.info("Iniciando coleta de evidências para o cenário: " + nomeCenario);
        }

        //METODO ATUALIZADO PARA ADICIONAR EVIDÊNCIA COM STATUS
        public static void adicionarEvidencia(String url, String body, Response response, boolean sucesso, String motivoFalha) {
            evidencias.add(new EvidenciaRequest(url, body, response, sucesso, motivoFalha));
            String status = sucesso ? SUCESSO_VALUE : FALHA_VALUE;
            logger.debug("Evidência adicionada [" + status + "]. Total: " + evidencias.size());
        }


        public static void finalizarCenarioEGerarPDF() {
            if (!evidencias.isEmpty()) {
                logger.info("Gerando PDF completo com " + evidencias.size() + " evidências");
                gerarPDFCompleto();
                evidencias.clear();
            } else {
                logger.warn("Nenhuma evidência encontrada para gerar PDF");
            }
        }

        //Classe interna para armazenar dados da evidência
        static class EvidenciaRequest {
            String url;
            String body;
            Response response;
            LocalDateTime timestamp;
            boolean sucesso;
            String motivoFalha;

            EvidenciaRequest(String url, String body, Response response, boolean sucesso, String motivoFalha) {
                this.url = url;
                this.body = body;
                this.response = response;
                this.timestamp = LocalDateTime.now();
                this.sucesso = sucesso;
                this.motivoFalha = motivoFalha;
            }
        }

        // METODO PRINCIPAL PARA GERAR PDF COMPLETO (ATUALIZADO)
        private static void gerarPDFCompleto() {
            try {
                String reportsPath = REPORTS;
                File reportsDir = new File(reportsPath);
                if (!reportsDir.exists()) {
                    reportsDir.mkdirs();
                }

                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT_1));
                String nomeArquivo = nomeCenario.replaceAll(REGEX_VALUE_1, VAZIO_VALUE).replaceAll(REGEX_VALUE_2, UNDERLINE_VALUE);
                String pdfPath = reportsPath + BARRA_VALUE + nomeArquivo + COMPLETO_VALUE + timestamp + EXTENSAO_PDF;

                try (PDDocument document = new PDDocument()) {
                    PDPageContentStream contentStream = null;
                    PDPage currentPage = null;
                    float yPosition = 0;
                    float margin = 50;
                    float pageWidth = 0;

                    // Criar primeira página
                    currentPage = new PDPage();
                    document.addPage(currentPage);
                    contentStream = new PDPageContentStream(document, currentPage);
                    yPosition = 750;
                    pageWidth = currentPage.getMediaBox().getWidth() - (2 * margin);

                    // 📋 TÍTULO DO DOCUMENTO
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(EVIDENCIAS_COPLETEAS_VALUE + nomeCenario.toUpperCase());
                    contentStream.endText();
                    yPosition -= 30;

                    // 📅 DATA E HORA
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    String dataHora = DATA_HORA_TEXT + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT_2));
                    contentStream.showText(dataHora);
                    contentStream.endText();
                    yPosition -= 20;

                    // 📊 RESUMO COM ESTATÍSTICAS
                    long sucessos = evidencias.stream().filter(e -> e.sucesso).count();
                    long falhas = evidencias.size() - sucessos;

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(TOTAL_REQUISICOES_TEXT + evidencias.size());
                    contentStream.endText();
                    yPosition -= 15;

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(SUCESSOS_VALUE + sucessos + FALHAS_VALUE + falhas);
                    contentStream.endText();
                    yPosition -= 40;

                    // 🔄 PROCESSAR CADA EVIDÊNCIA
                    int numeroRequisicao = 1;
                    for (EvidenciaRequest evidencia : evidencias) {

                        // Verificar se precisa de nova página
                        if (yPosition < 250) {
                            contentStream.close();
                            currentPage = new PDPage();
                            document.addPage(currentPage);
                            contentStream = new PDPageContentStream(document, currentPage);
                            yPosition = 750;
                            pageWidth = currentPage.getMediaBox().getWidth() - (2 * margin);
                        }

                        // 📊 SEPARADOR DE REQUISIÇÃO COM STATUS
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                        contentStream.newLineAtOffset(margin, yPosition);
                        String statusTexto = evidencia.sucesso ? SUCESSO_VALUE : FALHA_VALUE;
                        contentStream.showText(REQUISICAO_TEXT_FORMATED + numeroRequisicao + " - " + statusTexto + FORMATACAO);
                        contentStream.endText();
                        yPosition -= 25;

                        // 🎨 STATUS VISUAL
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        contentStream.newLineAtOffset(margin, yPosition);
                        if (evidencia.sucesso) {
                            contentStream.showText(STATUS_PASSOU_TEXT);
                        } else {
                            contentStream.showText(STATUS_FALHOU_TEXT);
                        }
                        contentStream.endText();
                        yPosition -= 18;

                        // 🚨 MOTIVO DA FALHA (se houver)
                        if (!evidencia.sucesso && evidencia.motivoFalha != null && !evidencia.motivoFalha.trim().isEmpty()) {
                            PDFResult result = adicionarSecaoComPaginacao(contentStream, document, MOTIVO_FALHA_TEXT,
                                    limparTexto(evidencia.motivoFalha), yPosition, margin, pageWidth);
                            contentStream = result.contentStream;
                            yPosition = result.yPosition;
                        }

                        // 🕐 TIMESTAMP DA REQUISIÇÃO
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 9);
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText(TIMESTAMP_KEY + evidencia.timestamp.format(DateTimeFormatter.ofPattern(DATE_HOUR_FORMAT_3)));
                        contentStream.endText();
                        yPosition -= 20;

                        // Adicionar cada seção da evidência
                        PDFResult result = adicionarSecaoComPaginacao(contentStream, document, REQUEST_URL_MAIUSC,
                                limparTexto(evidencia.url), yPosition, margin, pageWidth);
                        contentStream = result.contentStream;
                        yPosition = result.yPosition;

                        if (evidencia.body != null && !evidencia.body.trim().isEmpty()) {
                            result = adicionarSecaoComPaginacao(contentStream, document, REQUEST_BODY_MAIUSC,
                                    limparTexto(formatarJson(evidencia.body)), yPosition, margin, pageWidth);
                            contentStream = result.contentStream;
                            yPosition = result.yPosition;
                        }

                        String statusInfo = STATUS_CODE_KEY + evidencia.response.getStatusCode() + "\n" +
                                STATUS_LINE_TEXT + evidencia.response.getStatusLine();
                        result = adicionarSecaoComPaginacao(contentStream, document, RESPONSE_STATUS_TEXT,
                                limparTexto(statusInfo), yPosition, margin, pageWidth);
                        contentStream = result.contentStream;
                        yPosition = result.yPosition;

                        String responseBody = evidencia.response.getBody().asString();
                        if (responseBody != null && !responseBody.trim().isEmpty()) {
                            result = adicionarSecaoComPaginacao(contentStream, document, RESPONSE_BODY_TEXT,
                                    limparTexto(formatarJson(responseBody)), yPosition, margin, pageWidth);
                            contentStream = result.contentStream;
                            yPosition = result.yPosition;
                        }

                        yPosition -= 30; // Espaço entre requisições
                        numeroRequisicao++;
                    }

                    if (contentStream != null) {
                        contentStream.close();
                    }

                    document.save(pdfPath);
                    logger.info("PDF completo do cenário salvo em: " + pdfPath);
                }

            } catch (Exception e) {
                logger.error("Erro ao gerar PDF completo do cenário", e);
            }
        }

        // 📄 Classe para retornar resultado da paginação
        static class PDFResult {
            PDPageContentStream contentStream;
            float yPosition;

            PDFResult(PDPageContentStream contentStream, float yPosition) {
                this.contentStream = contentStream;
                this.yPosition = yPosition;
            }
        }

        // METODO PARA ADICIONAR SEÇÃO COM PAGINAÇÃO AUTOMÁTICA
        private static PDFResult adicionarSecaoComPaginacao(PDPageContentStream contentStream, PDDocument document,
                                                            String titulo, String conteudo, float yPosition,
                                                            float margin, float pageWidth) throws Exception {

            // Verificar se precisa de nova página para o título
            if (yPosition < 100) {
                contentStream.close();
                PDPage newPage = new PDPage();
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                yPosition = 750;
                pageWidth = newPage.getMediaBox().getWidth() - (2 * margin);
            }

            // Título da seção
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(titulo + ":");
            contentStream.endText();
            yPosition -= 18;

            // Conteúdo da seção
            String[] linhas = conteudo.split("\n");
            for (String linha : linhas) {
                List<String> linhasQuebradas = quebrarLinha(linha, PDType1Font.HELVETICA, 8, pageWidth);

                for (String linhaQuebrada : linhasQuebradas) {
                    // Verificar se precisa de nova página
                    if (yPosition < 50) {
                        contentStream.close();
                        PDPage newPage = new PDPage();
                        document.addPage(newPage);
                        contentStream = new PDPageContentStream(document, newPage);
                        yPosition = 750;
                        pageWidth = newPage.getMediaBox().getWidth() - (2 * margin);
                    }

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 8);
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(linhaQuebrada);
                    contentStream.endText();
                    yPosition -= 10;
                }
            }

            yPosition -= 10; // Espaço entre seções
            return new PDFResult(contentStream, yPosition);
        }

        // METODO PARA QUEBRAR LINHAS BASEADO NA LARGURA
        private static List<String> quebrarLinha(String texto, PDFont font, float fontSize, float maxWidth) throws Exception {
            List<String> linhas = new ArrayList<>();

            if (texto == null || texto.isEmpty()) {
                return linhas;
            }

            String[] palavras = texto.split(" ");
            StringBuilder linhaAtual = new StringBuilder();

            for (String palavra : palavras) {
                String testeString = linhaAtual.length() == 0 ? palavra : linhaAtual + " " + palavra;
                float largura = font.getStringWidth(testeString) / 1000 * fontSize;

                if (largura <= maxWidth) {
                    if (linhaAtual.length() > 0) {
                        linhaAtual.append(" ");
                    }
                    linhaAtual.append(palavra);
                } else {
                    if (linhaAtual.length() > 0) {
                        linhas.add(linhaAtual.toString());
                        linhaAtual = new StringBuilder(palavra);
                    } else {
                        // Palavra muito longa, quebrar forçadamente
                        linhas.add(palavra.substring(0, Math.min(palavra.length(), 80)));
                    }
                }
            }

            if (linhaAtual.length() > 0) {
                linhas.add(linhaAtual.toString());
            }

            return linhas;
        }

        //METODO PARA LIMPAR CARACTERES ESPECIAIS
        private static String limparTexto(String texto) {
            if (texto == null || texto.isEmpty()) {
                return "";
            }

            return texto
                    .replaceAll("\r\n", "\n")     // Converte CRLF para LF
                    .replaceAll("\r", "\n")        // Converte CR para LF
                    .replaceAll("\t", "    ");      // Converte TAB para espaços
        }

        // 🎨 MÉTODO PARA FORMATAR JSON
        private static String formatarJson(String json) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object jsonObject = mapper.readValue(json, Object.class);
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            } catch (Exception e) {
                return json;
            }
        }
    }

    public static String getBody(String sistema, String payload) {
        try {
            String basePath = new File("").getAbsolutePath() + CAMINHO_ARQUIVOS_DATA + sistema;
            File file = new File(basePath + File.separator + payload);

            if (!file.exists() || file.isDirectory()) {
                throw new RuntimeException("Arquivo não encontrado: " + file.getAbsolutePath());
            }

            String body = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

            if (request == null) {
                request = getRequestSpec();
            }

            request = request.body(body);
            return body;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o payload JSON: " + payload + " para sistema " + sistema, e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao montar o payload " + payload + " para sistema " + sistema, e);
        }
    }

    public void logResponseBody(Response response) {
        String responseBody = response.getBody().asString();

        try {
            // Formata como JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Object json = mapper.readValue(responseBody, Object.class);
            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

            logger.info("Response Body:");
            String[] lines = prettyJson.split("\n");
            for (String line : lines) {
                logger.info(line);
            }
        } catch (Exception e) {
            // Se não for JSON, mostra como texto
            logger.info("Response Body: " + responseBody);
        }
    }
}