# Auth Service:

## Required Env Vars:

```  
AUTH_SERVICE_PORT = {Spring will start the web server on this port
AUTH_SERVICE_MONGODB_URI = {URI of MongoDB Document for auth service data}

KAFKA_BOOTSTRAP_SERVER = {URL of kafka message broker}

INTERNAL_JWT_ISSUER_URL = {Fqdn to verify the issuer of internal JWT = FQDN of auth service itself}
OIDC_PROVIDER_DISCOVERY_ENDPOINT = {discovery endpoint of the oidc provider}
JWT_PUBLIC_KEY_BASE64 = {public key for signing the JWT base64 encoded}
JWT_PRIVATE_KEY_BASE64 = {private key for signing the JWT base64 encoded}
JWT_KEY_ID = {UUID of JWK pair which will be set in JWT header}
JWT_VALIDITY_IN_SECONDS = {validity of JWT}
JWT_KEY_ALGORITHM = {Abbreviation of used signing algotihm according to java.security, ex. 'RSA'} (TODO!)
JWT_SIGNING_ALGORITHM = {Abbreviation of used signing algotihm according to java.security, ex. 'RSA256'} (TODO!)
```