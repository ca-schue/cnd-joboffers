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
import ExternalApiError from "../model/ExternalApiError";
import {ThrowableProblem} from "../.generated/career-service/models/ThrowableProblem";


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
    return config.mockAuthApi && new MockAuthApi() || new DefaultAuthApi()
}

export const authApi = getAuthApi(config)

function getUserApi(config: AppConfig): UserApi {
    return config.mockUserApi && new MockUserApi() || new DefaultUserApi()
}

export const userApi = getUserApi(config)


// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function parseError<T>(e: any): T {
    const body = e?.body
    if (isThrowableProblem(body)) {
        throw new ExternalApiError(
            e?.status || body.status || 500,
            body.code || -1,
            body.title || "Unknown Error",
            body.detail || null
        )
    }

    throw e
}

export function isThrowableProblem<T>(response: T | ThrowableProblem): response is ThrowableProblem {
    const problem = (response as ThrowableProblem)
    return problem?.status !== undefined && 599 >= Number(problem.status) && Number(problem.status) >= 300
        && problem?.title !== undefined;
}

export function handleUnexpectedResponse<T>(response: T | ThrowableProblem) : T {
    if (isThrowableProblem(response)) {
        throw response
    }

    return response
}
