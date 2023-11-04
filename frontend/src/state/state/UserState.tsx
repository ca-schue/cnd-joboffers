import {UserDTO} from "../../../src/.generated/user-service";

export default interface UserState {
    user?: UserDTO
}

export const defaultUserState: UserState = {
    user: undefined
}
