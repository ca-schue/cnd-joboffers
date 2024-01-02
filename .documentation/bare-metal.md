# Run the Application bare-metal

You can run the "Joboffers" application bare-metal, un-containerized on your local machine. This requires installing all dependencies and auxiliary infrastructure.

## Prerequisites:
1. Operating System
   - Tested on: Debian 12.2.0 "Bookworm" ([AMD 64 bit, netinst](https://cdimage.debian.org/debian-cd/current/amd64/iso-cd/)) inside VirtualBox VM
2. JDK
   - Version: 21.0.1
   - Tested on: [OpenJDK JDK 21.0.1 General-Availability Release](https://jdk.java.net/21/) (Linux/x64)
   - [Installation guide (unofficial)](https://www.linuxcapable.com/how-to-install-openjdk-21-on-ubuntu-linux/)
3. Apache Kafka & Zookeeper
   - Version: 3.6.0
   - Tested on: [kafka_2.13-3.6.0.tgz]([https://dlcdn.apache.org/kafka/3.6.0/](https://dlcdn.apache.org/kafka/3.6.0/kafka_2.13-3.6.0.tgz)) ([repo](https://dlcdn.apache.org/kafka/3.6.0/))
   - Installation guide
     - Download the file above
     - Unpack with `tar xzf kafka_2.13-3.6.0.tgz`
     - Go into the folder `cd kafka_2.13-3.6.0.tgz`
     - Run zookeeper `./bin/zookeeper-server-start.sh ./config/zookeeper.properties`
     - Run kafka `./bin/kafka-server-start.sh ./config/server.properties`
4. MongoDB 
      - Version: 4.4 (If CPU does not support AVX), otherwise 5.0+ 
      - Tested on: [Mongo DB 4.4 "Debian Bookworm" amd64](https://repo.mongodb.org/apt/debian/dists/bookworm/mongodb-org/4.4/main/binary-amd64/)
      - [Installation guide (unofficial)](https://www.mongodb.com/docs/v4.4/tutorial/install-mongodb-on-debian/)
      - Note: MongoDB 4.4 requires the insecure `libssl1.1` package ([insecure workaround](https://askubuntu.com/a/1403683), use at own risk)
        - Install with
           ```
            echo "deb [trusted=true] http://security.ubuntu.com/ubuntu focal-security main" | sudo tee /etc/apt/sources.list.d/focal-security.list
            sudo apt-get update
            sudo apt-get install libssl1.1
            sudo rm /etc/apt/sources.list.d/focal-security.list
            sudo apt-get update
           ```
        - You need to run an initial script that the required client secrets are in the database. See *Bare-Metal Execution* last point.
5. Node.js 
      - Version: 9.2.0
      - Tested on: `apt-get install npm=9.2.0~ds1-1`
6. EventStoreDB
    - Download the [EventStore-OSS-Linux-v23.10.0.tar.gz Release](https://github.com/EventStore/EventStore/releases/tag/oss-v23.10.0)
    - Extract with `tar xzf  EventStore-OSS-Linux-v23.10.0.tar.gz`
    - Go into the folder `cd EventStore-OSS-Linux-v23.10.0.tar.gz`
    - Start EventStoreDB with `./eventstored --insecure`
    - You can check the local UI if EventStore is running under [localhost:2113](http://127.0.0.1:2113/web/index.html#/dashboard)

## Build Bare-Metal Services
- ### Frontend:
  - Workdir: `cnd-joboffers/frontend/`
     ``` 
     npm install # installs package.json requirements
     npm install -g http-server # installs Node.js http server
     # Do not execute 'npm build' yet! 
     ```
- ### Spring Services:
    - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
    - Build `.jar` with gradle wrapper: (will download gradle 8.4)
      ```
      ./gradlew bootJar
      ```
    - Optionally: Execute slice tests to verify core functionality
      ```
      ./gradlew test
      ```
    - Optionally: Execute end-to-end integration tests to verify use cases
      ```
      ./gradlew integrationTest
      ```

## Configure Bare-Metal Services
***Note***: This application is configured at execution time using environment variables. Use additional configuration, such as changing the `application.yaml` files  or injecting `--spring-flags` at your own risk of misconfigurations and inconsistencies.
The provided env file `.env.local` can be used to override the default configuration. As single source of truth check the application.yaml in the microservice source code.
- All variables are preconfigured with non-conflicting values (assuming the specified ports are unused on your local machine).
- Variable substitution allows unique values such as ports and hostnames **only to be changed in one place**.
- Reference the provided service documentations before changing configuration parameters: Frontend, Auth, User, Career, Notification

## Bare-Metal Execution
1. Auxiliary infrastructure must be reachable under:
    - Zookeeper: `http://localhost:$ZOOKEEPER_PORT` (default = `http://localhost:2181`)
    - Kafka: `http://localhost:$KAFKA_PORT` (default = `http://localhost:9092`)
    - MongoDB: `http://localhost:$MONGO_DB_PORT` (default = `http://localhost:27017`)
    - EventDB: `esdb://localhost:$EVENTSTORE_HTTP_PORT` (default = `esdb://localhost:2113`)
2. Start Services in this order:
    1. Auth-Service
    3. User-Serivce
    4. Career-Service | Notification-Service
       - Execute in the build directory of each service `cnd-joboffers/[auth|user|career|notification]service/build/libs/`:
         ```
         set -o allexport && source .config/.env.local && set +o allexport # not required for default configuration
         java -jar [auth|user|career|notification]service<version>.jar
         ```   
    5. Frontend
       - Execute in the frontend build directory `cnd-joboffers/frontend/build`:
         ```
         set -o allexport && source .config/.env.local && set +o allexport # not required for default configuration
         npm run build # build frontend
         http-server -p $FRONTEND_PORT # provide custom port with -p flag (default: $FRONTEND_PORT = 80)
         ```
3. Register Career-Service at Auth-Service 
   - **IMPORTANT: You need to add intial credentials to mongodb.**
      - Go into the auth-service folder
      - Execute: `mongo mongo-init.js`

## Using the Application
- Access in browser: `http://localhost:$FRONTEND_PORT` (default = `http://localhost/`)
