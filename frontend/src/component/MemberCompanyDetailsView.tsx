import React, {useEffect, useState} from 'react';
import {Button, Container, Form} from 'react-bootstrap';
import {useAppSelector, useLogout} from "../state/hooks";
import {CompanyDTO} from "../.generated/user-service";
import {userApi} from "../api/apis";

interface MyCompanyViewProps {
    loading: ((is_loading: boolean) => void)
}

const MemberCompanyDetailsView = (props: MyCompanyViewProps) => {

    const userState = useAppSelector(state => state.user)
    const selectedMemberCompany = useAppSelector(state => state.selectedMemberCompany)
    const logout = useLogout()

    const [inviteUserError, setInviteUserError] = useState("")
    const [inviteUserFeedback, setInviteUserFeedback] = useState("")

    useEffect(() => {
        if(userState.user === undefined || selectedMemberCompany.selected_company === undefined) {
            logout()
        } else {
            setFields(selectedMemberCompany.selected_company)
        }
    }, [userState.user, selectedMemberCompany.selected_company]);

    const clearErrorsAndFeedback = () => {
        setInviteUserError("")
        setInviteUserFeedback("")
    }

    const setFields = (existingCompany: CompanyDTO) => {
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
        social_media: (undefined as undefined | string[]),
    });

    const [newUserEmail, setNewUserEmail] = useState('');

    const handleInviteUser = () => {
        clearErrorsAndFeedback()
        const selectedCompany: CompanyDTO = selectedMemberCompany.selected_company as CompanyDTO
        props.loading(true)
        userApi.inviteUserToCompany(
            selectedCompany.id,
            newUserEmail
        )
            .then( () => {
                setNewUserEmail("")
                props.loading(false)
                setInviteUserFeedback("User '" + newUserEmail + "' invited.")
            })
            .catch(error => {
                setInviteUserError("Error when inviting user '" + newUserEmail + "': " + error.details)
                props.loading(false)
            })
    };

    return (
        <Container>

            <h1>Premium Partner</h1>
            {premiumPartner && (
                <p>{partnerUntil}</p>
            )}

            <hr />

            <h1>Basis-Informationen</h1>
            <Form>
                <Form.Group>
                    <Form.Label>Firmenname</Form.Label>
                    <Form.Control
                        readOnly={true}
                        type="text"
                        value={companyDetails.name}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Beschreibung</Form.Label>
                    <Form.Control
                        readOnly={true}
                        as="textarea"
                        value={companyDetails.description}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Tags</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyDetails.tags.join(', ')}
                        readOnly={true}
                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Standort</Form.Label>
                    <Form.Control
                        type="text"
                        value={companyDetails.location}
                        readOnly={true}                    />
                </Form.Group>
            </Form>

            <hr />

            <h1>Firmen-Links</h1>
            <Form>
                <Form.Group>
                    <Form.Label>Webseiten-URL</Form.Label>
                    <Form.Control
                        type="text"
                        value={!companyLinks.website_url ? "" : companyLinks.website_url}
                        readOnly={true}                    />
                </Form.Group>
                <Form.Group>
                    <Form.Label>Social Media</Form.Label>
                    <Form.Control
                        type="text"
                        value={!companyLinks.social_media ? [] as string [] : (companyLinks.social_media as string []).join(', ')}
                        readOnly={true}                    />
                </Form.Group>
            </Form>

            <hr />

            <h1>HR</h1>
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


        </Container>
    );
};

export default MemberCompanyDetailsView;
