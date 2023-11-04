import React, {useEffect, useState} from 'react';
import {Accordion, Button, Card, Container, Form} from 'react-bootstrap';
import {CompanyDTO, CompanyIdDTO, UserDTO} from "../.generated/user-service";
import {userApi} from "../api/apis";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import {addMemberCompany, removeInvite} from "../state/reducer/MyCompaniesSlice";
import {updateUser} from "../state/reducer/UserSlice";

interface CompanyInvitesViewProps {
    loading: ((is_loading: boolean) => void)
}

function CompanyInvitesView(props: CompanyInvitesViewProps) {
    const [companyInvites, setCompanyInvites] = useState({} as {[company_id: CompanyIdDTO]: CompanyDTO});
    const [selectedCompanies, setSelectedCompanies] = useState([] as CompanyDTO []);

    const navigate = useNavigate()
    const dispatch = useAppDispatch()

    const logout = useLogout();

    const userState = useAppSelector(state => state.user)
    const user = userState.user as UserDTO
    const myCompaniesState = useAppSelector(state => state.myNewCompanies)


    useEffect(() => {
        if (userState.user === undefined) {
            logout();
        }
        if (myCompaniesState.invited_to === undefined || Object.values(myCompaniesState).length == 0) {
            navigate("/my-companies");
        } else {
            setCompanyInvites(myCompaniesState.invited_to);
        }
    }, [userState.user, myCompaniesState.invited_to]);

    const handleCheckboxChange = (selectedCompany: CompanyDTO) => {
        if (selectedCompanies.includes(selectedCompany)) {
            setSelectedCompanies(selectedCompanies.filter(company => company.id !== selectedCompany.id));
        } else {
            setSelectedCompanies([...selectedCompanies, selectedCompany]);
        }
    };


    const processSelectedInvited = async () => {
        for (const selectedCompany of Object.values(selectedCompanies)) {
            props.loading(true)
            userApi.acceptCompanyInvite(user.id, selectedCompany.id)
                .then( () => {
                    dispatch(removeInvite({invitedToCompany: selectedCompany}));
                    dispatch(addMemberCompany({company: selectedCompany}));
                    const updatedCurrentUser = structuredClone((userState.user as UserDTO));
                    updatedCurrentUser.associations.member_of.push(selectedCompany.id)
                    updatedCurrentUser.associations.invited_to = updatedCurrentUser.associations.invited_to.filter(inviteId => inviteId != selectedCompany.id)
                    dispatch(updateUser({user: updatedCurrentUser}))
                    props.loading(false)
                })
                .catch(error => logout())
        }
    }

    const handleAcceptInvites = async () => {
        processSelectedInvited()
            .then( () => {
                if(Object.values(companyInvites).length === 0) {
                    navigate("/my-companies");
                }
            })
    }

    return (
        <Container>
            <h1>Company Invites</h1>
            <Form>
                {Object.values(companyInvites).map((invitedCompany: CompanyDTO) => (
                    <Card key={invitedCompany.id}>
                        <Card.Body>
                            <Form.Check
                                type="checkbox"
                                label={invitedCompany.details.name}
                                checked={selectedCompanies.includes(invitedCompany)}
                                onChange={() => handleCheckboxChange(invitedCompany)}
                            />
                        </Card.Body>
                    </Card>
                ))}
                <Button
                    variant="primary"
                    onClick={handleAcceptInvites}
                    disabled={Object.values(selectedCompanies).length === 0 || Object.values(companyInvites).length === 0}
                >
                    Accept Invites
                </Button>
            </Form>
        </Container>
    );
}
export default CompanyInvitesView;