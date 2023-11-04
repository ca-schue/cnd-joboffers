import React, {useEffect, useState} from 'react';
import {Button, Modal, Form, ListGroup, Container, Row, Col} from 'react-bootstrap';
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {Company} from "../.generated/user-service/models/Company";
import {CompanyDTO, CompanyIdDTO, PublicUserProfileDTO, UserDTO, UserIdDTO} from "../.generated/user-service";
import {userApi} from "../api/apis";
import {addMemberCompany, setOwnerCompany, updateMemberCompany} from "../state/reducer/MyCompaniesSlice";
import {updateUser} from "../state/reducer/UserSlice";
import {UUID} from "../.generated/user-service/models/UUID";
import * as Stream from "stream";
import {forEach, map} from "react-bootstrap/ElementChildren";

interface MyCompanyViewProps {
    loading: ((is_loading: boolean) => void)
}

const MyCompanyView = (props: MyCompanyViewProps) => {

    const userState = useAppSelector(state => state.user)
    const myCompaniesState = useAppSelector(state => state.myNewCompanies)
    const [hasCompany, setHasCompany] = useState(false)
    const logout = useLogout()
    const dispatch = useAppDispatch()

    const [members, setMembers] = useState({members: ([] as PublicUserProfileDTO [])})

    const [createCompanyError, setCreateCompanyError] = useState("")
    const [createCompanyFeedback, setCreateCompanyFeedback] = useState("")

    const [deleteCompanyError, setDeleteCompanyError] = useState("")
    const [deleteCompanyFeedback, setDeleteCompanyFeedback] = useState("")

    const [premiumPartnerError, setPremiumPartnerError] = useState("")

    const [updateBasicCompanyDataError, setUpdateBasicCompanyDataError] = useState("")
    const [updateBasicCompanyDataFeedback, setUpdateBasicCompanyDataFeedback] = useState("")

    const [updateCompanyLinksError, setUpdateCompanyLinksError] = useState("")
    const [updateCompanyLinksFeedback, setUpdateCompanyLinksFeedback] = useState("")

    const [inviteUserError, setInviteUserError] = useState("")
    const [inviteUserFeedback, setInviteUserFeedback] = useState("")

    const clearErrorsAndFeedback = () => {
        setCreateCompanyError("")
        setCreateCompanyFeedback("")
        setDeleteCompanyError("")
        setDeleteCompanyFeedback("")
        setPremiumPartnerError("")
        setUpdateBasicCompanyDataError("")
        setUpdateBasicCompanyDataFeedback("")
        setUpdateCompanyLinksError("")
        setUpdateCompanyLinksFeedback("")
        setInviteUserError("")
        setInviteUserFeedback("")
    }

    useEffect(() => {
        clearErrorsAndFeedback()
        if(userState.user === undefined) {
            logout()
        } else {
            if(myCompaniesState.owner_of === undefined) {
                setHasCompany(false)
            } else {
                setHasCompany(true);

            }
        }
    }, [])

    useEffect(() => {
        if(userState.user === undefined) {
            logout()
        } else {
            if(myCompaniesState.owner_of === undefined) {
                setHasCompany(false)
            } else {
                setHasCompany(true)
                setFields(myCompaniesState.owner_of)
                    .then(() => {
                        props.loading(false)
                    })
                    .catch(error => {
                        console.log(error)
                        props.loading(false)
                        logout()
                    })
            }
        }
    }, [userState.user, myCompaniesState.owner_of]);

    const loadMemberDetails = async (memberId: UserIdDTO) => {
        try {
            const userProfile = await userApi.fetchPublicUserProfile(memberId);
            return (
                <ListGroup.Item key={userProfile.id}>
                    <Row>
                        <Col md={4}>
                            <strong>Member ID:</strong> {userProfile.id}
                        </Col>
                        <Col md={4}>
                            <strong>First Name:</strong> {userProfile.first_name}
                        </Col>
                        <Col md={4}>
                            <strong>Last Name:</strong> {userProfile.last_name}
                        </Col>
                        <Col md={12}>
                            <strong>User Profile Email:</strong> {userProfile.user_profile_email}
                        </Col>
                    </Row>
                </ListGroup.Item>
            );
        } catch (error) {
            console.log(error);
        }
    }

    const setFields = async (existingCompany: CompanyDTO) => {
        props.loading(true);
        let memberPromises: Promise<void> [] = [];
        (myCompaniesState.owner_of as CompanyDTO).members.forEach(member => {
            memberPromises.push(
                userApi.fetchPublicUserProfile(member)
                    .then(userProfile => {
                        const newMembers: PublicUserProfileDTO [] = members.members;
                        let found = false;
                        newMembers.forEach(member => {
                            if(member.id == userProfile.id) {
                                found = true;
                            }
                        })
                        if (!found) {
                            newMembers.push(userProfile)
                            setMembers({members: newMembers})
                        }
                    })
            )
        });
        await Promise.all(memberPromises)

        setPremiumPartner(existingCompany.partner_program.partnered)
        if (existingCompany.partner_program.partner_until !== undefined) {
            setPartnerUntil(existingCompany.partner_program.partner_until)
        }

        setCompanyDetails({
            name: existingCompany.details.name,
            description: existingCompany.details.description,
            tags: existingCompany.details.tags,
            location: existingCompany.details.location === undefined ? "" : existingCompany.details.location
        })

        setCompanyLinks({
            social_media: existingCompany.links.social_media === undefined ? [] : existingCompany.links.social_media,
            website_url: existingCompany.links.website_url === undefined ? "" : existingCompany.links.website_url
        })
    }

    const [premiumPartner, setPremiumPartner] = useState(false);
    const [partnerUntil, setPartnerUntil] = useState("");
    const [companyDetails, setCompanyDetails] = useState({
        name: "",
        description: "",
        tags: ([] as string []),
        location: "",
    });
    const [companyLinks, setCompanyLinks] = useState({
        website_url: (undefined as undefined | string),
        social_media: (undefined as undefined | string []),
    });

    const [newUserEmail, setNewUserEmail] = useState('');
    const [showDeleteModal, setShowDeleteModal] = useState(false);

    const handleCreateCompany = () => {
        clearErrorsAndFeedback()
        const currentUser: UserDTO = (userState.user as UserDTO)

        props.loading(true)
        userApi.createNewCompany(
            currentUser.id,
            companyDetails,
            companyLinks
        )
            .then(newCompany => {
                const ownerUser = structuredClone(currentUser)
                ownerUser.associations.owner_of = newCompany.id
                dispatch(updateUser({user: ownerUser}))
                dispatch(setOwnerCompany({company: newCompany}))
                dispatch(addMemberCompany({company: newCompany}))
                setCreateCompanyFeedback("Company created. Logging out.")
                new Promise(r => setTimeout(r, 1000 * 2))
                    .then(() => {
                        props.loading(false)
                        logout();
                    })
            })
            .catch(error => {
                setCreateCompanyError(error.body.error)
                props.loading(false)
            })
    }

    const handleUpdateBasisInfo = () => {
        clearErrorsAndFeedback()
        const existingCompany: CompanyDTO = myCompaniesState.owner_of as CompanyDTO
        props.loading(true)
        userApi.updateCompanyDetails(
            existingCompany.id,
            companyDetails
        )
            .then(updatedCompany => {
                dispatch(setOwnerCompany({company: updatedCompany}))
                dispatch(updateMemberCompany({updatedMemberCompany: updatedCompany}))
                props.loading(false)
                setUpdateBasicCompanyDataFeedback("Company data updated.")
            })
            .catch(error => {
                setUpdateBasicCompanyDataError(error.body.error)
                props.loading(false)
            })
    };

    const handleUpdateFirmenLinks = () => {
        clearErrorsAndFeedback()
        const existingCompany: CompanyDTO = myCompaniesState.owner_of as CompanyDTO
        props.loading(true)
        userApi.updateCompanyLinks(
            existingCompany.id,
            {
                homepage: companyLinks.website_url,
                social_media: companyLinks.social_media
            }
        )
            .then(updatedCompany => {
                dispatch(setOwnerCompany({company: updatedCompany}))
                dispatch(updateMemberCompany({updatedMemberCompany: updatedCompany}))
                props.loading(false)
                setUpdateCompanyLinksFeedback("Company links updated")
            })
            .catch(error => {
                setUpdateCompanyLinksError(error)
                props.loading(false)
            })
    };

    const handleInviteUser = () => {
        clearErrorsAndFeedback()
        const existingCompany: CompanyDTO = myCompaniesState.owner_of as CompanyDTO
        props.loading(true)
        userApi.inviteUserToCompany(
            existingCompany.id,
            newUserEmail
        )
            .then( () => {
                setNewUserEmail("")
                props.loading(false)
                setInviteUserFeedback("User '" + newUserEmail + "' invited.")
            })
            .catch(error => {
                setInviteUserError("Error when inviting user '" + newUserEmail + "': " + error.body.error)
                props.loading(false)
            })
    };

    const handleDeleteCompany = () => {
        clearErrorsAndFeedback()
        const existingCompany: CompanyDTO = myCompaniesState.owner_of as CompanyDTO
        setShowDeleteModal(false)
        props.loading(true)
        userApi.deleteCompany(existingCompany.id)
            .then( () => {
                setDeleteCompanyFeedback("Company deleted. Logging out.")
                new Promise(r => setTimeout(r, 1000 * 2))
                    .then(() => {
                        props.loading(false)
                        logout();
                    })
            })
            .catch(error => {
                setDeleteCompanyError(error.body.error)
                props.loading(false)
            })
    };

    const handlePartnerProgramSubscription = () => {
        const existingCompany: CompanyDTO = myCompaniesState.owner_of as CompanyDTO
        props.loading(true)
        userApi.subscribeToPartnerProgram(existingCompany.id)
            .then(subscribedCompany => {
                dispatch(setOwnerCompany({company: subscribedCompany}))
                dispatch(updateMemberCompany({updatedMemberCompany: subscribedCompany}))
                props.loading(false)
            })
            .catch(error => {
                setPremiumPartnerError(error.body.error)
                props.loading(false)
            })
    }

    return (
        <Container>

            {hasCompany && (
                <div>
                    <h1>Premium Partner</h1>
                    {premiumPartner ? (
                        <p>{partnerUntil}</p>
                    ) : (
                        <Button onClick={handlePartnerProgramSubscription}>Become a premium partner</Button>)
                    }
                    { premiumPartnerError != "" && (
                        <div className="text-danger">
                            {premiumPartnerError}
                        </div>
                    )}
                </div>
            )}

            <hr />

            <h1>Basis-Informationen</h1>
            <Form>
                <Form.Group>
                    <Form.Label>Firmenname</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyDetails.name}
                        onChange={(e) => setCompanyDetails({ ...companyDetails, name: e.target.value })}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Beschreibung</Form.Label>
                    <Form.Control
                        as="textarea"
                        value={companyDetails.description}
                        onChange={(e) => setCompanyDetails({ ...companyDetails, description: e.target.value })}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Tags</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyDetails.tags.join(', ')}
                        onChange={(e) => setCompanyDetails({ ...companyDetails, tags: e.target.value.split(', ') })}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Standort</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyDetails.location}
                        onChange={(e) => setCompanyDetails({ ...companyDetails, location: e.target.value })}
                    />
                </Form.Group>
                {hasCompany && (
                    <Button onClick={handleUpdateBasisInfo}>Update der Basis-Informationen</Button>
                )}
                { updateBasicCompanyDataFeedback != "" && (
                    <div className="text-success">
                        {updateBasicCompanyDataFeedback}
                    </div>
                )}
                { updateBasicCompanyDataError != "" && (
                    <div className="text-danger">
                        {updateBasicCompanyDataError}
                    </div>
                )}
            </Form>

            <hr />

            <h1>Firmen-Links</h1>
            <Form>
                <Form.Group>
                    <Form.Label>Webseiten-URL</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyLinks.website_url === undefined ? "" : companyLinks.website_url}
                        onChange={(e) => setCompanyLinks({ ...companyLinks, website_url: e.target.value })}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Social Media</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyLinks.social_media === undefined ? [] as string [] : (companyLinks.social_media as string []).join(', ')}
                        onChange={(e) => setCompanyLinks({ ...companyLinks, social_media: e.target.value.split(', ') })}
                    />
                </Form.Group>
                {hasCompany && (
                    <Button onClick={handleUpdateFirmenLinks}>Update der Firmen-Links</Button>
                )}
                { updateCompanyLinksFeedback != "" && (
                    <div className="text-success">
                        {updateCompanyLinksFeedback}
                    </div>
                )}
                { updateCompanyLinksError != "" && (
                    <div className="text-danger">
                        {updateCompanyLinksError}
                    </div>
                )}
                
                {!hasCompany && (
                    <Button onClick={handleCreateCompany}>Neue Firma erstellen</Button>
                )}
                { createCompanyFeedback != "" && (
                    <div className="text-success">
                        {createCompanyFeedback}
                    </div>
                )}
                { createCompanyError != "" && (
                    <div className="text-danger">
                        {createCompanyError}
                    </div>
                )}
            </Form>

            <hr />

            {hasCompany && (
            <div>
                <h1>HR</h1>
                <Container>
                    <h2>Member List</h2>
                    <ListGroup>
                        {/*members.length == 0 && (
                            <h5>No members in your company :(</h5>
                        )*/}
                        {members.members.map(userProfile => (
                            <ListGroup.Item key={userProfile.id}>
                                <Row>
                                    <Col md={4}>
                                        <strong>Member ID:</strong> {userProfile.id}
                                    </Col>
                                    <Col md={4}>
                                        <strong>First Name:</strong> {userProfile.first_name}
                                    </Col>
                                    <Col md={4}>
                                        <strong>Last Name:</strong> {userProfile.last_name}
                                    </Col>
                                    <Col md={12}>
                                        <strong>User Profile Email:</strong> {userProfile.user_profile_email}
                                    </Col>
                                </Row>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </Container>
                <Form>
                    <Form.Group>
                        <Form.Label>Invite new User</Form.Label>
                        <Form.Control
                            type="text"
                            value={newUserEmail}
                            onChange={(e) => setNewUserEmail(e.target.value)}
                        />
                    </Form.Group>
                    <Button onClick={handleInviteUser}>Invite new User</Button>
                    { inviteUserFeedback != "" && (
                        <div className="text-success">
                            {inviteUserFeedback}
                        </div>
                    )}
                    { inviteUserError != "" && (
                        <div className="text-danger">
                            {inviteUserError}
                        </div>
                    )}
                </Form>

                <hr />

                <h1>Delete Company</h1>
                <Button onClick={() => setShowDeleteModal(true)}>Delete Company</Button>

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Confirm Deletion</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>Are you sure you want to delete the company?</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                            Cancel
                        </Button>
                        <Button variant="danger" onClick={handleDeleteCompany}>
                            Delete
                        </Button>
                        { deleteCompanyFeedback != "" && (
                            <div className="text-success">
                                {deleteCompanyFeedback}
                            </div>
                        )}
                        { deleteCompanyError != "" && (
                            <div className="text-danger">
                                {deleteCompanyError}
                            </div>
                        )}
                    </Modal.Footer>
                </Modal>
            </div>
            )}
        </Container>
    );
};

export default MyCompanyView;
