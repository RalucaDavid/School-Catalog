# School Catalog
The project was originally designed as a console-accessible core catalog for the Programming Environments and Tools topic with database functionality. After that, the project evolved into a more advanced web application. \
It was created using the Java programming language and the following advanced components:
<ul>
  <li>
    <strong>GUI</strong> - the graphic interface web-based that has all the functionalities of the catalog ( Made with the Angular framework and the Tailwind CSS framework )
  </li>
  <li>
    <strong>Networking</strong> - the communication between client and server ( Made with the Dropwizard framework )
  </li>
  <li>
    <strong>Database</strong> - the data that the server will use will be handled by a database ( Made with JPA and PostgreSQL )
  </li>
  <li>
    <strong>Security</strong> - the authentication is secure so that only users can have access to the functionalities of the catalog ( Made with JWT )
  </li>
</ul>

# Features
- **Authentication** - register and login, with passwords hashed using BCrypt and sessions handled through JWT tokens
- **Roles** - every user has a role (`ADMIN`, `TEACHER` or student) and the routes are protected both on the server and on the client
- **Admin panel** - create subjects, list subjects and teachers, add or remove teachers from a subject
- **Teachers** - see the subjects they teach, add or remove students and give grades and final grades
- **Students** - see the subjects they are enrolled in together with their grades

# Tech stack
| Part | Technologies |
|---|---|
| Backend | Java 21, Dropwizard 2, JPA (EclipseLink), JJWT, jBCrypt, Lombok |
| Frontend | Angular 15, Tailwind CSS |
| Database | PostgreSQL 14 (Docker) |

# Project structure
```
backend/    Dropwizard REST API (controllers, JWT auth, JPA entities and services)
frontend/   Angular application
database/   docker-compose file for the PostgreSQL database
```

# Running locally
1. **Database** - start PostgreSQL with Docker:
   ```
   cd database
   docker compose up -d
   ```
2. **Backend** - from the `backend` folder, run the `org.school.App` class (for example from IntelliJ IDEA). The server starts on `http://localhost:8080` and reads the `configuration.yml` file from the working directory.
3. **Frontend** - from the `frontend` folder:
   ```
   npm install
   npm start
   ```
   The application is available at `http://localhost:4200`.

# Team
[Cristea Ștefan](https://github.com/ksenonadv) \
[David Raluca](https://github.com/RalucaDavid)

# Photos
![image](https://github.com/RalucaDavid/School-Catalog/assets/117584603/f8f8975f-d1ef-4b94-af0b-8c7fae5375f0)
![image](https://github.com/RalucaDavid/School-Catalog/assets/117584603/0a6e5f0c-988f-47fa-b4e4-e896ce73571f)
![image](https://github.com/RalucaDavid/School-Catalog/assets/117584603/9c0b6d68-4a42-41d4-a011-fd71f2e45798)
![image](https://github.com/RalucaDavid/School-Catalog/assets/117584603/4b9b1871-5b64-49c4-b9b5-c7b720bc4209)
