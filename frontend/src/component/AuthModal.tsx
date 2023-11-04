import React, {useEffect, useState} from 'react';
import {useAuth} from "react-oidc-context";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {authApi, userApi} from "../api/apis";
import {setAccessToken} from "../state/reducer/AuthSlice";
import {loginAccount, userProfileCreated} from "../state/reducer/AccountSlice";
import {loginUser} from "../state/reducer/UserSlice";
import {Box, Button, Dialog, TextField, Typography} from "@mui/material";
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
    }

    const dispatch = useAppDispatch()

    const handleOidcLogin = async () => {
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
                        dispatch(loginAccount({account: account}))

                        const access_token = accountLoginResponse.access_token;
                        if (access_token == null) {
                            throw new Error("OIDC Login failed. No Access Token provided.")
                        }
                        dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));

                        userApi.fetchUser(account.id)
                            .then(async user => {
                                dispatch(loginUser({user: user}))
                                dispatch(userProfileCreated({userProfileCreated: true}))

                                hideModal()
                            })
                            .catch(() => {
                                dispatch(userProfileCreated({userProfileCreated: false}))
                                console.log("No user fetched");
                            })
                    })
            })
            .catch(error => {
                    setError(error.message)
                }
            );

    }

    const handleInternalLogin = async () => {
        authApi.loginInternalAccount(accountEmail, password)
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
                dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));

                userApi.fetchUser(account.id)
                    .then(async user => {
                        dispatch(loginUser({user: user}))
                        dispatch(userProfileCreated({userProfileCreated: true}))
                        hideModal()

                    })
                    .catch(() => {
                        dispatch(userProfileCreated({userProfileCreated: false}))
                        console.log("No user fetched");
                        setUserProfileEmail(account.email)
                    })
            })
            .catch(error => {
                    setError(error.message)
                }
            );
    }

    const handleInternalRegistration = async () => {
        authApi.registerNewInternalAccount(accountEmail, password)
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
                dispatch(setAccessToken({accessToken: accountLoginResponse.access_token}));
                setUserProfileEmail(account.email)
            })
            .catch(error => {
                setError(error.message)
            })
    };

    const handleProfileCreation = () => {


        userApi.createUserProfile(userProfileEmail, firstName, lastName)
            .then(async user => {
                dispatch(loginUser({user: user}))
                dispatch(userProfileCreated({userProfileCreated: true}))
                //logout()
            })
            .catch(error => {
                setError(error.message)
            });
    };

    //if (!isOpen) {
    //    return null;
    //}

    return (
        <div>
            {(!accountState.loggedIn || accountState.account == undefined) && (
                <Dialog onClose={hideModal} open={isOpen}
                        PaperProps={{sx: {padding: "10px", borderRadius: "0px", width: "90%", maxWidth: "580px"}}}>
                    <Typography variant="h5">Login:</Typography>
                    <br/>
                    <Box display="flex" flexDirection="row" flexWrap="wrap" rowGap="10px">
                        <TextField type="email" sx={{width: "265px", marginRight: "20px"}} label={"Email"}
                                   value={accountEmail} onChange={(event) => setAccountEmail(event.target.value)}/>
                        <TextField type="password" sx={{width: "265px"}} label={"Password"} value={password}
                                   onChange={(event) => setPassword(event.target.value)}/>
                    </Box>
                    {error && <Typography sx={{color: "red"}}>{error}</Typography>}
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
                </Dialog>

            )}
            {accountState.loggedIn && accountState.account != undefined && !accountState.userProfileCreated && (
                <Dialog onClose={hideModal} open={isOpen}
                        PaperProps={{sx: {padding: "10px", borderRadius: "0px", width: "90%", maxWidth: "580px"}}}>
                    <Typography variant="h5">Schließe die Anmeldung ab:</Typography>
                    <br/>
                    <TextField type="email" sx={{width: "265px"}} label={"Profil Email"} value={userProfileEmail}
                               onChange={(event) => setUserProfileEmail(event.target.value)}/>
                    <Box display="flex" flexDirection="row" flexWrap="wrap" rowGap="10px">
                        <TextField sx={{width: "265px", marginRight: "20px", marginTop: "10px"}} label={"Vorname"}
                                   value={firstName} onChange={(event) => setFirstName(event.target.value)}/>
                        <TextField sx={{width: "265px", marginTop: "10px"}} label={"Nachname"} value={lastName}
                                   onChange={(event) => setLastName(event.target.value)}/>
                    </Box>
                    {error && <Typography sx={{color: "red"}}>{error}</Typography>}
                    <Box marginLeft="auto">
                        <Button variant="contained" sx={{marginTop: "30px", padding: "5px 30px", marginRight: "10px"}}
                                onClick={handleProfileCreation}>Profil vervollständigen</Button>
                    </Box>
                </Dialog>

            )}
        </div>
    );
}

export default AuthModal;
