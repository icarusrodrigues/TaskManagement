# TaskManagement

[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](README.pt-br.md)

A Full Stack project, with a Backend application in Java Spring Boot for Task management, and Frontend in React.

After clone this repository, follow the instructions to run the [Backend](Backend/README.md) and [Frontend](Frontend/README.md).

## Features

This project has the following features: 

- Login with Username or Email, and password.
- User Sign Up.
- Logout.
- List of logged in user tasks. By default, tasks will be separated into 3 columns based on status: Pending, In Progress and Completed, sorted in ascending order of due date, and can be filtered by status).
- Creation of tasks, which must have a title and description, and if you do not want to specify a due date, a due date will be set for 1 week after the current date.
- Task update. 
  - If the task status is **Pending**, you can change the title, description, and deadline. 
  - If the task status is **In Progress**, you can only change the title and description. 
  - If the task status is **Completed**, you cannot change its fields.
- Delete tasks.
- Start and Complete tasks.