package br.com.teste.accenture.exceptions;

public class ExceptionMessages {

    // Mensagens de erro para operações de usuário
    public static final String USUARIO_JA_EXISTE = "Usuário já existe no sistema";
    public static final String FALHA_AO_CRIAR_USUARIO = "Falha ao criar usuário";
    public static final String ERRO_AO_CRIAR_USUARIO = "Erro ao criar usuário";
    public static final String FALHA_AO_LISTAR_DETALHES_USUARIO = "Falha ao listar detalhes do usuário";

    public static final String ERRO_AO_LISTAR_DETALHES_USUARIO = "Erro ao listar detalhes do usuário";

    // Mensagens de erro para operações com token
    public static final String FALHA_AO_GERAR_TOKEN = "Falha ao gerar token";
    public static final String ERRO_AO_GERAR_TOKEN = "Erro ao gerar token";

    // Mensagens de erro para operações com autirização
    public static final String FALHA_VERIFICAR_AUTORIZACAO = "Falha ao verificar a autorização";
    public static final String ERRO_VERIFICAR_AUTORIZACAO = "Erro ao verificar a autorização";

    // Mensagens de erro para operações com livros
    public static final String FALHA_AO_LISTAR_LIVROS = "Falha ao listar livros";
    public static final String ERRO_AO_LISTAR_LIVROS = "Erro ao listar livros";
    public static final String FALHA_AO_ALUGAR_LIVRO = "Falha ao alugar livros";
    public static final String ERRO_AO_ALUGAR_LIVRO = "Erro ao alugar livros";
    public static final String ERRO_AO_OBTER_DADOS_DO_LIVRO = "Erro ao obter dados do livro: ";

    // Mensagens de erro para operações login
    public static final String ERRO_AO_REALIZAR_LOGIN = "Username ou Password não encontrados no System Properties. Execute a criação de usuário primeiro.";

    // Mensagens de erro para arquivo properties
    public static final String ARQUIVO_PROPERTIES_NAO_ENCONTRADO = "";
    public static final String ERRO_AO_LER_ARQUIVO_PROPERTIES = "";
    public static final String ERRO_AO_CARREGAR_ARQUIVO_PROPERTIES = "";
    public static final String CHAVE_NAO_ENCONTRADA_NO_ARQUIVO_PROPERTIES = "";

    // Mensagens de erro para salvar dados no arquivo JSON
    public static final String ERRO_AO_SALVAR_DADOS_NO_ARQUIVO_JSON = "Erro ao salvar dados no arquivo JSON";
}
