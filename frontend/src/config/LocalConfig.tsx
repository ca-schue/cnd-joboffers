import {AppConfig} from "./AppConfig";

export const LocalConfig : AppConfig = {
    authServiceUrl: process.env.EXTERNAL_AUTH_SERVICE_URL || "http://localhost:8081",
    userServiceUrl: process.env.EXTERNAL_USER_SERVICE_URL || "http://localhost:8082",
    careerServiceUrl: process.env.EXTERNAL_CAREER_SERVICE_URL || "http://localhost:8083",
    oidcProviderDiscoveryEndpoint: process.env.OIDC_PROVIDER_DISCOVERY_ENDPOINT || "https://accounts.google.com",
    oidcClientId: process.env.OIDC_CLIENT_ID || "1059672486285-gea53vidspg5m9ff1d6mg5q5vjigva1t.apps.googleusercontent.com",
    oidcClientSecret: process.env.OIDC_CLIENT_SECRET || "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",
    oidcClientRedirectUrl: process.env.OIDC_CLIENT_REDIRECT_URL || "http://localhost",
    mockAuthApi: false,
    mockUserApi: false,
    mockCareerApi: false,
}

