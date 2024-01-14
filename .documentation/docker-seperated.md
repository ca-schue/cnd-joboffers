# Run the Application as Separate Docker Containers 

You can run the "joboffers" application as separately started docker containers (`docker run ...`) on your local machine running under `localhost`.
**Since this is error-prone for misconfigurations, we recommend using [docker compose](docker-compose.md) instead.**

## Prerequisites:
 1. Operating System
    - Tested on: Debian 12.2.0 "Bookworm" ([AMD 64 bit, netinst](https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/)) inside VirtualBox VM
 2. JDK
    - Version: 21.0.1
    - Tested on: [OpenJDK JDK 21.0.1 General-Availability Release](https://jdk.java.net/21/) (Linux/x64)
    - [Installation guide (unofficial)](https://www.linuxcapable.com/how-to-install-openjdk-21-on-ubuntu-linux/)
 3. Docker
    - Version: 24.0.7
    - Tested on: [apt repository]([https://dlcdn.apache.org/kafka/3.6.0/](https://docs.docker.com/engine/install/debian/#install-using-the-repository))

## Build Service Containers
- ### Frontend:
  - Workdir: `cnd-joboffers/frontend/`
    ```
    docker build -t frontend:latest .
    ```
- ### Spring Services:
  - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
  - Build `.jar` with gradle wrapper: (will download gradle 8.4) 
    ```
    ./gradlew bootJar
    ```
  - Build container with Dockerfile:
    ```
    docker build -t [auth|user|career|notification]-service:latest .
    ```

## Configure Service Containers

***Note:*** This application is configured at execution time using environment variables. Use additional configuration, such as changing the `application.yaml` files  or injecting `--spring-flags` at your own risk of misconfigurations and inconsistencies.
We recommend using the provided `.env.docker.static` file as the single source of truth for configuration as documented below:
- All variables are preconfigured with non-conflicting values (assuming the specified ports are unused on your local machine).
- ***Problem:*** Compared to `docker compose --env-file ...`, the command `docker run --env-file ...` [cannot substitute variables in the `.env` file](https://stackoverflow.com/questions/63714506/variable-substitution-in-env-file-for-docker-run-env-file), leading to replicated configuration parameters such as ports and URLs.
- Any changes made to the configuration parameters must therefore be checked for inconsistencies. 
- **We advise against using separate Docker containers - Use  `docker compose --env-file ...` together with the `.env.docker` file and variable substitution instead.**

## Execute Containers
- Execute all commands in the root directory of this repository `cnd-joboffers/`.
1. Create docker network:
    ```
    docker network create services-seperated
    ```
2. Create docker volumes folder on the host machine:
    ```
    ./mkdirs.sh
    ```
3. Start auxiliary infrastructure containers:
   - Mongo DB:
       ```
       docker run \
           --rm \
           --name mongodb \
           --volume ./volumes/mongo-data:/data/db \
           --volume ./authservice/mongo-init.js:/docker-entrypoint-initdb.d/mongo-init.js \
           --network services-seperated \
           --env-file ./.config/.env.docker.static \
           mongo:4.4.6
       ```
   - Zookeeper:
       ```
       docker run \
           --rm \
           --name zookeeper \
           --volume ./volumes/zookeeper/data:/var/lib/zookeeper/data \
           --volume ./volumes/zookeeper/log:/var/lib/zookeeper/log \
           --network services-seperated \
           --env-file ./.config/.env.docker.static \
           confluentinc/cp-zookeeper:latest     
       ```
   - Kafka:  
       ```
       docker run \
           --rm \
           --name kafka-event \
           --volume ./volumes/kafka-broker/data:/var/lib/kafka/data \
           --network services-seperated \
           --env-file ./.config/.env.docker.static \
           confluentinc/cp-kafka:latest
       ```
   - EventDB 
       ```
       docker run \
           --rm \
           --name eventstoredb \
           --volume ./volumes/eventstore-volume-data:/var/lib/eventstore \
           --volume ./volumes/eventstore-volume-logs:/var/log/eventstore \
           --network services-seperated \
           --env-file ./.config/.env.docker.static \
           eventstore/eventstore:23.6.0-buster-slim
      ```   
4. Start service containers in this order:
   - ***Note:*** We recommend exposing ports using environment variables (`-p $SERVICE_PORT:$SERVICE_PORT`) by sourcing the `.env.docker.static` before running a service container as documented below.
    1. Auth-Service
       ```
       source .config/.env.docker.static
       docker run \
          --rm \
          --name auth-service \
          --network services-seperated \
          --env-file ./.config/.env.docker.static \
          -p $AUTH_SERVICE_PORT:$AUTH_SERVICE_PORT \
          auth-service:latest
       ```
   2. User-Serivce
      ```
      source .config/.env.docker.static
      docker run \
          --rm \
          --name user-service \
          --network services-seperated \
          --env-file ./.config/.env.docker.static \
          -p $USER_SERVICE_PORT:$USER_SERVICE_PORT \
          user-service:latest
      ```
   3. Career-Service
      ```
      source .config/.env.docker.static
      docker run \
          --rm \
          --name career-service \
          --network services-seperated \
          --env-file ./.config/.env.docker.static \
          -p $CAREER_SERVICE_PORT:$CAREER_SERVICE_PORT \
          career-service:latest
      ```  
   4. Notification-Service
      ```
      source .config/.env.docker.static
      docker run \
          --rm \
          --name notification-service \
          --network services-seperated \
          --env-file ./.config/.env.docker.static \
          notification-service:latest
      ```          
   5. Frontend 
      ```
      source .config/.env.docker.static
      docker run \
         --rm \
         --name frontend \
         --network services-seperated \
         --env-file ./.config/.env.docker.static \
         -p $FRONTEND_PORT:80 \
         frontend:latest
      ```
## Using the Application
- Default Configuration: `http://localhost/`
- Custom Configuration: `http://localhost:$FRONTEND_PORT/`   
