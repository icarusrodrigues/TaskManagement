# TaskManagement

[![en](https://img.shields.io/badge/lang-en-blue.svg)](README.md)

Um projeto Full Stack, com uma aplicação Backend em Java Spring Boot para gerenciamento de tarefas e um Frontend em React.

Após clonar este repositório, siga as instruções para executar o [Backend](Backend/README.pt-br.md) e o [Frontend](Frontend/README.md).

## Funcionalidades

Este projeto possui as seguintes funcionalidades:

- Login com Nome de Usuário ou E-mail e senha.
- Cadastro de usuário.
- Logout.
- Lista de tarefas do usuário logado. Por padrão, as tarefas serão separadas em 3 colunas com base no status: **Pendente, Em Progresso e Concluída**, ordenadas em ordem crescente pela data de vencimento, e podem ser filtradas por status.
- Criação de tarefas, que devem ter um título e uma descrição. Caso não seja especificada uma data de vencimento, será definida automaticamente para **1 semana após a data atual**.
- Atualização de tarefas:
    - Se a tarefa estiver com status **Pendente**, é possível alterar o título, a descrição e o prazo.
    - Se a tarefa estiver **Em Progresso**, só é possível alterar o título e a descrição.
    - Se a tarefa estiver **Concluída**, seus campos não podem ser alterados.
- Exclusão de tarefas.
- Iniciar e concluir tarefas.  

