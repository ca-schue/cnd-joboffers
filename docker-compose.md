# Run the Application with Docker Compose 

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

## Docker Build Container
- ### Frontend:
  - Workdir: `cnd-joboffers/frontend/`
  - Install NPM packages: `npm install`
  - Install Node.js http-server: `npm install -g http-server`
  - Build: `npm run build`
- ### Spring Services:
  - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
  - Build jar with gradle wrapper: `./gradlew bootJar` (will download gradle 8.4)

## Docker Compose Configuration
- ### Default configuration (no changes required)
- ### Semi-Custom configuration:
    - Change default values of predefined environment variables in `.env` file as documented for each service\
      (Frontend, Auth, User, Career, Notification)
    - Pass this file to the docker compose command (see next section): `docker compose up --env-file <env-file-name>.env`
- ### Fully-Custom configuration: (not recommended)
  - Configure frontend: Change values of `LocalConfig.tsx` before executing docker compose  
  - Configure Spring Services at build time:
    - Set `application.yaml` attributes by changing the `application.yaml` file directly. Rebuild the jars required or passed as filename in spring flag at execution-time (see next section) 
  - Configure Spring Services at execution-time:
    - Change the docker compose file by setting spring flags in the `command:` attributes of the services. Refer to section ["Docker Configuration"](docker.md). 

## Docker Compose Execution
1. Create docker volumes:
   - Workdir: `cnd-joboffers/` (root)
   - Command: `./mkdirs.sh`
3. Execute docker compose
   - Workdir: `cnd-joboffers/` (root)
   - Command:
     - Default configuration: `docker compose up`
     - Custom configuration: `docker compose up --env-file <env-file-name>.env`

## Docker Compose Use Application
- Default Configuration: Enter in Browser `http://localhost/`
- Custom Configuration: TODO