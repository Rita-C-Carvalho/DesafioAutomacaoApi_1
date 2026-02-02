#language: pt
@DemoQA @ACCENTURE
Funcionalidade: Validação de testes na API DemoQA

  @DemoQA_User_Management_01
  Cenario: Criar usuário e validar properties atualizadas
    Dado que eu crie um usuário na DemoQA
    E valido o status code com sucesso "201"

  @DemoQA_Token_Management_01
  Cenario: Gerar token e validar properties atualizadas
    Dado que eu crie um usuário na DemoQA
    E gero um token de acesso
    E valido o status code com sucesso "200"
    E verifico se a propriedade "CURRENT_FULL_TOKEN" existe no sistema

  @DemoQA_Authorization_01
  Cenario: Verificar autorização do usuário
    Dado que eu crie um usuário na DemoQA
    E gero um token de acesso
    E verifico se o usuário está autorizado
    E valido o status code com sucesso "200"

  @DemoQA_Books_Management_01
  Cenario: Listar livros disponíveis e validar properties
    Quando listo os livros disponíveis
    E valido o status code com sucesso "200"

  @DemoQA_Book_Rental_Multiple
  Cenario: Alugar múltiplos livros para usuário
    Dado que eu crie um usuário na DemoQA
    E gero um token de acesso
    Quando listo os livros disponíveis
    E alugo os seguintes livros:
      | bookKey            |
      | gitPocketGuide     |
      | learningJavaScript |
    E listo os detalhes do usuário com os livros alugados



  @DemoQA_User_Details_01
  Cenario: Listar detalhes do usuário com livros alugados
    Dado que eu crie um usuário na DemoQA
    E gero um token de acesso
    Quando listo os livros disponíveis
    E alugo os seguintes livros:
      | bookKey            |
      | gitPocketGuide     |
      | learningJavaScript |
    E listo os detalhes do usuário com os livros alugados
    E valido o status code com sucesso "200"


  @DemoQA_Complete_Flow_01
  Cenario: Fluxo completo de criação, autenticação e aluguel
    Dado que eu crie um usuário na DemoQA
    E valido o status code com sucesso "201"
    E gero um token de acesso
    E valido o status code com sucesso "200"
    E verifico se o usuário está autorizado
    E valido o status code com sucesso "200"
    Quando listo os livros disponíveis
    E valido o status code com sucesso "200"
    E alugo os seguintes livros:
      | bookKey            |
      | gitPocketGuide     |
      | learningJavaScript |
    E valido o status code com sucesso "201"
    E listo os detalhes do usuário com os livros alugados
    E valido o status code com sucesso "200"


