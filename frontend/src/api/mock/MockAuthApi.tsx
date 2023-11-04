import AuthApi from "../AuthApi";
import * as console from "console";
import {
    AccountDTO,
    AccountLoginResponseDTO,
    AccountManagementService, InternalAccountDTO,
    OpenAPI as AuthApiConfig
} from "./../../.generated/auth-service";
import {UserDTO} from "./../../.generated/user-service";
import {UUID} from "../../.generated/auth-service/models/UUID";


export default class MockAuthApi /*implements AuthApi*/ {

    async deleteAccount(accountId: UUID): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(r => { r() });
    }

    async updateAccountEmail(accountId: UUID, new_email: string): Promise<AccountDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        const newAccount: InternalAccountDTO = {
            accountType: "InternalAccount",
            id: accountId,
            email: new_email
        };
        return new Promise<AccountDTO>(resolve => {
            resolve(newAccount)});
    }

    async changePassword(accountId: UUID,new_password: string): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(r => { r() });
    }

    async loginInternalAccount(email: string, password: string): Promise<AccountLoginResponseDTO> {
        console.log("Entered Login")
        await new Promise(r => setTimeout(r, 1000 * 3))
        console.log("Sleeping done")

        //return new Promise<AccountLoginResponseDTO>(resolve =>{ throw new Error("Credentials not found")})


        return new Promise<AccountLoginResponseDTO>(resolve => {
            resolve(
                {
                    account: {
                        accountType: "InternalAccount",
                        id: "123",
                        email: "internal@account.mail"
                    },
                    access_token: "jwt.access.token"
                }
            )
        })
    }

    async registerNewInternalAccount(email: string, password: string): Promise<AccountLoginResponseDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<AccountLoginResponseDTO>(resolve => {
            resolve(
                {
                    account: {
                        accountType: "InternalAccount",
                        id: "123",
                        email: email
                    },
                    access_token: "jwt.access.token"
                }
            )
        })
    }


    async loginOidcAccount(id_token: string): Promise<AccountLoginResponseDTO> {
        console.log("Entered Login")
        await new Promise(r => setTimeout(r, 1000 * 3))
        AuthApiConfig.TOKEN = id_token;
        console.log("Sleeping done")
        return new Promise<AccountLoginResponseDTO>(resolve => {
            resolve(
                {
                    account: {
                        accountType: "OidcAccount",
                        id: "123",
                        subject: "1234-5678"
                    },
                    access_token: "jwt.access.token"
                }
            )
        })
    }


}
