package br.com.teste.accenture.steps;

import br.com.teste.accenture.services.BookService;
import br.com.teste.accenture.services.UserService;
import br.com.teste.accenture.tools.PropertiesLoader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static br.com.teste.accenture.utils.Constants.*;

public class DemoQASteps {

    private static final Logger logger = LoggerFactory.getLogger(DemoQASteps.class);
    private UserService userService = new UserService();
    private BookService bookService = new BookService();
    private String token;

    @Dado("que eu crie um usuário na DemoQA")
    public void criarUsuario() {

        userService.criarUsuario(BaseSteps.getApiUtils());
    }

    @E("gero um token de acesso")
    public void gerarToken() {

        token = userService.gerarToken(BaseSteps.getApiUtils());
    }

    @E("verifico se o usuário está autorizado")
    public void verificarAutorizacao() {

        userService.verificarAutorizacao(BaseSteps.getApiUtils());
    }

    @Quando("listo os livros disponíveis")
    public void listarLivros() {
        bookService.listarLivros(BaseSteps.getApiUtils());
    }

    @E("alugo os seguintes livros:")
    public void alugarMultiplosLivros(DataTable dataTable) {
        String token = PropertiesLoader.getFromSystem(CURRENT_FULL_TOKEN, DEMOQA_VALUE);

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token não encontrado no System Properties. Execute a geração de token primeiro.");
        }

        List<Map<String, String>> livros = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> livro : livros) {
            String bookKey = livro.get(BOOK_KEY);
            logger.info("Alugando livro: " + bookKey);
            bookService.alugarLivro(BaseSteps.getApiUtils(), bookKey, token);
        }
    }

    @E("listo os detalhes do usuário com os livros alugados")
    public void listarDetalhesUsuario() {
        bookService.listarDetalhesUsuario(BaseSteps.getApiUtils(), token);
    }

    @Entao("verifico se a propriedade {string} existe no sistema")
    public void verificarPropriedadeExiste(String propriedade) {
        boolean exists = PropertiesLoader.propertyExistsInSystem(propriedade, DEMOQA_VALUE);
        Assert.assertTrue("Propriedade '" + propriedade + "' deve existir no System Properties", exists);

        String valor = PropertiesLoader.getFromSystem(propriedade, DEMOQA_VALUE);
        logger.info("Propriedade '" + propriedade + "' encontrada com valor: " + valor);
    }

}