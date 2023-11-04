import { AccountDTO } from "../../../src/.generated/auth-service";

export default interface AccountState {
    loggedIn: boolean
    userProfileCreated: boolean
    account?: AccountDTO
}

export const defaultAccountState: AccountState = {
    loggedIn: false,
    userProfileCreated: false,
    account: undefined
}
