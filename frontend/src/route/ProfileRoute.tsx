import React from "react";
import {Box, Typography} from "@mui/material";
import {useAppSelector, useLogout} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import MyJobApplicationView from "../component/MyJobApplicationView";
import UserArea from "../component/UserArea";
import GenericView from "../view/GenericView";
import GenericButton from "../component/left-hand-menu/GenericButton";
import SideMenu from "../component/left-hand-menu/SideMenu";
import styled from "@emotion/styled";
import EditJobApplication from "../component/my-job-applications/EditJobApplication";
import MyAccountView from "../component/MyAccountView";
import MyProfileView from "../component/MyProfileView";

interface ProfileRouteProps {
    view: 'profile' | 'account' | 'job-applications' | 'edit-job-application'
}

const ProfileRoute = (props: ProfileRouteProps) => {

    const navigate = useNavigate()
    const logoutHook = useLogout()
    const userState = useAppSelector(state => state.user)
    const accountState = useAppSelector(state => state.account)

    function routeTo(url: string) {
        navigate(url)
    }

    const componentToUse = () => {
        switch(props.view) {
            case "profile": return(<MyProfileView/>)
            case "account": return(<MyAccountView/>)
            case "job-applications": return(<MyJobApplicationView/>)
            case "edit-job-application": return(<EditJobApplication/>)
        }
    }

    return (
        <UserArea>
            <GenericView>
                <SideMenu>
                    { accountState.loggedIn && accountState.account !== undefined && (
                        <GenericButton text="Account" selected={props.view == "account"} onClick={() => routeTo("/profile/account")} />
                    )}
                    { accountState.loggedIn && accountState.account !== undefined && accountState.userProfileCreated && userState.user !== undefined && (
                        <GenericButton text="Profil" selected={props.view == "profile"} onClick={() => routeTo("/profile")} />
                    )}
                    { accountState.loggedIn && accountState.account !== undefined && accountState.userProfileCreated && userState.user !== undefined && (
                        <GenericButton text="Meine Bewerbungen" selected={props.view == "job-applications"} onClick={() => routeTo("/profile/job-applications")} />
                    )}
                    { accountState.loggedIn && accountState.account !== undefined && (
                        <GenericButton text="Abmelden" onClick={() => logoutHook(true)} />
                    )}
                </SideMenu>
                <Box width="100%" height="100%" display="flex" flexDirection="column">
                    {componentToUse()}
                </Box>
            </GenericView>
        </UserArea>
    )
}

export default ProfileRoute
