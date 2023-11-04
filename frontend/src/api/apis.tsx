import {CareerApi, DefaultCareerApi} from "./CareerApi";
import {config} from "../config/config";
import {AppConfig} from "../config/AppConfig";
import {AuthApi, DefaultAuthApi} from "./AuthApi";
import MockAuthApi from "./mock/MockAuthApi";
import MockCareerApi from "./mock/MockCareerApi";
import MockUserApi from "./mock/MockUserApi";
import {DefaultUserApi, UserApi} from "./UserApi";
import {OpenAPI as UserApiConfig} from "../.generated/user-service";
import {OpenAPI as AuthApiConfig} from "../.generated/auth-service";
import {OpenAPI as CareerApiConfig} from "../.generated/career-service";


export function setBasicAuthCredentials(email: string, password: string) {
    AuthApiConfig.USERNAME = email;
    AuthApiConfig.PASSWORD = password;
}


export function setAccessTokenToApi(accessToken: string | undefined) {
    UserApiConfig.TOKEN = accessToken
    AuthApiConfig.TOKEN = accessToken
    CareerApiConfig.TOKEN = accessToken
}


function getCareerApi(config: AppConfig): CareerApi {
    return config.mockCareerApi && new MockCareerApi() || new DefaultCareerApi()
}

export const careerApi = getCareerApi(config)

function getAuthApi(config: AppConfig): AuthApi {
    // @ts-ignore
    return config.mockAuthApi && new MockAuthApi() || new DefaultAuthApi()
}

export const authApi = getAuthApi(config)

function getUserApi(config: AppConfig): UserApi {
    return config.mockUserApi && new MockUserApi() || new DefaultUserApi()
}

export const userApi = getUserApi(config)
