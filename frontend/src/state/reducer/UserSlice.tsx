import {defaultUserState} from "../state/UserState";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {UserDTO} from "../../../src/.generated/user-service";


export const UserSlice = createSlice({
    name: "user",
    initialState: defaultUserState,
    reducers: {
        loginUser: (state, action: PayloadAction<{user: UserDTO}>) => {
            state.user = action.payload.user
        },
        updateUser: (state, action: PayloadAction<{user: UserDTO}>) => {
            state.user = action.payload.user
        },
        logoutUser: (state) => {
            state.user = undefined
        }
    }
})

export const { loginUser, logoutUser, updateUser } = UserSlice.actions
