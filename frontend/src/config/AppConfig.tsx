
export type AppConfig = {
    authServiceUrl: string
    userServiceUrl: string
    careerServiceUrl: string
    oidcProviderDiscoveryEndpoint: string
    oidcClientId: string
    oidcClientSecret: string
    oidcClientRedirectUrl: string
    mockAuthApi: boolean
    mockUserApi: boolean
    mockCareerApi: boolean
}
