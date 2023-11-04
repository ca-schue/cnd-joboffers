import {AppConfig} from "./AppConfig";

export const LocalConfig : AppConfig = {
    authServiceUrl: process.env.AUTH_SERVICE_URL || "http://localhost:8081",
    userServiceUrl: process.env.USER_SERVICE_URL || "http://localhost:8082",
    careerServiceUrl: process.env.CAREER_SERVICE_URL || "http://localhost:8083",
    mockAuthApi: false,
    mockUserApi: false,
    mockCareerApi: false,
}

