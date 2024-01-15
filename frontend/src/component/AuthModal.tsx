import React, {Fragment, useEffect, useState} from 'react';
import {useAuth} from "react-oidc-context";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {authApi, userApi} from "../api/apis";
import {setAccessToken} from "../state/reducer/AuthSlice";
import {loginAccount, userProfileCreated} from "../state/reducer/AccountSlice";
import {loginUser} from "../state/reducer/UserSlice";
import {Box, Button, CircularProgress, Dialog, TextField, Typography} from "@mui/material";
import GoogleIcon from '@mui/icons-material/Google';


interface ModalDialogProps {
    isOpen: boolean,
    hideModal: () => void;
}

function AuthModal({isOpen, hideModal}: ModalDialogProps) {

    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [error, setError] = useState("");
    const accountState = useAppSelector(state => state.account)
    const [accountEmail, setAccountEmail] = useState("");
    const [loading, setLoading] = useState<false | 'login' | 'oidc-profile' | 'internal-profile'>(false);
    const [relogin, setRelogin] = useState(false)

    const [registerAccountEmail, setRegisterAccountEmail] = useState("");
    const [registerPassword, setRegisterPassword] = useState('');

    const [userProfileEmail, setUserProfileEmail] = useState(accountState.account && 'email' in accountState.account && accountState.account.email || "");

    const auth = useAuth();

    useEffect(() => {
        if (isOpen) {
            initStates()
        } else {
            cleanupStates();
        }
    }, [isOpen]);

    const initStates = () => {
        if (!accountState.loggedIn || accountState.account === undefined) {
            cleanupStates()
        } else if (accountState.loggedIn && accountState.account != undefined && !accountState.userProfileCreated) {
            if (accountState.account.accountType === "InternalAccount") {
                setUserProfileEmail(accountState.account.email)
            }
        } else {
            cleanupStates()
            hideModal()
        }
    }

    const cleanupStates = () => {
        setRelogin(false)
    }

    const dispatch = useAppDispatch()

    const handleOidcLogin = async () => {
        setLoading("login")
        auth.signinPopup()
            .then(user => {
                if (user.id_token == undefined) {
                    throw new Error("OIDC Login canceled");
                }
                const id_token = user.id_token
                authApi.loginOidcAccount(id_token)
                    .then(accountLoginResponse => {
                        const account = accountLoginResponse.account;
                        if (account.accountType !== "OidcAccount") {
                            throw new Error("OIDC Login failed. No OIDC Account")
                        }

                        const access_token = accountLoginResponse.access_token;
                        if (access_token == null) {
                            throw new Error("OIDC Login failed. No Access Token provided.")
                        }
                        dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));

                        userApi.fetchUser(account.id)
                            .then(async user => {
                                setLoading(false)
                                dispatch(loginAccount({account: account}))
                                dispatch(loginUser({user: user}))
                                dispatch(userProfileCreated({userProfileCreated: true}))
                                setError("")
                                hideModal()
                            })
                            .catch(() => {
                                setLoading(false)
                                dispatch(loginAccount({account: account}))
                                dispatch(userProfileCreated({userProfileCreated: false}))
                                console.log("No user fetched");
                            })
                    }).catch(error => {
                        setLoading(false)
                        if(error.status != undefined && error.status == 502) {
                            setError("Backend not ready.")
                        } else {
                            setError(error.details)
                        }
                })
            })
            .catch(error => {
                setLoading(false)
                if(error.status != undefined && error.status == 502) {
                    setError("Backend not ready.")
                } else {
                    setError(error.details)
                }
            });

    }

    const handleInternalLogin = async () => {
        setLoading("login")
        authApi.loginInternalAccount(accountEmail, password)
            .then(accountLoginResponse => {
                const account = accountLoginResponse.account;
                if (account.accountType !== "InternalAccount") {
                    throw new Error("Internal Login failed. No Internal Account")
                }

                const access_token = accountLoginResponse.access_token;
                if (access_token == null) {
                    throw new Error("Internal Login failed. No Access Token provided.")
                }
                dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));

                userApi.fetchUser(account.id)
                    .then(async user => {
                        setLoading(false)
                        dispatch(loginAccount({account}))
                        dispatch(loginUser({user: user}))
                        dispatch(userProfileCreated({userProfileCreated: true}))
                        setError("")
                        hideModal()
                    })
                    .catch(() => {
                        setLoading(false)
                        dispatch(loginAccount({account}))
                        dispatch(userProfileCreated({userProfileCreated: false}))
                        console.log("No user fetched");
                        setUserProfileEmail(account.email)
                    })
            })
            .catch(error => {
                setLoading(false)
                if(error.status != undefined && error.status == 502) {
                    setError("Backend not ready.")
                } else if(error.status != undefined && error.status == 401) {
                    setError("Wrong credentials.")
                } else {
                    setError(error.details)
                }
            });
    }

    const handleInternalRegistration = async () => {
        setLoading("login")
        return authApi.registerNewInternalAccount(accountEmail, password)
            .then(accountLoginResponse => {
                const account = accountLoginResponse.account;
                if (account.accountType !== "InternalAccount") {
                    throw new Error("Internal Login failed. No Internal Account")
                }
                dispatch(loginAccount({account}))

                const access_token = accountLoginResponse.access_token;
                if (access_token == null) {
                    throw new Error("Internal Login failed. No Access Token provided.")
                }
                setLoading(false)
                dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));
                setUserProfileEmail(account.email)
                setError("")
            })
            .catch(error => {
                setLoading(false)
                if(error.status != undefined && error.status == 502) {
                    setError("Backend not ready.")
                } else {
                    setError(error.details)
                }
            })
    };

    const logout = useLogout()

    const handleProfileCreation = (oidc: boolean) => {
        setLoading(oidc && "oidc-profile" || "internal-profile")
        userApi.createUserProfile(userProfileEmail, firstName, lastName)
            .then(async user => {
                dispatch(loginUser({user: user}))
                dispatch(userProfileCreated({userProfileCreated: true}))

                if (accountEmail && password && accountState.account?.accountType == "InternalAccount") {
                    handleInternalLogin().then(() => {
                        setLoading(false)
                    }).catch(() => logout())
                } else {
                    setLoading(false)
                    setRelogin(true)
                    if(accountState.account?.accountType == "OidcAccount") {
                        handleOidcLogin()
                    }
                    logout()
                }
            })
            .catch(error => {
                setLoading(false)
                if(error.status != undefined && error.status == 502) {
                    setError("Backend not ready.")
                } else {
                    setError(error.details)
                }
            });
    };

    //if (!isOpen) {
    //    return null;
    //}

    return (
        <div>
            {(!accountState.loggedIn || accountState.account == undefined) && (
                <Dialog onClose={hideModal} open={isOpen}
                        PaperProps={{sx: {borderRadius: "0px", width: "90%", maxWidth: "580px"}}}>
                    {loading == "login" &&
                        <Box sx={{position: "absolute", width: "100%", height: "100%", display: "flex", justifyContent: "center", alignItems: "center"}}>
                            <CircularProgress size={70}/>
                        </Box>
                    }
                    <Box sx={{padding: "10px"}}>
                        <Typography variant="h5">{relogin && "Profil erstellt. Bitte melden sie sich ernaut an:" || "Login:"}</Typography>
                        <br/>
                        <Box sx={{...(loading == "login" && {visibility: "hidden"})}}>

                            <Box display="flex" flexDirection="row" flexWrap="wrap" rowGap="10px">
                                <TextField type="email" sx={{width: "265px", marginRight: "20px"}} label={"Email"}
                                           value={accountEmail} onChange={(event) => setAccountEmail(event.target.value)}/>
                                <TextField type="password" sx={{width: "265px"}} label={"Password"} value={password}
                                           onChange={(event) => setPassword(event.target.value)}/>
                            </Box>
                            {error && <Typography sx={{color: "red", margin: "10px 0px 10px 0px"}}>{error}</Typography>}
                            <Box marginLeft="auto">
                                <Button variant="contained"
                                        sx={{marginTop: "20px", width: "120px", padding: "5px 30px", marginRight: "10px"}}
                                        onClick={handleInternalRegistration}>Registrieren</Button>
                                <Button variant="contained"
                                        sx={{marginTop: "20px", width: "120px", padding: "5px 30px", marginRight: "10px"}}
                                        onClick={handleInternalLogin}>Login</Button>
                            </Box>
                            <Box width="100%" display="flex" justifyContent="center" alignItems="center" gap="10px"
                                 padding="20px 20px 10px 20px">
                                <Box width="20%" display="inline-block">
                                    <hr style={{borderTop: "1px solid black"}}/>
                                </Box>
                                <Typography display="inline-block">OIDC Login</Typography>
                                <Box width="20%" display="inline-block">
                                    <hr style={{borderTop: "1px solid black"}}/>
                                </Box>
                            </Box>
                            <Box width="100%" display="flex" justifyContent="center" alignItems="center">
                                <Button onClick={handleOidcLogin} variant="contained"
                                        sx={{marginTop: "10px", padding: "5px 30px 5px 25px"}}><GoogleIcon
                                    sx={{margin: "0px 10px 0px 5px"}}/>Login mit Google</Button>
                            </Box>
                        </Box>
                    </Box>
                </Dialog>

            )}
            {accountState.loggedIn && accountState.account != undefined && !accountState.userProfileCreated && (
                <Dialog onClose={hideModal} open={isOpen}
                        PaperProps={{sx: {borderRadius: "0px", width: "90%", maxWidth: "580px"}}}>
                    {(loading == "internal-profile" || loading == "oidc-profile") &&
                        <Box sx={{position: "absolute", width: "100%", height: "100%", display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column"}}>
                            {loading == "oidc-profile" &&
                                <Fragment>
                                    <Typography sx={{marginBottom: "5px"}}>Bitte haben sie einen Moment Geduld...</Typography>
                                    <Typography sx={{marginBottom: "15px"}}>Sie werden nach Abschluss ausgeloggt.</Typography>
                                </Fragment>
                            }
                            <CircularProgress size={70}/>
                        </Box>
                    }
                    <Box sx={{padding: "10px"}}>
                        <Typography variant="h5">Schließe die Anmeldung ab:</Typography>
                        <br/>
                        <Box sx={{...((loading == "internal-profile" || loading == "oidc-profile") && {visibility: "hidden"})}}>
                            <TextField type="email" sx={{width: "265px"}} label={"Profil Email"} value={userProfileEmail}
                                       onChange={(event) => setUserProfileEmail(event.target.value)}/>
                            <Box display="flex" flexDirection="row" flexWrap="wrap" rowGap="10px">
                                <TextField sx={{width: "265px", marginRight: "20px", marginTop: "10px"}} label={"Vorname"}
                                           value={firstName} onChange={(event) => setFirstName(event.target.value)}/>
                                <TextField sx={{width: "265px", marginTop: "10px"}} label={"Nachname"} value={lastName}
                                           onChange={(event) => setLastName(event.target.value)}/>
                            </Box>
                            {error && <Typography sx={{color: "red"}}>{error}</Typography>}
                            {(!accountEmail || !password || accountState.account.accountType != "InternalAccount") &&
                                <Typography sx={{margin: "30px 10px 0px 10px"}}>
                                    Sie werden nach Erstellung des Profils abgemeldet, um die Informationen zu übernehmen.
                                </Typography>
                            }
                            <Box marginLeft="auto">
                                <Button variant="contained" sx={{marginTop: "30px", padding: "5px 30px", marginRight: "10px"}}
                                        onClick={() => handleProfileCreation(accountState.account?.accountType != "InternalAccount")}>Profil vervollständigen</Button>
                            </Box>
                        </Box>
                    </Box>
                </Dialog>

            )}
        </div>
    );
}

export default AuthModal;
