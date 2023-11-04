import {TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import AppState from "./state/AppState";
import {AppDispatch} from "./Store";
import {removeAccessToken} from "./reducer/AuthSlice";
import {logoutUser} from "./reducer/UserSlice";
import {clearMyJobApplications} from "./reducer/MyJobApplicationSlice";
import {clearMyCompanies} from "./reducer/MyCompaniesSlice";
import {useNavigate} from "react-router-dom";
import {logoutAccount} from "./reducer/AccountSlice";
import {clearSelectedCompany} from "./reducer/SelectedMemberCompanySlice";
import {OpenAPI} from "../.generated/auth-service";

export const useAppDispatch: () => AppDispatch = useDispatch
export const useAppSelector: TypedUseSelectorHook<AppState> = useSelector

const resetOpenApiConfig = () => {
    OpenAPI.BASE = ''
    OpenAPI.VERSION = '1.0.0'
    OpenAPI.WITH_CREDENTIALS = false
    OpenAPI.CREDENTIALS = 'include'
    OpenAPI.TOKEN = undefined
    OpenAPI.USERNAME = undefined
    OpenAPI.PASSWORD = undefined
    OpenAPI.HEADERS = undefined
    OpenAPI.ENCODE_PATH = undefined
}

export const useLogout = () => {

    const appDispatch = useAppDispatch()
    const navigate = useNavigate()

    return (toHomeScreen: boolean = false) => {
        appDispatch(removeAccessToken())
        appDispatch(logoutAccount())
        appDispatch(logoutUser())
        appDispatch(clearMyJobApplications())
        appDispatch(clearMyCompanies())
        appDispatch(clearSelectedCompany())
        resetOpenApiConfig()
        if(toHomeScreen) {
            navigate("/")
        }
    }

}
