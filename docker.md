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
  - Build container with Dockerfile: `docker build -t frontend:latest .`
- ### Spring Services:
  - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
  - Build jar with gradle wrapper: `./gradlew bootJar` (will download gradle 8.4)
  - Build container with Dockerfile: `docker build -t [auth|user|career|notification]-service:latest .`

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
1. Create docker network
    ```
    docker network create services-seperated
    ```
2. Create docker volumes
   - Workdir: `cnd-joboffers/`(root)
      ```
      ./mkdirs.sh
      ```
3. Start auxiliary infrastructure containers
   - Workdir: `cnd-joboffers/`(root)
   - Mongo DB:
       ```
       docker run \
           --rm \
           --env-file ./docker.env \
           --name mongodb \
           --volume ./volumes/mongo-data:/data/db \
           --volume ./authservice/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js \
           --network services-seperated \
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
   - EventDB 
       ```
       docker run \
           --rm \
           --env-file ./docker.env \
           --name eventstoredb \
           --volume ./volumes/eventstore-volume-data:/var/lib/eventstore \
           --volume ./volumes/eventstore-volume-logs:/var/log/eventstore \
           --network services-seperated \
           eventstore/eventstore:23.6.0-buster-slim
       ```   
4. Start service containers in this order:
   - Workdir: `cnd-joboffers/`(root)
   1. Auth-Service
      ```
      docker run \
         --rm \
         --env-file ./docker.env \
         --name auth-service \
         --network services-seperated \
         auth-service:latest
      ```
   2. User-Serivce
      ```
      docker run \
         --rm \
         --env-file ./docker.env \
         --name user-service \
         --network services-seperated \
         user-service:latest
      ```
   3. Career-Service
      ```
      docker run \
         --rm \
         --env-file ./docker.env \
         --name career-service \
         --network services-seperated \
         career-service:latest
      ```  
   4. Notification-Service
      ```
      docker run \
         --rm \
         --env-file ./docker.env \
         --name notification-service \
         --network services-seperated \
         notification-service:latest
      ```          
   5. Frontend 
      ```
      docker run \
         --rm \
         --env-file ./docker.env \
         --name frontend \
         --network services-seperated \
         -p 80:80 \
         frontend:latest
      ```  