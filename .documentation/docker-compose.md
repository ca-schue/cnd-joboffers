# Run the Application with Docker Compose 

In contrast to seperated docker containers, each running under `localhost`, our provided docker compose file runs them in an isolated network behind an ngninx reverse proxy.

## Prerequisites:
 1. Operating System
    - Tested on: Debian 12.2.0 "Bookworm" ([AMD 64 bit, netinst](https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/)) inside VirtualBox VM
 2. JDK
    - Version: 21.0.1
    - Tested on: [OpenJDK JDK 21.0.1 General-Availability Release](https://jdk.java.net/21/) (Linux/x64)
    - [Installation guide (unofficial)](https://www.linuxcapable.com/how-to-install-openjdk-21-on-ubuntu-linux/)
 3. Docker
    - Version: Latest
    - Tested on: [kafka-3.6.0-src.tgz](https://dlcdn.apache.org/kafka/3.6.0/)
    - [Installation guide (unofficial)](https://tecadmin.net/install-apache-kafka-debian/)

## Build Spring Services 
  - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
  - Build `.jar` with gradle wrapper: (will download gradle 8.4)
    ```
    ./gradlew bootJar
    ```
  - Whenever you change lines in the code you will need to redo this step and build the image again
  
## Application Configuration
***Note***: This application is configured at execution time using environment variables. Use additional configuration, such as changing the `application.yaml` files  or injecting `--spring-flags` at your own risk of misconfigurations and inconsistencies.
We recommend using the provided `.env.docker` file as the single source of truth for configuration as documented below:
- All variables are preconfigured with non-conflicting values (assuming the specified ports are unused on your local machine).
- Variable substitution allows unique values such as ports and hostnames **only to be changed in one place**.
- Reference the provided service documentations before changing configuration parameters: Frontend, Auth, User, Career, Notification

## Docker Compose Execution
- Execute all commands in the root directory of this repository `cnd-joboffers/`.
1. Create docker volumes folder on the host machine:
    ```
    ./mkdirs.sh
    ```
2. Execute docker compose
   ```
   docker compose --env-file .config/.env.docker up
   ```
   
## Using the Application
- Default Configuration: `http://localhost/`
- Custom Configuration: `http://localhost/$NGINX_PORT`

## Stopping the Application
- Workdir: `cnd-joboffers/` (root)
   ```
   docker compose --env-file .config/.env.docker down
   ```
