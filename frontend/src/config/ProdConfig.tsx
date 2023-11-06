import {AppConfig} from "./AppConfig";

export const ProdConfig : AppConfig = {
    authServiceUrl: process.env.AUTH_SERVICE_URL!,
    userServiceUrl: process.env.USER_SERVICE_URL!,
    careerServiceUrl: process.env.CAREER_SERVICE_URL!,
    oidcProviderDiscoveryEndpoint: process.env.OIDC_PROVIDER_DISCOVERY_ENDPOINT!,
    oidcClientId: process.env.OIDC_CLIENT_ID!,
    oidcClientSecret: process.env.OIDC_CLIENT_SECRET!,
    oidcClientRedirectUrl: process.env.OIDC_CLIENT_REDIRECT_URL!,
    mockAuthApi: false,
    mockUserApi: false,
    mockCareerApi: false,
}

