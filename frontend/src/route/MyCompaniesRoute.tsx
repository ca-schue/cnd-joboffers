import React, {Fragment, useEffect, useState} from "react";
import {Box, Button, CircularProgress, Typography} from "@mui/material";
import {useAppDispatch, useAppSelector, useLogout} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import UserArea from "../component/UserArea";
import MyJobOffersView from "../component/MyJobOffersView";
import GenericView from "../view/GenericView";
import GenericButton from "../component/left-hand-menu/GenericButton";
import SideMenu from "../component/left-hand-menu/SideMenu";
import CompanySelector from "../component/my-companies/CompanySelector";
import {careerApi, userApi} from "../api/apis";
import {CompanyDTO, CompanyIdDTO, UserDTO} from "../.generated/user-service";
import {CompanyId} from "../.generated/user-service/models/CompanyId";
import {
    addInvitedToCompany,
    addMemberCompany,
    clearMyCompanies,
    setOwnerCompany, updateMemberCompany
} from "../state/reducer/MyCompaniesSlice";
import MyAccountView from "../component/MyAccountView";
import MyJobApplicationView from "../component/MyJobApplicationView";
import EditJobApplication from "../component/my-job-applications/EditJobApplication";
import MyCompanyView from "../component/MyCompanyView";
import JobOfferView from "../component/JobOfferView";
import {overwriteJobOffers, setSelectedCompany} from "../state/reducer/SelectedMemberCompanySlice";
import CompanyInvitesView from "../component/CompanyInvitesView";
import MemberCompanyDetailsView from "../component/MemberCompanyDetailsView";

interface MyCompanyRouteProps {
    view: 'company' | 'company-invites' |'member-company-details' | 'job-offers' | 'edit-job-offer' | 'create-job-offer' | 'manage-companies' | 'job-offer-details'
}

