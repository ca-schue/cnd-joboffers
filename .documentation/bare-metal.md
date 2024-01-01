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
   -  Version: 3.6.0
   - Tested on: [kafka-3.6.0-src.tgz](https://dlcdn.apache.org/kafka/3.6.0/)
   - [Installation guide (unofficial)](https://tecadmin.net/install-apache-kafka-debian/)
4. MongoDB 
      - Version: 4.4 (If CPU does not support AVX), otherwise 5.0+ 
      - Tested on: [Mongo DB 4.4 "Debian Bookworm" amd64](https://repo.mongodb.org/apt/debian/dists/bookworm/mongodb-org/4.4/main/binary-amd64/)
      - [Installation guide (unofficial)](https://www.mongodb.com/docs/v4.4/tutorial/install-mongodb-on-debian/) use following repository in step 2:\
       `echo "deb [ signed-by=/usr/share/keyrings/mongodb-server-4.4.gpg ] http://repo.mongodb.org/apt/debian/dists/bookworm/mongodb-org/4.4/main" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list`
      - Note: MongoDB 4.4 requires the insecure `libssl1.1` package ([insecure workaround](https://askubuntu.com/a/1403683), use at own risk)
5. Node.js 
      - Version: 9.2.0
      - Tested on: `apt-get install npm=9.2.0~ds1-1`
6. TODO: event DB

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
We recommend using the provided `.env.bare-metal` file as the single source of truth for configuration as documented below:
- All variables are preconfigured with non-conflicting values (assuming the specified ports are unused on your local machine).
- Variable substitution allows unique values such as ports and hostnames **only to be changed in one place**.
- Reference the provided service documentations before changing configuration parameters: Frontend, Auth, User, Career, Notification

## Bare-Metal Execution
1. Auxiliary infrastructure must be reachable under:
    - Zookeeper: `http://localhost:$ZOOKEEPER_PORT` (default = `http://localhost:2181`)
    - Kafka: `http://localhost:$KAFKA_PORT` (default = `http://localhost:9092`)
    - MongoDB: `http://localhost:$MONGO_DB_PORT` (default = `http://localhost:27017`)
    - EventDB: `http://TODO!:$EVENTSTORE_HTTP_PORT` (default = `???:2113`)
2. Start Services in this order:
    1. Auth-Service
    2. User-Serivce
    3. Career-Service | Notification-Service
       - Execute in the build directory of each service `cnd-joboffers/[auth|user|career|notification]service/build/libs/`:
         ```
         source ./.config/.env.bare-metal # not required for default configuration
         java -jar [auth|user|career|notification]service<version>.jar
         ```   
    4. Frontend
       - Execute in the frontend build directory `cnd-joboffers/frontend/build`:
         ```
         source ./.config/.env.bare-metal # not required for default configuration
         npm run build # build frontend
         http-server -p $FRONTEND_PORT # provide custom port with -p flag (default: $FRONTEND_PORT = 80)
         ```
3. Register Career-Service at Auth-Service 
   - (TODO!)

## Using the Application
- Access in browser: `http://localhost:$FRONTEND_PORT` (default = `http://localhost/`)

