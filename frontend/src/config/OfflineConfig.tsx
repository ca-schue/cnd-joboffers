import {AppConfig} from "./AppConfig";

export const OfflineConfig : AppConfig = {
    authServiceUrl: process.env.EXTERNAL_AUTH_SERVICE_URL || "http://localhost:8085",
    userServiceUrl: process.env.EXTERNAL_USER_SERVICE_URL || "http://localhost:8084",
    careerServiceUrl: process.env.EXTERNAL_CAREER_SERVICE_URL || "http://localhost:8083",
    oidcProviderDiscoveryEndpoint: process.env.OIDC_PROVIDER_DISCOVERY_ENDPOINT || "",
    oidcClientId: process.env.OIDC_CLIENT_ID || "",
    oidcClientSecret: process.env.OIDC_CLIENT_SECRET || "",
    oidcClientRedirectUrl: process.env.OIDC_CLIENT_REDIRECT_URL || "",
    mockAuthApi: true,
    mockUserApi: true,
    mockCareerApi: true,
}

