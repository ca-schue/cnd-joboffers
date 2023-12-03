# Cloud-native development (CND) Project: "Joboffers"

Very brief overview of project context (Student project) and structure of this readme ...
- Microservice application for student project for CND subject ... smth like this
- [Fulfillment of requirements (German)]()

## Application Use Cases

![Use Cases](./.documentation/figures/use-cases.svg)

What does this application do from a user perspective? only use-case level no technical details:

Actor role evolution:

![Actor role evolution](./.documentation/figures/actor-role-evolution.svg)

- Actor: Non-Registered User (Guest):
  - List Job offers etc. ...
  - Can create Account with OIDC or Basic Auth.
- Actor: Registered + Authenticated Account (No Profile):
  - ... TODO: Was haben wir hier nochmal gesagt?
  - Must complete user profile for use cases x y and z
- Actor: Registered + Authenticated User (Account + User Profile):
  - Verified User = Account + User Profile
  - Can perform core use cases a b and c ...
- Actor: Company Owner
  - ...
- Actor: Company Member
  - ...

## Run this application ...
- ... [locally 'bare metal' (un-containerized)](.documentation/bare-metal.md)
- ... [locally as seperated docker containers](.documentation/docker-seperated.md)
- ... [locally using docker compose](.documentation/docker-compose.md)
- ... [on a kubernetes cluster](.documentation/kubernetes-helm.md)

## System architecture

![System Architecture](./.documentation/figures/system-architecture.svg)

<details xmlns="http://www.w3.org/1999/html">
<summary><b>Frontend</b> (<a href=".documentation/frontend.md">detailed documentation</a>)
  </summary>
  
  - TODO: Converts Use Cases to Microservice Calls + Handles OIDC Authentication
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Auth Service</b> (<a href=".documentation/auth-service.md">detailed documentation</a>)</summary>
  
  - Implements external and internal authentication and authorization across the application using JWT
  - Synchronizes accounts with user profiles by processing asynchronous User Service events.
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>User Service</b> (<a href=".documentation/user-service.md">detailed documentation</a>)</summary>
  
  - Responsible for consistent lifecycle of user profiles and companies.
  - Ensures consistency between accounts and user profiles and account by acting as an asynchronous ordering party for the Auth Service.
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Career Service</b> (<a href=".documentation/career-service.md">detailed documentation</a>)</summary>
   
    - Logic: Job offer & application Management
      - Calls User Service to do ...     
      - Listens to Events on Kafka ...
    - Persistence: Job Data with Event Sourcing
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Notification Service</b> (<a href=".documentation/notification-service.md">detailed documentation</a>)</summary>

  - Logic: User Profile & Company Management
      - Listens to xyz Events on Kafka and does abc
</details>

## Featured technology  stack

TODO...
- Frontend Implementation: React
- Service Implementation: Spring Boot
- Authentication: OIDC, OAuth2 and JWT
- Communication: (unsecured) HTTP & Kafka + OpenAPI
- Persistence: 
  - Event Sourcing
  - MongoDB


