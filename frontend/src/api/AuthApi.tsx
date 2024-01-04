import {
    AccountManagementService,
    AccountDTO,
    AccountLoginResponseDTO,
    OpenAPI as AuthApiConfig
} from "../.generated/auth-service";
import {config} from "../config/config";
import * as console from "console";
import {UUID} from "../.generated/auth-service/models/UUID";
import {useAppSelector} from "../state/hooks";
import store from "../state/Store"
import AuthState from "../state/state/AuthState";
import ExternalApiError from "../model/ExternalApiError";
import {OpenAPI} from "../.generated/career-service";
import {handleUnexpectedResponse, parseError} from "./apis";
import {JobApplicationCreationResponse} from "../.generated/career-service/models/JobApplicationCreationResponse";
import {InternalAccount} from "../.generated/auth-service/models/InternalAccount";
import {InternalAccountRegistrationRequest} from "../.generated/auth-service/models/InternalAccountRegistrationRequest";
import {AccountLoginResponse} from "../.generated/auth-service/models/AccountLoginResponse";

export interface AuthApi {
    registerNewInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO>
    loginInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO>
    loginOidcAccount(id_token: string) : Promise<AccountLoginResponseDTO>
    updateAccountEmail(accountId: UUID,new_email: string): Promise<AccountDTO>;
    changePassword(accountId: UUID,new_password: string): Promise<void>;
    deleteAccount(accountId: UUID): Promise<void>;
}

export class DefaultAuthApi implements AuthApi{

    constructor() {
        this.initAuthApiConfig()
    }

    private getAuthState() {
        return store.getState().auth
    }

    private initAuthApiConfig() {
        AuthApiConfig.BASE = config.authServiceUrl
        AuthApiConfig.USERNAME = undefined
        AuthApiConfig.PASSWORD = undefined
        AuthApiConfig.TOKEN = undefined
        OpenAPI.HEADERS = { "Accept": "application/json, application/problem+json" }
    }

    private setAccessToken() {
        AuthApiConfig.TOKEN = this.getAuthState().accessToken
        AuthApiConfig.USERNAME = undefined
        AuthApiConfig.PASSWORD = undefined
    }

    async deleteAccount(accountId: UUID): Promise<void> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.deleteAccount(accountId)
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async changePassword(accountId: UUID,new_password: string): Promise<void> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.updateInternalAccountPassword(
            accountId,
            {
                new_plaintext_password: new_password
            })
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async updateAccountEmail(accountId: UUID,new_email: string): Promise<AccountDTO> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.updateInternalAccountEmail(accountId, {
            new_email: new_email
        })
            .catch(parseError<InternalAccount>).then(handleUnexpectedResponse)
    }

    async registerNewInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        return AccountManagementService.registerInternalAccount({
            email: email,
            password: password
        }).catch(parseError<AccountLoginResponse>).then(handleUnexpectedResponse)
    }

    async loginInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        AuthApiConfig.USERNAME = email;
        AuthApiConfig.PASSWORD = password;
        return AccountManagementService.loginInternalAccount()
            .catch(parseError<AccountLoginResponseDTO>).then(handleUnexpectedResponse)
    }

    async loginOidcAccount(id_token: string): Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        AuthApiConfig.TOKEN = id_token;
        return AccountManagementService.loginOidcAccount()
            .catch(parseError<AccountLoginResponseDTO>).then(handleUnexpectedResponse);
    }

}


export default AuthApi
