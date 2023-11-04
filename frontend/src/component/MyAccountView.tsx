import React, {Fragment, useEffect, useState} from "react";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import { Container, Row, Col, Form, Button, Modal, Stack } from 'react-bootstrap';
import {authApi, userApi} from "../api/apis";
import {loginAccount, logoutAccount, updateAccount} from "../state/reducer/AccountSlice";
import {AccountDTO, InternalAccountDTO} from "../.generated/auth-service";

const MyAccountView = () => {

    const logout = useLogout()

    const dispatch = useAppDispatch()

    const navigate = useNavigate()
    const appDispatch = useAppDispatch()

    const accountState  = useAppSelector(state => state.account)
    const userState = useAppSelector(state => state.user)

    // updateAccountEmail
    // deleteAccount

    useEffect(() => {
        if (!accountState.loggedIn) {
            logout()
            navigate("/")
        }
    }, [accountState.loggedIn])

    const [newEmail, setNewEmail] = useState("");
    const [emailValidFeedback, setEmailValidFeedback] = useState("")
    const [emailError, setEmailError] = useState("")
    const [emailBuffering, setEmailBuffering] = useState(false)

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [repeatPassword, setRepeatPassword] = useState("");
    const [passwordValidFeedback, setPasswordValidFeedback] = useState("")
    const [passwordError, setPasswordError] = useState("")
    const [passwordBuffering, setPasswordBuffering] = useState(false)

    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [deletingValidFeedback, setDeletingValidFeedback] = useState("")
    const [deletingError, setDeletingError] = useState("")
    const [deletingBuffering, setDeletingBuffering] = useState(false)


    const [isGlobalBuffering, setIsGlobalBuffering] = useState(false);

    const clearEmailFeedback = () =>  {
        setEmailValidFeedback("")
        setEmailError("")
    }
    const handleEmailChange = async () => {
        clearEmailFeedback()
        setEmailBuffering(true)
        setIsGlobalBuffering(true)
        // @ts-ignore => Checked in useEffect()
        authApi.updateAccountEmail(accountState.account.id, newEmail)
            .then(updatedAccount => {
                    dispatch(updateAccount({updatedAccount: updatedAccount}))
                    setEmailValidFeedback("Login email successfully changed!")
                setEmailBuffering(false)
                setIsGlobalBuffering(false)
                }
            )
            .catch(emailError => {
                setEmailError(emailError.message)
                setEmailBuffering(false)
                setIsGlobalBuffering(false)
            })
    };

    const clearPasswordFeedback = () =>  {
        setPasswordValidFeedback("")
        setPasswordError("")
    }
    const handlePasswordChange = async () => {
        clearPasswordFeedback()
        setPasswordBuffering(true)
        setIsGlobalBuffering(true)
        authApi.loginInternalAccount((accountState.account as InternalAccountDTO).email, currentPassword)
            .then(() => {
                if (newPassword === repeatPassword) {
                    authApi.changePassword((accountState.account as InternalAccountDTO).id, newPassword)
                        .then(async () => {
                            setPasswordBuffering(false)
                            setPasswordValidFeedback("Password successfully changed! Logging out...")
                            setIsGlobalBuffering(true)
                            await new Promise(r => setTimeout(r, 1000 * 2))
                            logout()
                        })
                } else {
                    throw new Error("Passwords no not match")
                }
            })
            .catch(passwordError => {
                    setPasswordBuffering(false)
                    setIsGlobalBuffering(false)
                    setPasswordError(passwordError.message)
                }
            )


    };

    const handleDeleteAccount = () => {
        setShowDeleteModal(false)
        setDeletingBuffering(true)
        setIsGlobalBuffering(true)

        authApi.deleteAccount((accountState.account as AccountDTO).id)
            .then(async () => {
                setDeletingBuffering(false)
                setDeletingValidFeedback("Account deleted! Logging out...")
                await new Promise(r => setTimeout(r, 1000 * 2))
                setIsGlobalBuffering(false)
                logout()
            })
        .catch(deletingError => {
            setShowDeleteModal(false)
            setDeletingBuffering(false)
            setIsGlobalBuffering(false)
            setDeletingError(deletingError.message)
        })
    };



    return (
        <Container>
            <h1>Account Management</h1>
            {   accountState.account !== undefined &&
                accountState.account.accountType === "InternalAccount" && (
                    <Stack gap={5}>
                        <Row className="mt-4">
                            <Col>
                                <h2>Update Email</h2>
                                <Form>
                                    <Form.Group>
                                        <Form.Label className="mt-2">Current Email</Form.Label>
                                        <Form.Control value={accountState.account.email} readOnly />
                                        { emailValidFeedback != "" && (
                                            <div className="text-success">
                                                {emailValidFeedback}
                                            </div>
                                        )}
                                    </Form.Group>
                                    <Form.Group>
                                        <Form.Label className="mt-2">Enter New Email</Form.Label>
                                        <Form.Control
                                            value={newEmail}
                                            onChange={(e) => setNewEmail(e.target.value)}
                                        />
                                        { emailError != "" && (
                                            <div className="text-danger">
                                                {emailError}
                                            </div>
                                        )}
                                    </Form.Group>
                                    <Button variant="primary" className="mt-2" onClick={handleEmailChange} disabled={emailBuffering || isGlobalBuffering}>
                                        {emailBuffering ? "Updating Email..." : "Change Email"}
                                    </Button>
                                </Form>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <h2>Change Password</h2>
                                <Form>
                                    <Form.Group>
                                        <Form.Label className="mt-2">Enter Current Password</Form.Label>
                                        <Form.Control
                                            type="password"
                                            value={currentPassword}
                                            onChange={(e) => setCurrentPassword(e.target.value)}
                                        />
                                    </Form.Group>
                                    <Form.Group>
                                        <Form.Label className="mt-2">Enter New Password</Form.Label>
                                        <Form.Control
                                            type="password"
                                            value={newPassword}
                                            onChange={(e) => setNewPassword(e.target.value)}
                                        />
                                    </Form.Group>
                                    <Form.Group>
                                        <Form.Label className="mt-2">Repeat New Password</Form.Label>
                                        <Form.Control
                                            type="password"
                                            value={repeatPassword}
                                            onChange={(e) => setRepeatPassword(e.target.value)}
                                        />
                                        { passwordValidFeedback != "" && (
                                            <div className="text-success">
                                                {passwordValidFeedback}
                                            </div>
                                        )}
                                        { passwordError != "" && (
                                            <div className="text-danger">
                                                {passwordError}
                                            </div>
                                        )}
                                    </Form.Group>
                                    <Button variant="primary" className="mt-2" onClick={handlePasswordChange} disabled={passwordBuffering || isGlobalBuffering}>
                                        {passwordBuffering ? "Updating Password ..." : "Change Password"}
                                    </Button>
                                </Form>
                            </Col>
                        </Row>
                    </Stack>
                )}
                    <Stack gap={5} className="mt-3">
                        <Row>
                            <Col>
                                <h2>Delete Account</h2>
                                { deletingValidFeedback != "" && (
                                    <div className="text-danger">
                                        {deletingValidFeedback}
                                    </div>
                                )}
                                { deletingError != "" && (
                                    <div className="text-danger">
                                        {deletingError}
                                    </div>
                                )}
                                <Button variant="danger" onClick={() => setShowDeleteModal(true)} disabled={deletingBuffering || isGlobalBuffering}>
                                    {deletingBuffering ? "Deleting account..." : "Delete Account"}
                                </Button>
                                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                                    <Modal.Header closeButton>
                                        <Modal.Title>Confirm Deletion</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body>Are you sure you want to delete your account? This will delete both your user profile and account!</Modal.Body>
                                    <Modal.Footer>
                                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)} disabled={deletingBuffering || isGlobalBuffering}>
                                            Cancel
                                        </Button>
                                        <Button variant="danger" onClick={handleDeleteAccount} disabled={deletingBuffering || isGlobalBuffering}>
                                            Delete
                                        </Button>
                                    </Modal.Footer>
                                </Modal>
                            </Col>
                        </Row>
                    </Stack>
        </Container>
    )
}


export default MyAccountView
