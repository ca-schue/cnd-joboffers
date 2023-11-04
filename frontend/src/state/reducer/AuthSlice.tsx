import type {PayloadAction} from '@reduxjs/toolkit'
import {createSlice} from "@reduxjs/toolkit";
import {defaultAuthState} from "../state/AuthState";
import {setAccessTokenToApi} from "../../api/apis";


export const AuthSlice = createSlice({
    name: "auth",
    initialState: defaultAuthState,
    reducers: {
        setAccessToken: (state, action: PayloadAction<{accessToken: string}>) => {
            setAccessTokenToApi(action.payload.accessToken)
            state.accessToken = action.payload.accessToken
        },
        removeAccessToken: (state) => {
            setAccessTokenToApi(undefined)
            state.accessToken = undefined
        }
    }
})

export const { setAccessToken, removeAccessToken } = AuthSlice.actions
