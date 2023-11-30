# User Service:

## Required Env Vars:

```  
USER_SERVICE_PORT = {Spring will start the web server on this port}
USER_SERVICE_MONGODB_URI = {URI of MongoDB Document for user service data}

KAFKA_BOOTSTRAP_SERVER = {URL of kafka message broker}

INTERNAL_JWT_ISSUER_URL = {issuer URL of valid internal JWTs}
```