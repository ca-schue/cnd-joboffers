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
  - Build container with Dockerfile: `docker build -t (TODO...)`

## Docker Container Configuration
- Problem: [Docker run cannot substitute variables set in .env, only docker compose can](https://stackoverflow.com/questions/63714506/variable-substitution-in-env-file-for-docker-run-env-file)
- ### Default configuration (no changes required)
- ### Semi-Custom configuration:
    - Change default values of predefined environment variables in `.env` file as documented for each service\
      (Frontend, Auth, User, Career, Notification)
    - Pass this file to each service container at execution time: `docker run (TODO!) --env-file .env`
- ### Fully-Custom configuration:
  - Configure frontend: Change values of `LocalConfig.tsx` before building the docker container  
  - Configure Spring Services at execution-time:
    - Set `application.yaml` attributes using spring flags at container execution-time: \
      `docker run (TODO!....) --<fst-level>.<snd-level>.<...>=<value>`
    - Set `application.yaml` attributes by passing spring environment variables to the container at execution-time. Pass this file to each service container at execution time: `docker run (TODO!) --env FST-LEVEL_SND-LEVEL_...=value` 
    - Set `application.yaml` attributes by changing the `application.yaml` file directly. If renamed, provide its full file path to the docker container at execution-time: \
      `docker run (TODO!....) --spring.config-name=<new-application.yaml>`

## Docker Execution
1. Create docker network (TODO!)
    ```
    docker network create services-seperated
    ```

2. Create docker volumes (TODO!)
3. Start auxiliary infrastructure containers
   - Mongo DB:
       ```
       source docker.env
    
       docker run \
           --rm \
           --env-file ./docker.env \
           --name mongodb \
           --volume ./volumes/mongo-data:/data/db \
           --volume ./authservice/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js \
           --network services-seperated \
           (-p 27017:$MONGO_DB_PORT \)
           mongo:4.4.6
       ```
   - Zookeeper:
       ```
       docker run \
           --rm \
           --env-file ./docker.env \
           --name zookeeper \
           --volume ./volumes/zookeeper/data:/var/lib/zookeeper/data \
           --volume ./volumes/zookeeper/log:/var/lib/zookeeper/log \
           --network services-seperated \
           confluentinc/cp-zookeeper:latest
       ```
   - Kafka:  
       ```
       docker run \
           --rm \
           --env-file ./docker.env \
           --name kafka-event \
           --volume ./volumes/kafka-broker/data:/var/lib/kafka/data \
           --network services-seperated \
           confluentinc/cp-kafka:latest
       ```
      .... Zookeeper, Kafka, MongoDB, EventDB (Ensure that the ports match the service configuration)
4. Start service containers (TODO!)
   - Order:
       1. Auth-Service
       2. User-Serivce
       3. Career-Service | Notification-Service
       4. Frontend 
   - Start commands:
     - Spring services:
       - Workdir: `cnd-joboffers/[auth|user|career|notification]service/build/libs/`
       - Command: `java -jar [auth|user|career|notification]service<version>.jar`
     - Frontend:
       - Workdir: `cnd-joboffers/frontend/build`
       - Command (default configuration): `http-server -p 80`
       - Command (custom configuration): `http-server -p [configured frontend port]`