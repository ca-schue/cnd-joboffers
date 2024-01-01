# User Service:

- Responsible for lifecycle of user profiles and companies
- Manages persisting user and company data.
- Ensures consistency between users and companies as well as between users and accounts.

## Noteworthy Use Cases

<details xmlns="http://www.w3.org/1999/html">

<summary><b>Consistent states between users and companies</b> - Atomic transactions</summary>

- If invited, a user can be member of none, one or more companies.\
    ![Company invite](./figures/user-service/company-invite.svg)


- On company deletion by the owner, all member relations are dissolved. \
    ![Company deletion](./figures/user-service/company-deletion.svg)

</details>


<details xmlns="http://www.w3.org/1999/html">

<summary><b>Consistent states between users and accounts</b> - Publishing asynchronous events</summary>

The User Service mandates an asynchronous coupling between <b>one</b> user profile and <b>one</b> account based on two constraints:
<pre>
1) UserId == AccountId
2) User Profile Email != Account Email
</pre> 

- After creating a user profile, a published event will [create a link to an account](./auth-service.md). \
    ![User profile creation](./figures/user-service/user-profile-created.svg)


- When a user profile is deleted, the coupling with an account is [dissolved by a published event](./auth-service.md). \
  ![User profile deletion](./figures/user-service/user-profile-deleted.svg)
  
</details>


## Technology Stack
- Language: Java (developed with JDK 21)
- Build Orchestration: Gradle 8.4
- [Featured Dependencies](../userservice/build.gradle):
  - Spring OAuth2 Resource Server 
  - Spring Kafka Template
  - Spring MongoDB
  - Mockito (Unit & Component tests)

## Microservice Architecture

The microservice follows a scalable design principle, employing a "Hexagonal Architecture" for enhanced modularity.
This approach facilitates efficient scaling of accounts, clients and services while promoting code clarity and maintainability.

The "Hexagonal Architecture" was implemented according to the guidelines of the accompanying lecture [[1]](https://online-lectures-cs.thi.de/sesa-ws2021-inf/#/6).
These are based on the "Ports and Adapters" pattern by Alistair Cockburn [[2]](https://alistair.cockburn.us/hexagonal-architecture/) and were simplified in a Miro board of the lecture [[3]](https://miro.com/app/board/o9J_llo8IL8=/?moveToWidget=3458764538112404057&cot=14):

<blockquote>

<cite><b>"The rule to obey is that code pertaining to the 'inside' part should not leak into the 'outside' part."</b> <a href="https://alistair.cockburn.us/hexagonal-architecture/">[2]</a></cite>
<details>
<summary>Implemented with ...</summary>

- Hexagonal layers are implemented as Gradle subprojects: `adapters:in`, `domain`, `application`, `adapters:out`.
- Inner Gradle subprojects do not receive any `dependency` from outer subprojects.
</details>
</blockquote>

<blockquote>

<cite><b>No access among the adapters</b> <a href="https://miro.com/app/board/o9J_llo8IL8=/?moveToWidget=3458764538112404057&cot=14">[3]</a></cite>
<details>
<summary>Implemented with ...</summary>

- Input and output adapters separated by Java modules and Gradle projects `adapters:in` and `adapters:out`.
- Java class visibility reduced to package level whenever possible.
</details>
</blockquote>

<blockquote>
<cite><b>"[…] use cases should generally be written at [...] the inner hexagon, to specify the functions [...]"</b> <a href="https://alistair.cockburn.us/hexagonal-architecture/">[1]</a></cite>

<cite><b>Limit input adapter access to domain layer interfaces</b> <a href="https://miro.com/app/board/o9J_llo8IL8=/?moveToWidget=3458764538112404057&cot=14">[3]</a></cite>

<details>
<summary>Implemented with ...</summary>

- The Input Adapter Gradle Subproject does not get access to the subproject of the Application Layer.
</details>
</blockquote>

<blockquote>

<cite><b>"The [domain] logic is implemented without relation to [...] technology"</b><a href="https://miro.com/app/board/o9J_llo8IL8=/?moveToWidget=3458764538112404057&cot=14">[2]</a></cite>

<p style="margin-left: 10px">Implemented by design.</p>
</blockquote>

***Note:*** Increase webpage size for better readability of the vector graphic below

![Actor role evolution](./figures/user-service/user-service.svg)

### Comments on Design decisions

<details>
  <summary><b>Input Adapters</b></summary>

  - HTTP:
    - Endpoint Controller:
      - User and Company Management use cases are performed through HTTP calls on `UserHttpControllerImpl` and `CompanyHttpControllerImpl`
      - Received DTOs are mapped to the domain model
      - [User Service HTTP endpoints are defined with OpenApi](../userservice/adapters/src/main/resources/openapi-us-rest-spec.yaml)
    - Endpoint Security:
      - With Spring's `OAuth2 Resource Server`, selected endpoints are authenticated by validating "Account JWT" or "Client JWT" using the JWK of the Auth Service (obtained via oauth2 HTTP Endpoint).   
      - Spring Security `Authorization Managers` allow authorizing endpoint access.
</details>

<details>
  <summary><b>Domain Model</b></summary>

  - Domain models `User` and `Company` designed using DDD principles.
  - This design was chosen to ensure atomic transactions between users and companies.
</details>

<details>
  <summary><b>Output Adapters</b></summary>

- Repositories: MongoDB
  - Repository ports for persisting account and client data are implemented through Spring's `MongoRepository` module. The URL for the remote database is set in the `AUTH_SERVICE_MONGODB_URI` environment variable.
  - Mapping from the domain model to persisting entities is done through DAOs
- Events: Kafka
  - Event Ports for distributing user and company data asynchronously to other services are implemented using Spring's `KafkaTemplates`
  - [User Service Events are defined with OpenApi](../userservice/adapters/src/main/resources/openapi-us-event-spec.yaml)
  - Mapping from the domain model to persisting entities is done through generated OpenApi classes.

</details>

## Microservice Build and Test
- Workdir: `cnd-joboffers/userservice/`
- Build `.jar` with gradle wrapper: (will download gradle 8.4)
  ```
  ./gradlew bootJar
  ```
- Optionally: Execute fully mocked unit tests to verify core functionality
  ```
  ./gradlew test
  ```
- Optionally: Execute end-to-end integration tests to verify use cases
  ```
  ./gradlew integrationTest
  ```
For further details and information on the execution of the microservice, please follow the steps of the [respective execution environment](../README.md#run-this-application-).


## Environment Variables for Configuration

```  
AUTH_SERVICE_PORT = {Spring will start the User Service HTTP server on this port}
AUTH_SERVICE_MONGODB_URI = {URI of remote MongoDB database for user and company data}

KAFKA_BOOTSTRAP_SERVER = {URL of kafka message broker}

INTERNAL_JWT_ISSUER_URL = {FQDN of the Auth Service set in "iss" JWT claim}
```