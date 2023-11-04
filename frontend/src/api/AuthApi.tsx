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
import ThrowableProblem from "../model/ThrowableProblem";
import {OpenAPI} from "../.generated/career-service";

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
    }

    private setAccessToken() {
        const authState = this.getAuthState()
        if (this.getAuthState().accessToken === undefined) {
            throw new Error("No Access token found. Re-Login recommended.")
        } else {
            AuthApiConfig.USERNAME = undefined
            AuthApiConfig.PASSWORD = undefined
            AuthApiConfig.TOKEN = authState.accessToken
        }
    }

    async deleteAccount(accountId: UUID): Promise<void> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.deleteAccount(accountId)
    }

    async changePassword(accountId: UUID,new_password: string): Promise<void> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.updateInternalAccountPassword(
            accountId,
            {
                new_plaintext_password: new_password
            })
    }

    async updateAccountEmail(accountId: UUID,new_email: string): Promise<AccountDTO> {
        this.initAuthApiConfig()
        this.setAccessToken()
        return AccountManagementService.updateInternalAccountEmail(accountId, {
            new_email: new_email
        })
    }

    async registerNewInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        return AccountManagementService.registerInternalAccount({
            email: email,
            password: password
        })
    }

    async loginInternalAccount(email: string, password: string) : Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        AuthApiConfig.USERNAME = email;
        AuthApiConfig.PASSWORD = password;
        return AccountManagementService.loginInternalAccount()
    }

    async loginOidcAccount(id_token: string): Promise<AccountLoginResponseDTO> {
        this.initAuthApiConfig()
        AuthApiConfig.TOKEN = id_token;
        return AccountManagementService.loginOidcAccount();
    }

}


export default AuthApi
