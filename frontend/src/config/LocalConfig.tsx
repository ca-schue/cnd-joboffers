import {AppConfig} from "./AppConfig";

export const LocalConfig : AppConfig = {
    authServiceUrl: process.env.AUTH_SERVICE_URL || "http://localhost:8081",
    userServiceUrl: process.env.USER_SERVICE_URL || "http://localhost:8082",
    careerServiceUrl: process.env.CAREER_SERVICE_URL || "http://localhost:8083",
    oidcProviderDiscoveryEndpoint: process.env.OIDC_PROVIDER_DISCOVERY_ENDPOINT || "",
    oidcClientId: process.env.OIDC_CLIENT_ID || "",
    oidcClientSecret: process.env.OIDC_CLIENT_SECRET || "",
    oidcClientRedirectUrl: process.env.OIDC_CLIENT_REDIRECT_URL || "",
    mockAuthApi: false,
    mockUserApi: false,
    mockCareerApi: false,
}