const MyCompanyRoute = (props: MyCompanyRouteProps) => {

    const navigate = useNavigate()
    const dispatch = useAppDispatch()

    const [members, setMembers] = useState([] as CompanyDTO [])

    const userState = useAppSelector(state => state.user)
    const myCompaniesState = useAppSelector(state => state.myNewCompanies)
    const myCompaniesMemberOfState = useAppSelector(state => state.myNewCompanies.member_of)
    const myCompaniesInvitedToState = useAppSelector(state => state.myNewCompanies.invited_to)
    const selectedMemberCompanyState = useAppSelector(state => state.selectedMemberCompany)

    const [loading, setLoading] = React.useState(true);

    const logout = useLogout()

    const updateMemberCompanies = async () => {
        (userState.user as UserDTO).associations.member_of
            .forEach(companyId => {
                userApi.fetchCompany(companyId)
                    .then(company => {
                        dispatch(addMemberCompany({company: company}))
                    })
            })
    }

    const updateInvitedToCompanies = async () => {
        (userState.user as UserDTO).associations.invited_to
            .forEach(companyId => {
                userApi.fetchCompany(companyId)
                    .then(company => {
                        dispatch(addInvitedToCompany({company: company}))
                    })
            })
    }

    const updateOwnerCompany = async () => {
        // TODO
    }

    const changeSelectedMemberCompany = (companyId: CompanyIdDTO) => {
        setLoading(true)
        userApi.fetchCompany(companyId)
            .then(fetchedCompany => {
                dispatch(updateMemberCompany({updatedMemberCompany: fetchedCompany}))
                dispatch(setSelectedCompany({ company: fetchedCompany }));
                routeTo("/my-companies/member-company-details");
                setLoading(false)
            })
    }

    const initSelectedCompany = () => {
        changeSelectedMemberCompany(Object.values(myCompaniesState.member_of)[0].id)
    }

    const updateMyCompanies = async () => {
        dispatch(clearMyCompanies());
        let memberPromises: Promise<void> [] = [];
        (userState.user as UserDTO).associations.member_of
            .forEach(companyId => {
                memberPromises.push(
                    userApi.fetchCompany(companyId)
                        .then(company => {
                            dispatch(addMemberCompany({company: company}))
                            members.push(company)
                        })
                        .catch(error => {
                            console.log(error)
                            // Skip company if not exists, TODO: delete company from member association
                        })
                )
            })
        const firstMember = memberPromises[0]
        await Promise.all(memberPromises)
            .then(() => {
                const loadedMembers = members
                if(loadedMembers.length > 0) {
                    changeSelectedMemberCompany(members[0].id)
                }
            })

        let invitePromises: Promise<void> [] = [];
        (userState.user as UserDTO).associations.invited_to
            .forEach(companyId => {
                invitePromises.push(
                    userApi.fetchCompany(companyId)
                        .then(company => {
                            dispatch(addInvitedToCompany({company: company}))
                        })
                        .catch(error => {
                            console.log(error)
                            // Skip company if not exists, TODO: delete company from member association
                        })
                )
            })
        await Promise.all(invitePromises);

        if((userState.user as UserDTO).associations.owner_of !== undefined) {
            const ownerOfId: CompanyIdDTO = (userState.user as UserDTO).associations.owner_of as CompanyIdDTO
            await userApi.fetchCompany(ownerOfId)
                .then(ownerOfCompany => {
                    dispatch(setOwnerCompany({company: ownerOfCompany}))
                })
                .catch(error => {
                    console.log(error)
                    // Skip company if not exists, TODO: delete company from member association
                })
        }
    }

    useEffect(() => {

    }, [myCompaniesState.owner_of?.details.name]);

    useEffect(() => {
        if (userState.user === undefined) {
            logout()
        } else {
            updateMyCompanies()
                .then( () => {
                    if (loading) {
                        setLoading(false)
                    }
                })
        }
    },[userState.user?.associations])

    function routeTo(url: string) {
        navigate(url)
    }

    const handleJobOffersClicked = async () => {
        setLoading(true)
        careerApi.fetchAllJobOffersForCompany((selectedMemberCompanyState.selected_company as CompanyDTO).id)
            .then(jobOffers => {
                dispatch(overwriteJobOffers({ jobOffers: jobOffers }));
                setLoading(false)
                routeTo("/my-companies/job-offers");
            })
            .catch(jobOfferFetchError => {
                console.log(jobOfferFetchError)
                // TODO
                setLoading(false)
            })
    }

    const callbackLoading = (is_loading: boolean): void => {
        setLoading(is_loading)
    }

    const componentToUse = () => {
        switch(props.view) {
            case "company": return(<MyCompanyView loading={callbackLoading}/>)
            case "company-invites": return(<CompanyInvitesView loading={callbackLoading}/>)
            case "member-company-details": return(<MemberCompanyDetailsView loading={callbackLoading}/>)
            case "job-offers":
            case "edit-job-offer":
            case "create-job-offer":
            case "job-offer-details": {
                    const selectedCompany: CompanyDTO = selectedMemberCompanyState.selected_company as CompanyDTO
                    return (<MyJobOffersView
                        selected_company={selectedCompany}
                        edit={props.view == "edit-job-offer"}
                        create={props.view == "create-job-offer"}
                        details={props.view == "job-offer-details"}
                        loading={callbackLoading}
                    />)
            }
        }
    }

    return (
        <UserArea>
            <GenericView>
                {loading &&
                    <Box width="100%" height="100%" bgcolor="#484848ab" position="absolute" zIndex="10" display="flex" justifyContent="center" alignItems="center">
                        <CircularProgress color="primary" size={"5rem"} sx={{marginTop: "-5%"}}/>
                    </Box>
                }

                <SideMenu>
                    <GenericButton text="My Company" onClick={() => routeTo("/my-companies")} selected={props.view == "company"} />
                    <GenericButton text="Company Invites" onClick={() => routeTo("/my-companies/invites")} selected={props.view == "company-invites"} disabled={Object.keys(myCompaniesState.invited_to).length == 0} />
                    {Object.keys(myCompaniesState.member_of).length > 0 &&
                        <CompanySelector onChange={changeSelectedMemberCompany} companies={myCompaniesState.member_of}/>
                    }
                    {Object.keys(myCompaniesState.member_of).length > 0 &&
                        <GenericButton
                            text="Company Details"
                            onClick={() => routeTo("/my-companies/member-company-details")}
                            selected={props.view == "member-company-details"}
                            disabled={selectedMemberCompanyState.selected_company === undefined}
                        />
                    }
                    {Object.keys(myCompaniesState.member_of).length > 0 &&
                        <GenericButton
                            text="Job Offers"
                            onClick={handleJobOffersClicked}
                            selected={props.view == "job-offers"}
                            disabled={selectedMemberCompanyState.selected_company === undefined}
                        />
                    }
                </SideMenu>

                <Box width="100%" height="100%" display="flex" flexDirection="column">
                    {componentToUse()}
                </Box>


                {/* <Box maxWidth="100%" height="100%" width="100%"
                     display="flex" flexDirection="column" alignContent="center" textAlign="center">

                    {(!selectedCompany || props.view == "manage-companies") &&
                        <Box>
                            {memberOf.map(member => <Typography>You are member of {member}</Typography>)}
                            {invitedTo.map(invited => <Typography>You are invited to join {invited}</Typography>)}
                        </Box>
                    }

                    {selectedCompany && props.view == "company" &&
                        <Fragment>
                            <br/>
                            <Typography variant="h4">{selectedCompany.details.name}</Typography>
                            <br/>
                            <hr/>
                            <br/>
                            <Typography>{selectedCompany.details.description}</Typography>
                            <Button variant="contained" sx={{width: "100px"}}>
                                Update
                            </Button>
                        </Fragment>
                    }

                    {(props.view == "job-offers" || props.view == "edit-job-offer" || props.view == "create-job-offer" || props.view == "job-offer-details") && selectedCompany &&
                        <MyJobOffersView company={selectedCompany} edit={props.view == "edit-job-offer"} create={props.view == "create-job-offer"} details={props.view == "job-offer-details"} />
                    }
                </Box>
                */}
            </GenericView>
        </UserArea>

    )
}

export default MyCompanyRoute