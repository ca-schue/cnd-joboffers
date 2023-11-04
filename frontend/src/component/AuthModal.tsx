import React, {useEffect, useState} from 'react';
import { Modal, Button} from "react-bootstrap";
import { useAuth } from "react-oidc-context";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {authApi, userApi} from "../api/apis";
import {AccountLoginResponseDTO, AccountDTO, InternalAccountDTO, OidcAccountDTO} from "../.generated/auth-service";
import {setAccessToken} from "../state/reducer/AuthSlice";
import {loginAccount, userProfileCreated} from "../state/reducer/AccountSlice";
import {loginUser} from "../state/reducer/UserSlice";
import store from "../state/Store";
import {Box, CircularProgress} from "@mui/material";


interface ModalDialogProps {
    isOpen: boolean,
    hideModal: () => void;
}

function ModalDialog({ isOpen, hideModal}: ModalDialogProps) {

    const [loading, setLoading] = React.useState(false);

    const [modalTitle, setModalTitle] = useState('Login or Create an Account');
    const [password, setPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [error, setError] = useState("");
    const accountState = useAppSelector(state => state.account)
    const [accountEmail, setAccountEmail] = useState("");
    const [userProfileEmail, setUserProfileEmail] = useState("");

    const auth = useAuth();
    const logout = useLogout()

    useEffect(() => {
            if (isOpen) {
                initStates()
            } else {
                cleanupStates();
            }
        },[isOpen]);

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
        setAccountEmail('');
        setPassword('');
        setFirstName('');
        setLastName('');
        setModalTitle('Login or Create an Account')
        setError("")
        setLoading(false)
    }

    const dispatch = useAppDispatch()

    const handleOidcLogin = async () => {

        setLoading(true)

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
                                dispatch(userProfileCreated({ userProfileCreated: true}))

                                setModalTitle("Welcome back!")
                                await new Promise(r => setTimeout(r, 1000 * 2))
                                setLoading(false)
                                hideModal()
                            })
                            .catch(() => {
                                dispatch(userProfileCreated({ userProfileCreated: false}))
                                console.log("No user fetched");
                                setLoading(false)
                    } )
                    })
            })
            .catch(error => {
                    setError(error.message)
                setLoading(false)
                }
            );

    }

    const handleInternalLogin = async () => {
        setLoading(true)

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
                        dispatch(userProfileCreated({ userProfileCreated: true}))

                        setModalTitle("Welcome back!")
                        await new Promise(r => setTimeout(r, 1000 * 2))
                        setLoading(false)
                        hideModal()

                    })
                    .catch(() => {
                        dispatch(userProfileCreated({ userProfileCreated: false}))
                        console.log("No user fetched");
                        setUserProfileEmail(account.email)
                        setLoading(false)
                    })
            })
            .catch(error => {
                setError(error.message)
                setLoading(false)
            }
        );
    }

    const handleInternalRegistration = async () => {

        setLoading(true)

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
                setLoading(false)
            })
            .catch(error => {
                setError(error.message)
                setLoading(false)
            })
    };

     const handleProfileCreation = () => {

        setLoading(true)

        userApi.createUserProfile(userProfileEmail,firstName,lastName)
            .then(async user => {
                dispatch(loginUser({user: user}))
                dispatch(userProfileCreated({ userProfileCreated: true}))

                setModalTitle("New Profile created!");
                await new Promise(r => setTimeout(r, 1000 * 2))
                setLoading(false)
                hideModal()
                //logout()
            })
            .catch(error => {
                setError(error.message)
                setLoading(false)
            });
    };

    return (
        <div>
            {/*loading &&
                <Box width="100%" height="100%" bgcolor="#484848ab" position="absolute" zIndex="10" display="flex" justifyContent="center" alignItems="center">
                    <CircularProgress color="primary" size={"5rem"} sx={{marginTop: "-5%"}}/>
                </Box>
            */}
            {(!accountState.loggedIn || accountState.account === undefined) && (
                <Modal show={isOpen} onHide={hideModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>Login or register account</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <input
                            type="text"
                            placeholder="Account Email"
                            onChange={(e) => setAccountEmail(e.target.value)}
                        />
                        <input
                            type="password"
                            placeholder="Password"
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        {error != "" && <p>{error}</p>}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            className="me-2"
                            onClick={handleOidcLogin}
                        >
                            Login with Google
                        </Button>
                        <Button
                            className="me-2"
                            onClick={handleInternalLogin}
                        >
                            Anmelden
                        </Button>
                        <Button
                            onClick={handleInternalRegistration}
                        >
                            Account erstellen
                        </Button>
                    </Modal.Footer>
                </Modal>
            )}
            { accountState.loggedIn && accountState.account != undefined && !accountState.userProfileCreated && (
                <Modal show={isOpen} onHide={hideModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>Use all features by completing your user profile</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <input
                            type="text"
                            placeholder="User Profile Email"
                            value={userProfileEmail}
                            onChange={(e) => setUserProfileEmail(e.target.value)}
                        />
                        <input
                            type="text"
                            placeholder="Vorname"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                        />
                        <input
                            type="text"
                            placeholder="Nachname"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                        />
                    </Modal.Body>
                    {error != "" && <p>{error}</p>}
                    <Modal.Footer>
                        <Button
                            variant="primary"
                            onClick={handleProfileCreation}
                        >
                            Benutzerprofil anlegen
                        </Button>
                    </Modal.Footer>
                </Modal>
            )}
        </div>
    );
}

export default ModalDialog;