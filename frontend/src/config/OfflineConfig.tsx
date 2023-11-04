import {AppConfig} from "./AppConfig";

export const OfflineConfig : AppConfig = {
    authServiceUrl: process.env.AUTH_SERVICE_URL || "http://localhost:8085",
    userServiceUrl: process.env.USER_SERVICE_URL || "http://localhost:8084",
    careerServiceUrl: process.env.CAREER_SERVICE_URL || "http://localhost:8083",
    mockAuthApi: true,
    mockUserApi: true,
    mockCareerApi: true,
}

