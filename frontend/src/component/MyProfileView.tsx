import React, {useEffect, useState} from "react";
import {Button, Col, Container, Form, Modal, Row, Stack} from "react-bootstrap";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import {UserDTO, UserProfileDTO, UserSettingsDTO} from "../.generated/user-service";
import {userApi} from "../api/apis";
import {logoutUser, updateUser} from "../state/reducer/UserSlice";
import {userProfileCreated} from "../state/reducer/AccountSlice";

function MyProfileView() {

    const userState = useAppSelector(state => state.user)

    useEffect(() => {
        clearErrorsAndFeedback()
    }, []);

    useEffect(() => {
        if(userState.user === undefined) {
            console.log("Logout #1")
            //logout()
            navigate("/")
        } else {
            updateFields()
        }
    },[userState.user])

    const [updateFeedback, setUpdateFeedback] = useState("")
    const [updateError, setUpdateError] = useState("")

    const [deleteFeedback, setDeleteFeedback] = useState("")
    const [deleteError, setDeleteError] = useState("")

    const [subscribeError, setSubscribeError] = useState("")
    const [nightModeError, setNightModeError] = useState("")

    const clearErrorsAndFeedback = () => {
        setUpdateError("")
        setUpdateFeedback("")
        setDeleteError("")
        setDeleteError("")
    }


    const [isSubscribed, setSubscribed] = useState(false);
    const [subscriptionDays, setSubscriptionDays] = useState(0);
    const [isNightMode, setNightMode] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    const dispatch = useAppDispatch()
    const navigate = useNavigate()

    const updateFields = () => {
        const user: UserDTO = (userState.user as UserDTO)
        setSubscribed(user.subscription.subscribed)
        setNightMode(user.settings.night_mode_active)
        setEmail(user.profile.user_profile_email)
        setFirstName(user.profile.first_name)
        setLastName(user.profile.last_name)
    }

    const handleSubscribeClick = () => {
        clearErrorsAndFeedback()

        const days = subscriptionDays

        const user: UserDTO = (userState.user as UserDTO)

        if (!isNaN(days) && days > 0) {

            userApi.subscribe(user.id,days)
                .then(user => {
                    dispatch(updateUser({ user: user }))
                    setSubscribed(true);
                })
                .catch(subscriptionError => {
                    setSubscribeError(subscriptionError.details)
                })
        } else {
            alert("Please enter a valid number of days for the subscription.");
        }
    };

    const handleModeToggle = () => {
        clearErrorsAndFeedback()
        const user: UserDTO = (userState.user as UserDTO)
        const userSettings: UserSettingsDTO = {
            night_mode_active: user.settings.night_mode_active
        }
        userSettings.night_mode_active = !isNightMode
        userApi.updateUserSettings(user.id, userSettings)
            .then(user => {
                dispatch(updateUser({user: user}))
                setNightMode(!isNightMode);
            })
            .catch(nightModeError => {
                setNightModeError(nightModeError.details)
            });

    };

    const handleUpdateProfile = () => {
        clearErrorsAndFeedback()
        const user: UserDTO = (userState.user as UserDTO)
        const updatedProfile: UserProfileDTO = {
            user_profile_email: email,
            first_name: firstName,
            last_name: lastName
        }
        userApi.updateUserProfile(user.id, updatedProfile)
            .then(user => {
                dispatch(updateUser({user: user}))
                setUpdateFeedback("Profile updated!")
            })
            .catch(updatedProfileError => {
                setUpdateError(updatedProfileError.details)
            })
    };

    const handleDeleteProfile = () => {
        clearErrorsAndFeedback()
        setShowDeleteModal(true);
    };

    const handleConfirmDelete = async () => {
        setShowDeleteModal(false);
        const user: UserDTO = (userState.user as UserDTO)
        userApi.deleteUser(user.id)
            .then(async () => {
                await new Promise(r => setTimeout(r, 1000 * 2))
                dispatch(userProfileCreated({ userProfileCreated: false }))
                console.log("Logout #2")
                //logout()
                dispatch(logoutUser())
            })
            .catch(deleteUserError => {
                console.log(deleteUserError)
                setDeleteError(deleteUserError.details)
            })
    };

    const handleCloseDeleteModal = () => {
        setShowDeleteModal(false);
    };

    return (
        <Container>
            <h1>User Profile Management</h1>
            { userState.user !== undefined && (
                <Stack gap={5}>
                    <Row className="mt-4">
                        <Col>
                            <div className="mb-3 d-flex align-items-end">
                                {!isSubscribed && (
                                    <Form.Group controlId="formSubscriptionDays" className="mb-0">
                                        <Form.Label className="mb-0">Subscription Days</Form.Label>
                                        <Form.Control
                                            type="number"
                                            value={subscriptionDays}
                                            onChange={(e) => setSubscriptionDays(parseInt(e.target.value, 10))}
                                            className="mb-0"
                                        />
                                    </Form.Group>)}
                                {!isSubscribed && (
                                    <Button onClick={handleSubscribeClick} className="me-2 mb-0">
                                        Subscribe Now
                                    </Button>)}
                                { subscribeError != "" && (
                                    <div className="text-danger">
                                        {subscribeError}
                                    </div>
                                )}

                                {isSubscribed && (
                                    <Button className="me-2 mb-0" disabled={isSubscribed}>
                                        Subscribed until: {(userState.user as UserDTO).subscription.subscribedUntil}
                                    </Button>)}
                                <Button onClick={handleModeToggle} variant="secondary" className="mb-0">
                                    {isNightMode ? "Switch to Light Mode" : "Switch to Night Mode"}
                                </Button>
                                { nightModeError != "" && (
                                    <div className="text-danger">
                                        {nightModeError}
                                    </div>
                                )}
                            </div>

                            <Form>
                                <Form.Group controlId="formEmail">
                                    <Form.Label>Secondary Email (Used for Notifications and Newsletter)</Form.Label>
                                    <Form.Control
                                        type="email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                    />
                                </Form.Group>

                                <Form.Group controlId="formFirstName">
                                    <Form.Label>First Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={firstName}
                                        onChange={(e) => setFirstName(e.target.value)}
                                    />
                                </Form.Group>

                                <Form.Group controlId="formLastName">
                                    <Form.Label>Last Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={lastName}
                                        onChange={(e) => setLastName(e.target.value)}
                                    />
                                </Form.Group>
                            </Form>

                            <div className="mt-3">
                                <Button onClick={handleUpdateProfile} className="me-3">
                                    Update Profile
                                </Button>
                                <Button onClick={handleDeleteProfile} variant="danger">
                                    Delete Profile
                                </Button>
                            </div>
                            { updateFeedback != "" && (
                                <div className="text-success">
                                    {updateFeedback}
                                </div>
                            )}
                            { updateError != "" && (
                                <div className="text-danger">
                                    {updateError}
                                </div>
                            )}


                            <Modal show={showDeleteModal} onHide={handleCloseDeleteModal}>
                                <Modal.Header closeButton>
                                    <Modal.Title>Confirm Delete</Modal.Title>
                                </Modal.Header>
                                <Modal.Body>Are you sure you want to delete your profile?</Modal.Body>
                                <Modal.Footer>
                                    <Button variant="secondary" onClick={handleCloseDeleteModal}>
                                        Cancel
                                    </Button>
                                    <Button variant="danger" onClick={handleConfirmDelete}>
                                        Delete
                                    </Button>
                                </Modal.Footer>
                            </Modal>
                        </Col>
                    </Row>
                </Stack>
            )}
        </Container>
    );
}

export default MyProfileView;
