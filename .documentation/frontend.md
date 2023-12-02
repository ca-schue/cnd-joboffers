# Auth Service:

## Required Env Vars:
```  
EXTERNAL_AUTH_SERVICE_URL = {URL of auth service which sits potentially behind a reverse proxy}
EXTERNAL_USER_SERVICE_URL = {URL of user service which sits potentially behind a reverse proxy}
EXTERNAL_CAREER_SERVICE_URL = {URL of career service which sits potentially behind a reverse proxy}

OIDC_PROVIDER_DISCOVERY_ENDPOINT = {discovery endpoint of the external oidc provider}
OIDC_CLIENT_ID = {oidc client id for this frontend registered at the external oidc provider}
OIDC_CLIENT_SECRET = {oidc client secret for this frontend registered at the external oidc provider}
OIDC_CLIENT_REDIRECT_URL = {oidc redirect URL for this frontend registered at the external oidc provider}
```