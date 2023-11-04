import {
    AppBar,
    Box,
    Button,
    Container,
    IconButton,
    Menu,
    MenuItem,
    Toolbar,
    Typography,
    useMediaQuery
} from "@mui/material";
import React, {useEffect, useState} from "react";
import BusinessSharpIcon from '@mui/icons-material/BusinessSharp';
import AccountBoxSharpIcon from '@mui/icons-material/AccountBoxSharp';
import HourglassBottomIcon from '@mui/icons-material/HourglassBottom';
import {useNavigate} from "react-router-dom";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import logo from "../assets/logo.svg";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import AuthModal from "./AuthModal";


const anonymousLoginOptions = [
    {
        menuName: "OIDC Google",
        route: "/oidc-google"
    },
    {
        menuName: "Anmelden",
        route: "/signin"
    },
    {
        menuName: "Registrieren",
        route: "register"
    }
]

function HeaderBar() {

    const logoutHook = useLogout()

    const userState = useAppSelector(state => state.user)
    const accountState = useAppSelector(state => state.account)

    const navigate = useNavigate()
    const [anonymousAnchor, setAnonymousAnchor] = React.useState<null | HTMLElement>(null);
    const [userAnchor, setUserAnchor] = React.useState<null | HTMLElement>(null);

    const showUserName =  useMediaQuery("(min-width: 1280px)")


    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };


    const handleOpenAnonymousMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnonymousAnchor(event.currentTarget);
    };

    const handleCloseAnonymousMenu = () => {
        setAnonymousAnchor(null);
    };

    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setUserAnchor(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setUserAnchor(null);
    };


    const handleRouteMenuClicked = async (route: string) => {

    }

    const routeTo = (route: string) => {
        navigate(route)
    }

    const logout = () => {
        logoutHook()
        handleCloseUserMenu()
        handleCloseAnonymousMenu()
    }


    const Spinner = () => {
        return (
            <Box sx={{flexGrow: 0, marginLeft: "auto", display: "flex", flexDirection: "row"}}>
                <Button sx={{height: "50px", padding: "5px 5px 5px 10px", border: "1px solid white", borderRadius: "0px", "&:hover": {backgroundColor: "primary.light"}}} onClick={handleOpenUserMenu}>
                    <HourglassBottomIcon fontSize="large" sx={{color: "white"}}>Test</HourglassBottomIcon>
                </Button>
            </Box>
        )
    }

    return (
        <AppBar position="static" sx={{zIndex: "100"}}>
            <Container>
                <Toolbar disableGutters>
                    <Box width="100%" display="flex" alignItems="center">
                        <img onClick={() => routeTo("/")} src={logo} height="30px" style={{filter: "brightness(0) invert(1)", cursor: "pointer"}}/>

                        <AuthModal isOpen={isModalOpen} hideModal={closeModal}/>

                        {(!accountState.loggedIn || accountState.account === undefined) &&
                            <Box sx={{flexGrow: 0, marginLeft: "auto"}}>
                                <IconButton onClick={openModal} sx={{p: 0}}>
                                    <AccountBoxSharpIcon fontSize="large" sx={{color: "white"}}>Test</AccountBoxSharpIcon>
                                </IconButton>
                            </Box>
                        }

                        {accountState.loggedIn && accountState.account !== undefined && !accountState.userProfileCreated &&
                            <Box sx={{flexGrow: 0, marginLeft: "auto", display: "flex", flexDirection: "row"}}>
                                <Button sx={{height: "50px", padding: "5px 5px 5px 10px", border: "1px solid white", borderRadius: "0px", "&:hover": {backgroundColor: "primary.light"}}} onClick={handleOpenUserMenu}>
                                    <AccountBoxSharpIcon fontSize="large" sx={{color: "white"}}>Test</AccountBoxSharpIcon>
                                    <ArrowDropDownIcon fontSize="large" sx={{color: "white"}}/>
                                </Button>
                                <Menu
                                    sx={{mt: '45px'}}
                                    id="menu-logged-in-appbar"
                                    anchorEl={userAnchor}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(userAnchor)}
                                    onClose={handleCloseUserMenu}
                                >
                                    <MenuItem onClick={() => routeTo("/profile/account")}>
                                        <Typography textAlign="center">Mein Account</Typography>
                                    </MenuItem>
                                    <MenuItem onClick={openModal}>
                                        <Typography textAlign="center">Profil erstellen</Typography>
                                    </MenuItem>
                                    <MenuItem onClick={() => logout()}>
                                        <Typography textAlign="center">Abmelden</Typography>
                                    </MenuItem>
                                </Menu>
                            </Box>
                        }

                        {accountState.loggedIn && accountState.userProfileCreated && userState.user !== undefined &&
                            <Box sx={{flexGrow: 0, marginLeft: "auto", display: "flex", flexDirection: "row"}}>

                                <Button sx={{height: "50px", marginRight: "20px", padding: "5px", border: "1px solid white", borderRadius: "0px", "&:hover": {backgroundColor: "primary.light"}}}
                                        onClick={() => routeTo("/my-companies")}>
                                    <BusinessSharpIcon sx={{color: "white"}}></BusinessSharpIcon>
                                </Button>
                                <Button sx={{height: "50px", padding: "5px 5px 5px 10px", border: "1px solid white", borderRadius: "0px", "&:hover": {backgroundColor: "primary.light"}}} onClick={handleOpenUserMenu}>
                                    <AccountBoxSharpIcon fontSize="large" sx={{color: "white"}}>Test</AccountBoxSharpIcon>
                                    {showUserName && <Typography color={"white"} margin={"0px 10px"}>{userState.user.profile.first_name} {userState.user.profile.last_name}</Typography>}
                                    <ArrowDropDownIcon fontSize="large" sx={{color: "white"}}/>
                                </Button>
                                <Menu
                                    sx={{mt: '45px'}}
                                    id="menu-logged-in-appbar"
                                    anchorEl={userAnchor}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(userAnchor)}
                                    onClose={handleCloseUserMenu}
                                >
                                    <MenuItem onClick={() => routeTo("/profile/account")}>
                                        <Typography textAlign="center">Mein Account</Typography>
                                    </MenuItem>
                                    <MenuItem onClick={() => routeTo("/profile")}>
                                        <Typography textAlign="center">Mein Profil</Typography>
                                    </MenuItem>
                                    <MenuItem onClick={() => routeTo("/profile/job-applications")}>
                                        <Typography textAlign="center">Meine Bewerbungen</Typography>
                                    </MenuItem>
                                    <MenuItem onClick={() => logout()}>
                                        <Typography textAlign="center">Abmelden</Typography>
                                    </MenuItem>
                                </Menu>
                            </Box>
                        }
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    )

}


export default HeaderBar
