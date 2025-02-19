# Backend

## Instruções para execução

Abaixo estão os passos para executar a aplicação na sua máquina:

1. Instale o PostgreSQL, configure-o e crie um banco de dados com o nome "database" (ou outro nome de sua escolha);
2. No arquivo [application.yml](src/main/resources/application.yml), coloque suas credenciais do banco de dados (usuário, senha e nome do banco no campo `url`, caso tenha criado o banco com outro nome);
3. Abra a pasta Backend em um terminal com o comando (caso não esteja usando uma IDE):
```
cd Backend
```
4. Execute a aplicação com o comando:
```
mvn spring-boot:run
```

> [!NOTE]  
> A aplicação utiliza o Flyway para gerenciar alterações no banco de dados, então, ao executar a aplicação pela primeira vez, as tabelas necessárias serão criadas automaticamente.

> [!NOTE]  
> Um usuário ADMIN será criado automaticamente na primeira execução, com as seguintes credenciais:
> - **Usuário:** admin
> - **E-mail:** admin@admin.com
> - **Senha:** admin

> [!NOTE]  
> É possível criar seu próprio usuário, mas ele será do tipo USER, que não tem acesso a certas funcionalidades no backend.

## Postman

A aplicação também possui uma [coleção do Postman](Task_Management_Collection.postman_collection.json) com todos os endpoints e um [arquivo de ambiente](task-management.postman_environment.json) que contém a URL base do backend e a variável que armazenará o token de login.

## Swagger

A aplicação conta com a ferramenta Swagger para sua documentação. Para acessá-la, execute a aplicação e acesse a URL:  
[http://localhost:8080/task-management/api/swagger-ui/index.html](http://localhost:8080/task-management/api/swagger-ui/index.html)

> [!NOTE]  
> Para realizar requisições via Swagger, será necessário fazer a requisição de login, copiar o token e adicioná-lo clicando no botão "Authorize".
