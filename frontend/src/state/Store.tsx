import {combineReducers, configureStore} from "@reduxjs/toolkit";
import {defaultState} from "./state/AppState";
import {AccountSlice} from "./reducer/AccountSlice";
import {AuthSlice} from "./reducer/AuthSlice";
import {UserSlice} from "./reducer/UserSlice";
import {loadState, saveState} from "./localStorage";
import {MyJobApplicationSlice} from "./reducer/MyJobApplicationSlice";
import {MyCompaniesSlice} from "./reducer/MyCompaniesSlice";
import {SelectedMemberCompanySlice} from "./reducer/SelectedMemberCompanySlice";

export const AppReducer = combineReducers({
    auth: AuthSlice.reducer,
    account: AccountSlice.reducer,
    user: UserSlice.reducer,
    myJobApplications: MyJobApplicationSlice.reducer,
    myNewCompanies: MyCompaniesSlice.reducer,
    selectedMemberCompany: SelectedMemberCompanySlice.reducer,
})

const store = configureStore({
    reducer: AppReducer,
    preloadedState: loadState() || defaultState
})

store.subscribe(() => {
    saveState(store.getState());
});

export type AppDispatch = typeof store.dispatch

export default store
