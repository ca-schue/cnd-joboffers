# Run the Application bare-metal

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

## Bare-Metal Build Services
- ### Frontend:
  - Workdir: `cnd-joboffers/frontend/`
  - Install NPM packages: `npm install`
  - Install Node.js http-server: `npm install -g http-server`
  - Build: `npm run build`
- ### Spring Services:
  - Workdir: `cnd-joboffers/[auth|user|career|notification]service/`
  - Build with gradle wrapper: `./gradlew bootJar` (will download gradle 8.4)


## Bare-Metal Services Configuration
- ### Note: Default configuration requires no changes
- ### Semi-Custom configuration:
    - Set values of predefined environment variables specified in `.env` as documented for each service\
      (Frontend, Auth, User, Career, Notification)
- ### Fully-Custom configuration:
  - Configure frontend at build-time: Change values of `LocalConfig.tsx`  
  - Configure Spring Services at execution-time:
    - Set `application.yaml` attributes using spring flags: \
      `java -jar <service>.jar --<fst-level>.<snd-level>.<...>=<value>`
    - Set `application.yaml` attributes using environment variables `FST-LEVEL_SND-LEVEL_...=value`
    - Change default values of each `application.yaml`. If renamed, provide filename with: \
      `java -jar <service.jar> --spring.config-name=<new-application.yaml>`

## Bare-Metal Execution
1. Start auxiliary infrastructure. For default configuration it must be reachable under:
    - Zookeeper: `http://localhost:2181` (default)
    - Kafka: `http://localhost:9092` (default)
    - MongoDB: `http://localhost:27017` (default)
    - EventDB: `http://TODO!` (default)
2. Start services
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
3. Register Career-Service at Auth-Service 
   - (TODO!)

## Bare-Metal Use Application
- Default Configuration: Enter in Browser `http://localhost/`
- Custom Configuration: Enter in Browser value of `$FRONTEND_URL` 