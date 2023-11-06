import {AppConfig} from "./AppConfig";

export const ProdConfig : AppConfig = {
    authServiceUrl: process.env.AUTH_SERVICE_URL!,
    userServiceUrl: process.env.USER_SERVICE_URL!,
    careerServiceUrl: process.env.CAREER_SERVICE_URL!,
    mockAuthApi: false,
    mockUserApi: false,
    mockCareerApi: false,
}

