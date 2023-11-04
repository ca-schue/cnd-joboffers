
export default interface AuthState {
    accessToken?: string
}

export const defaultAuthState: AuthState = {
    accessToken: undefined
}
