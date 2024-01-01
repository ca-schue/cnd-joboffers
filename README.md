## Project Context

This project "Joboffers" is the submission of [@Thalmaier](https://github.com/Thalmaier) and [@ca-schue](https://github.com/ca-schue) for the assignment of the master course Cloud-native development (CND) at Technische Hochschule Ingolstadt in the winter semester 2023/24.
It aims to demonstrate the hexagonal architecture pattern for microservices in a complex, realistic and cloud-based context at its best.

<details xmlns="http://www.w3.org/1999/html">
<summary><b>Fulfillment of requirements (German) </b></summary>

<blockquote>
<cite><b>"Das Anwendungsbeispiel muss je Team-Mitglied mindestens aus zwei Diensten bestehen und Daten erfassen, ein Dienst sollte Anfragen über HTTP entgegen nehmen."</b></cite>

- [x] We have four microservices, which all store their data in a database and are connected over http (except notification service) and events:
- Auth Service (Authentication) by Carl
- User Service (User Management) by Carl
- Career Service (Main Business Logic) by Simon
- Notification Service (Logs events to console to demonstrate a possible use case) by Simon
</blockquote>

<blockquote>
<cite><b>"Realisieren Sie je Team-Mitglied mindestens einen Dienst unter Verwendung eines modernen Architekturansatzes."</b></cite>

- [x] All our services use a hexagonal architecture

</blockquote>

<blockquote>
<cite><b>"Ergänzen Sie ein kleines Frontend, welches die realisierte Backend-Funktionalität nutzt."</b></cite>

- [x] We created a frontend in react to use our application in a "real world" scenario
</blockquote>

<blockquote>
<cite><b>"Ergänzen Sie für die Dienste einfache Unit-Tests, die den jeweiligen Dienst überprüfen, eine vollständige Test-Abdeckung ist nicht notwendig."</b></cite>

- [x] Use Cases are verified by end-to-end integration tests in the respective microservice.
  Core functionality is verified by either fully mocked unit tests or slice tests with selected autowired components.
</blockquote>

<blockquote>
<cite><b>"Dokumentieren Sie kurz, was nötig ist, um die Dienste in einer VM zu installieren und zu starten."</b></cite>

- [x] Documented below
</blockquote>

<blockquote>
<cite><b>"Erstellen Sie Dockerfiles, welches je einen Dienst zur Ausführung bringt und Dokumentieren Sie, wie diese Dienste installiert und gestartet werden."</b></cite>

- [x] Documented below
</blockquote>

<blockquote>
<cite><b>"Erstellen Sie ein Docker-Compose-File, welche alle Dienste konfiguriert und Dokumentieren Sie, wie diese Datei genutzt wird, ergänzen Sie einen Load-Balancer (z.B. nginx) um Anfragen zu verteilen. Achten Sie auf die Konfiguration Ihrer Volumes."</b></cite>

- [x] Documented below
</blockquote>

<blockquote>
<cite><b>"Erstellen Sie Kubernetes-Manifeste, welche Ihre Dienste konfiguriert."</b></cite>

- [x] Documented below
</blockquote>

<blockquote>
<cite><b>"Automatisieren Sie den Bauprozess (die Bereitstellung ist nicht zwingend erforderlich) in einer wählbaren Umgebung, verwenden Sie hierfür z.B. das GitLab und verfügbare GitLab-CI."</b></cite>

- [x] We use Github Actions to build our applications, see `.github/workflows`
</blockquote>

<blockquote>
<cite><b>"Skizzieren Sie die realisierte finale Service-Architektur und die innere Architektur eines Dienstes."</b></cite>

- [x] See `.documentation` folder
</blockquote>

</details>

## Application Use Cases

In this application, either a newly registered account or a Google account is linked to a user profile.
This allows users to set up companies and invite other users as company members to publish job offers.
All users can then apply for the published job offers of any company.

![Use Cases](./.documentation/figures/use-cases.svg)


Actor role evolution:

![Actor role evolution](./.documentation/figures/actor-role-evolution.svg)

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
  
  - Uses the provided HTTP Api of the services to implement the use cases in a GUI
  - Handles OIDC Authentication 
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
   
  - Contains main business logic to create, edit and delete job offers / job applications
  - Has integration with the user-service over HTTP Api and Kafka events
  - Uses CQRS and Event-sourcing
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Notification Service</b> (<a href=".documentation/notification-service.md">detailed documentation</a>)</summary>

  - Small microservice that listens to events sent to the kafka event bus and prints information out to the console
</details>


