## Project Context

This project "Joboffers" is the submission of Simon T. ([@Thalmaier](https://github.com/Thalmaier)) and Carl S. ([@ca-schue](https://github.com/ca-schue)) for the assignment of the master course Cloud-native development (CND) at Technische Hochschule Ingolstadt in the winter semester 2023/24.
It aims to demonstrate the hexagonal architecture pattern for microservices in a complex, realistic and cloud-based context at its best.

<details xmlns="http://www.w3.org/1999/html">
<summary><b>Fulfillment of requirements (German) </b></summary>

<blockquote>
<cite><b>"Das Anwendungsbeispiel muss je Team-Mitglied mindestens aus zwei Diensten bestehen und Daten erfassen, ein Dienst sollte Anfragen über HTTP entgegen nehmen."</b></cite>

- [x] Four microservices acquire data via the frontend or inter-service communication via HTTP and additionally Kafka events. For more details, refer to section ["System architecture"](#system-architecture):
  - <b><i>Auth service</i></b> (Carl)
  - <b><i>User service</i></b> (Carl)
  - <b><i>Career service</i></b> (Simon)
  - <b><i>Notification service</i></b> (Simon)

  The frontend and the deployment of the application was developed in an equal split of work.

</blockquote>


<blockquote>
<cite><b>"Realisieren Sie je Team-Mitglied mindestens einen Dienst unter Verwendung eines modernen Architekturansatzes."</b></cite>

- [x] Our personal commitment was to implement each of the four services in the best possible way according to the guidelines of the hexagonal architecture as described in the lecture and literature. A comprehensive architecture description can be found in the [detailed service documentation of the system architecture](#system-architecture).

</blockquote>


<blockquote>
<cite><b>"Ergänzen Sie ein kleines Frontend, welches die realisierte Backend-Funktionalität nutzt."</b></cite>

- [x] We created a frontend in React in order to deploy the microservices in a realistic production environment.
</blockquote>


<blockquote>
<cite><b>"Ergänzen Sie für die Dienste einfache Unit-Tests, die den jeweiligen Dienst überprüfen, eine vollständige Test-Abdeckung ist nicht notwendig."</b></cite>

- [x] The core functionality is verified by either fully mocked unit tests or slice tests with selected autowired components. Additionally, relevant use cases are verified by end-to-end integration tests in the respective microservice.
</blockquote>


<blockquote>
<cite><b>"Dokumentieren Sie kurz, was nötig ist, um die Dienste in einer VM zu installieren und zu starten."</b></cite>

- [x] This readme is the starting point for the documentation.
   Detailed instructions are provided for each of the four execution environments...
  - ["bare metal"](.documentation/bare-metal.md) (equivalent to "local VM"), 
  - as ["separate Docker containers"](.documentation/docker-seperated.md), 
  - using ["Docker Compose"](.documentation/docker-compose.md) 
  - and on a ["Kubernetes cluster with Helm"](.documentation/kubernetes-helm.md).
</blockquote>


<blockquote>
<cite><b>"Erstellen Sie Dockerfiles, welches je einen Dienst zur Ausführung bringt und Dokumentieren Sie, wie diese Dienste installiert und gestartet werden."</b></cite>

- [x] Dockerfiles were written for each service as well as for the React frontend. The configuration and execution is described in ["Run the Application as Separate Docker Containers"](.documentation/docker-seperated.md).
</blockquote>


<blockquote>
<cite><b>"Erstellen Sie ein Docker-Compose-File, welche alle Dienste konfiguriert und Dokumentieren Sie, wie diese Datei genutzt wird, ergänzen Sie einen Load-Balancer (z.B. nginx) um Anfragen zu verteilen. Achten Sie auf die Konfiguration Ihrer Volumes."</b></cite>

- [x] A Docker-Compose file starts all four services, the React frontend and an Ngninx load balancer in an ordered sequence. In addition, the entire auxiliary infrastructure including all volumes is set up and configured.
An intuitive concept with environment variables was established to simplify configuration. See ["Run the application with Docker Compose"](.documentation/docker-compose.md).
</blockquote>


<blockquote>
<cite><b>"Erstellen Sie Kubernetes-Manifeste, welche Ihre Dienste konfiguriert."</b></cite>

- [x] As a cloud-native application, Kubernetes manifests were written for the entire system context. In order to achieve a realistic production environment, a Helm Chart was also written for deployment on a cluster. Detailed information can be found under ["Run the Application on a Kubernetes Cluster using Helm"](.documentation/kubernetes-helm.md).
</blockquote>


<blockquote>
<cite><b>"Automatisieren Sie den Bauprozess (die Bereitstellung ist nicht zwingend erforderlich) in einer wählbaren Umgebung, verwenden Sie hierfür z.B. das GitLab und verfügbare GitLab-CI."</b></cite>

- [x] The application was developed using CI/CD pipelines in GitHub Actions.
  In each pipeline, the service is built and comprehensively tested - depending on the test duration, additional integration tests are performed. At the end of each pipeline, a Docker image is created and uploaded to the [Docker Hub Registry](https://hub.docker.com/repositories/caschuen). Please refer to [`.github/workflows`](.github/workflows).
</blockquote>


<blockquote>
<cite><b>"Skizzieren Sie die realisierte finale Service-Architektur und die innere Architektur eines Dienstes."</b></cite>

- [x] Section ["System architecture"](#system-architecture) illustrates the service interactions.
  Beyond this, for each individual service, its relevance in the overall system context as well as its technical implementation are documented together with a detailed visualisation and explanation of the internal architecture.
</blockquote>


</details>

## Application Use Cases

In this application, either a newly registered account or a Google account is linked to a user profile.
This allows users to set up companies and invite other users as company members to publish job offers.
All users can then apply for the published job offers of any company.

![Use Cases](./.documentation/figures/use-cases.svg)

This results in the following role development of the actor:

![Actor role evolution](./.documentation/figures/actor-role-evolution.svg)

## Run this application ...
- ... [locally 'bare metal' (un-containerized)](.documentation/bare-metal.md)
- ... [locally as seperated Docker containers](.documentation/docker-seperated.md)
- ... [locally using Docker Compose](.documentation/docker-compose.md)
- ... [on a Kubernetes cluster using Helm](.documentation/kubernetes-helm.md)

## System architecture

![System Architecture](./.documentation/figures/system-architecture.svg)

<details xmlns="http://www.w3.org/1999/html">
<summary><b>Frontend </b> by Carl & Simon (<a href=".documentation/frontend.md">detailed documentation</a>)
  </summary>
  
  - Uses the provided HTTP Api of the services to implement the use cases in a GUI
  - Handles OIDC Authentication 
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Auth Service</b> by Carl (<a href=".documentation/auth-service.md">detailed documentation</a>)</summary>
  
  - Implements external and internal authentication and authorization across the application using JWT
  - Synchronizes accounts with user profiles by processing asynchronous User Service events.
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>User Service </b> by Carl (<a href=".documentation/user-service.md">detailed documentation</a>)</summary>
  
  - Responsible for consistent lifecycle of user profiles and companies.
  - Ensures consistency between accounts and user profiles and account by acting as an asynchronous ordering party for the Auth Service.
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Career Service</b> by Simon (<a href=".documentation/career-service.md">detailed documentation</a>)</summary>
   
  - Contains main business logic to create, edit and delete job offers / job applications
  - Has integration with the user-service over HTTP Api and Kafka events
  - Uses CQRS and Event-sourcing
</details>

<details xmlns="http://www.w3.org/1999/html">
  <summary><b>Notification Service</b> by Simon (<a href=".documentation/notification-service.md">detailed documentation</a>)</summary>

  - Small microservice that listens to events sent to the kafka event bus and prints information out to the console
</details>


