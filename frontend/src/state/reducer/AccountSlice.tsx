import {defaultAccountState} from "../state/AccountState";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {AccountDTO} from "../../.generated/auth-service";

export const AccountSlice = createSlice({
    name: "account",
    initialState: defaultAccountState,
    reducers: {
        loginAccount: (state, action: PayloadAction<{account: AccountDTO}>) => {
            state.account = action.payload.account
            state.loggedIn = true
        },
        updateAccount: (state, action: PayloadAction<{updatedAccount: AccountDTO}>) => {
            state.account = action.payload.updatedAccount
        },
        userProfileCreated: (state, action: PayloadAction<{userProfileCreated: boolean}>) => {
            state.userProfileCreated = action.payload.userProfileCreated
        },
        logoutAccount: (state) => {
            state.account = undefined
            state.loggedIn = false
            state.userProfileCreated = false
        }
    }
})

export const { loginAccount, updateAccount, userProfileCreated, logoutAccount } = AccountSlice.actions
