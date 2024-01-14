import React, {useEffect, useState} from 'react';
import {Col, Container, Form, ListGroup, Modal, Row} from 'react-bootstrap';
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {CompanyDTO, PublicUserProfileDTO, UserDTO, UserIdDTO} from "../.generated/user-service";
import {userApi} from "../api/apis";
import {addMemberCompany, setOwnerCompany, updateMemberCompany} from "../state/reducer/MyCompaniesSlice";
import {updateUser} from "../state/reducer/UserSlice";
import {Box, Button, Card, CardContent, TextField, Typography} from '@mui/material';
import WarningIcon from '@mui/icons-material/Warning';

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
                setCreateCompanyFeedback("Firma erstellt. Sie werden abgemeldet.")
                new Promise(r => setTimeout(r, 1000 * 2))
                    .then(() => {
                        props.loading(false)
                        logout();
                    })
            })
            .catch(error => {
                setCreateCompanyError(error.details)
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
                setUpdateBasicCompanyDataFeedback("Firma-Informationen aktualisiert.")
            })
            .catch(error => {
                setUpdateBasicCompanyDataError(error.details)
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
                setUpdateCompanyLinksFeedback("Firmen-Links aktualisiert.")
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
                setInviteUserFeedback("Benutzer '" + newUserEmail + "' eingeladen.")
            })
            .catch(error => {
                setInviteUserError("Der Benutzer '" + newUserEmail + "' konnte nicht eingeladen werden: " + error.details)
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
                setDeleteCompanyFeedback("Firma wurde gelöscht. Sie werden abgemeldet.")
                new Promise(r => setTimeout(r, 1000 * 2))
                    .then(() => {
                        props.loading(false)
                        logout();
                    })
            })
            .catch(error => {
                setDeleteCompanyError(error.details)
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
                setPremiumPartnerError(error.details)
                props.loading(false)
            })
    }

    return (
        <Container>

            {hasCompany && (
                <div>
                    <Typography variant="h4" margin={"20px 0px"}>Premium Partner</Typography>
                    {premiumPartner && (
                        <Typography>Sie sind Partner bis {partnerUntil}</Typography>
                    ) || (
                        <Button variant="contained" onClick={handlePartnerProgramSubscription}>Become a premium partner</Button>)
                    }
                    { premiumPartnerError != "" && <Typography color={"error"}>{premiumPartnerError}</Typography>}
                </div>
            )}

            <hr />

            <Typography variant="h4">Basis-Informationen</Typography>
            <Box sx={{width: "100%", display: "flex", flexDirection: "column", gap: "20px", marginTop: "20px"}}>
                <TextField
                    type="text"
                    label="Firmenname"
                    value={companyDetails.name}
                    onChange={(e) => setCompanyDetails({ ...companyDetails, name: e.target.value })}
                />
                <TextField
                    multiline
                    type="text"
                    label="Beschreibung"
                    value={companyDetails.description}
                    onChange={(e) => setCompanyDetails({ ...companyDetails, description: e.target.value })}
                />
                <TextField
                    type="text"
                    label="Tags"
                    value={companyDetails.tags.join(', ')}
                    onChange={(e) => setCompanyDetails({ ...companyDetails, tags: e.target.value.split(', ') })}
                />
                <TextField
                    type="text"
                    label="Standort"
                    value={companyDetails.location}
                    onChange={(e) => setCompanyDetails({ ...companyDetails, location: e.target.value })}
                />
            </Box>

            <Form>
                <br/>
                {hasCompany && (
                    <Button variant="contained" onClick={handleUpdateBasisInfo}>Update der Basis-Informationen</Button>
                )}
                { updateBasicCompanyDataFeedback != "" && <Typography color="success">{updateBasicCompanyDataFeedback}</Typography>}
                { updateBasicCompanyDataError != "" && <Typography color="error">{updateBasicCompanyDataError}</Typography>}
            </Form>

            <hr />
            <Typography variant="h4" sx={{marginTop: "10px"}}>Firmen-Links</Typography>
            <Box sx={{width: "100%", display: "flex", flexDirection: "column", gap: "20px", marginTop: "20px"}}>
                <TextField
                    type="text"
                    label="Website Url"
                    value={companyLinks.website_url}
                    onChange={(e) => setCompanyLinks({ ...companyLinks, website_url: e.target.value })}
                />
                <TextField
                    type="text"
                    label="Social Media Links"
                    value={!companyLinks.social_media ? [] as string[] : (companyLinks.social_media as string[]).join(', ')}
                    onChange={(e) => setCompanyLinks({ ...companyLinks, social_media: e.target.value.split(', ') })}
                />
                {hasCompany && (
                    <Button variant="contained" sx={{width: "fit-content"}} onClick={handleUpdateFirmenLinks}>Update der Firmen-Links</Button>
                )}
                { updateCompanyLinksFeedback != "" && <Typography color="success">{updateCompanyLinksFeedback}</Typography>}
                { updateCompanyLinksError != "" && <Typography color="error">{updateCompanyLinksError}</Typography>}

                {!hasCompany && (
                    <Box marginTop="10px">
                        <Button variant="contained" sx={{width: "fit-content", marginRight: "20px"}} onClick={handleCreateCompany}>Neue Firma erstellen</Button>
                        <Typography color={"error"} sx={{display: "inline-block"}}>Sie werden durch diese Aktion abgemeldet.</Typography>
                    </Box>
                )}
                { createCompanyFeedback != "" && <Typography color="success">{createCompanyFeedback}</Typography>}
                { createCompanyError != "" && <Typography color="error">{createCompanyError}</Typography>}
            </Box>

            {hasCompany && (
            <div>
                <hr/>
                <Typography variant={"h4"}>Mitarbeiter</Typography>

                <Box>
                    { members.members.map(userProfile => (
                        <Card key={userProfile.id} sx={{margin: "10px 0px", borderRadius: 0, border: "1px solid", width: "80%"}} color="primary">
                            <CardContent sx={{padding: "10px", "&:last-child": {paddingBottom: "10px"}}}>
                                <Typography>
                                    <strong>Member ID:</strong> {userProfile.id}
                                </Typography>
                                <Typography>
                                    <strong>First Name:</strong> {userProfile.first_name}
                                </Typography>
                                <Typography>
                                    <strong>Last Name:</strong> {userProfile.last_name}
                                </Typography>
                                <Typography>
                                    <strong>User Profile Email:</strong> {userProfile.user_profile_email}
                                </Typography>
                            </CardContent>
                        </Card>
                    ))}
                </Box>

                <Box display="flex" gap="5%" alignItems="center" width="90%" marginTop="20px">
                    <TextField
                        size="small"
                        type="text"
                        label="Mitglied einladen"
                        value={newUserEmail}
                        sx={{width: "60%"}}
                        onChange={(e) => setNewUserEmail(e.target.value)}
                    />
                    <Button variant="contained" sx={{width: "25%"}} onClick={handleInviteUser}>Abschicken</Button>
                </Box>

                <Box width="90%" textAlign="center" marginTop="20px">
                    { inviteUserFeedback != "" && <Typography color="success">{inviteUserFeedback}</Typography>}
                    { inviteUserError != "" && <Typography color="error">{inviteUserError}</Typography>}
                </Box>

                <hr />

                <Typography variant="h4">Gefahrenbereich</Typography>
                <Button sx={{margin: "30px 0px"}} variant="contained" color="error" onClick={() => setShowDeleteModal(true)}>
                    <WarningIcon sx={{marginRight: "15px"}}/> Firma löschen
                </Button>

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            <Typography variant={"h5"}>Warnung</Typography>
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body><Typography sx={{margin: "5px 0px"}}>Wollen sie diese Firma wirklich löschen?</Typography></Modal.Body>
                    <Modal.Footer>
                        <Button sx={{marginRight: "20px"}} variant="contained" color="primary" onClick={() => setShowDeleteModal(false)}>
                            Zurück
                        </Button>
                        <Button variant="contained" color="error" onClick={handleDeleteCompany}>
                            Firma endgültig löschen
                        </Button>
                        { deleteCompanyFeedback != "" && <Typography color="success">{deleteCompanyFeedback}</Typography>}
                        { deleteCompanyError != "" && <Typography color="error">{deleteCompanyError}</Typography>}
                    </Modal.Footer>
                </Modal>
            </div>
            )}
        </Container>
    );
};

export default MyCompanyView;
