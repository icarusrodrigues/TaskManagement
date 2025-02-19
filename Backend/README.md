# Backend

## Instructions for running

Below are the steps to run the application on your machine:

1. Install PostgreSQL, configure it, and create a database with name "database" (or another name of your choice);
2. In file [application.yml](src/main/resources/application.yml) put your database credentials (user, password and name of database in url field if you create a database with another name);
3. Open the Backend folder in a terminal with the command (If not using an IDE):
```
cd Backend
```
4. Run the application, using the command:
```
mvn spring-boot:run
```

> [!NOTE]  
> The application uses flyway to manage changes to the database, so when you run the application for the first time, the necessary tables will be automatically created.

> [!NOTE]  
> An ADMIN user will be created automatically the first time you run the codes, with the following credentials:
> - **Username**: admin
> - **Email**: admin@admin.com
> - **Password**: admin
> 
> With this user it is possible to use all Backend endpoints, but only via postman, it is not yet possible to access the features via Frontend.

> [!NOTE]  
> It is possible to create your own user, but it will be of type USER, which cannot access certain functionalities in the backend.

## Postman

In the application there is also a [postman collection](Task_Management_Collection.postman_collection.json) with all endpoints, and an [environment file](task-management.postman_environment.json) that contains the backend base url and the variable that will receive the login token.

## Swagger

The application has the Swagger tool to document it. To access it, while running the application and access the url: [http://localhost:8080/task-management/api/swagger-ui/index.html](http://localhost:8080/task-management/api/swagger-ui/index.html)

> [!NOTE]  
> To make requests via Swagger, you will need to make the login request, copy the token, and add it by clicking the Authorize button 
