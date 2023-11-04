import JobOffer, {JobOfferStatus} from "../model/JobOffer";
import {Box, Button, Typography, useTheme} from "@mui/material";
import React, {Fragment, useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import JobOfferDetailsCard from "./JobOfferDetailsCard";
import { CompanyDTO } from "../../src/.generated/user-service";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {
    closeJobOffer as closeJobOfferFromState,
    deleteJobOffer as deleteJobOfferFromState,
    setJobApplicationsForJobOffer,
    updateJobApplication
} from "../state/reducer/SelectedMemberCompanySlice";
import BackButton from "./BackButton";
import JobApplication, {JobApplicationStatus} from "../model/JobApplication";
import JobApplicationFilterList from "./my-companies/job-application/JobApplicationFilterList";
import JobApplicationCard from "./my-companies/job-application/JobApplicationCard";
import {careerApi} from "../api/apis";

interface MyJobOfferDetailsProps {
    current_jobOffer: JobOffer
    selected_company: CompanyDTO
    loading: ((is_loading: boolean) => void)
}


const MyJobOfferDetails = (props: MyJobOfferDetailsProps) => {

    const navigate = useNavigate()
    const dispatch = useAppDispatch()
    const theme = useTheme()

    const currentJobOffer = props.current_jobOffer
    const company = props.selected_company
    const selectedMemberCompanyState = useAppSelector(state => state.selectedMemberCompany)
    const jobApplicationsForCurrentJobOffer = useAppSelector(state => state.selectedMemberCompany.jobApplications[currentJobOffer.id])
    const jobApplicationsForCurrentJobOfferList = jobApplicationsForCurrentJobOffer && Object.values(jobApplicationsForCurrentJobOffer) || []

    const {hash} = useLocation()
    const anchoredJobApplicationId = hash.replace("#", "")
    const anchoredJobApplication = anchoredJobApplicationId && jobApplicationsForCurrentJobOffer[anchoredJobApplicationId] || undefined
    const initalillySelectedStatus = anchoredJobApplication && [anchoredJobApplication.status] || [JobApplicationStatus.OPEN]

    const closeJobOffer = () => {
        props.loading(true)
        careerApi.closeJobOffer(currentJobOffer.companyId, currentJobOffer.id, currentJobOffer.version).then(() => {
            dispatch(closeJobOfferFromState({jobOfferId: currentJobOffer.id}))
            props.loading(false)
        })
    }

    const deleteJobOffer = () => {
        props.loading(true)
        careerApi.deleteJobOffer(currentJobOffer.companyId, currentJobOffer.id, currentJobOffer.version).then(() => {
            dispatch(deleteJobOfferFromState({jobOffer: currentJobOffer}))
            props.loading(false)
            navigate("/my-companies/job-offers")
        })
    }

    useEffect(() => {
        props.loading(true)
        careerApi.fetchAllPublicJobApplicationsByJobOffer(currentJobOffer.companyId, currentJobOffer.id).then(jobApplications => {
                dispatch(setJobApplicationsForJobOffer({jobOffer: currentJobOffer, jobApplications: jobApplications}))
                props.loading(false)
            }
        )
    }, [currentJobOffer.id]);

    const edit = () => {
        navigate("edit")
    }

    const acceptJobApplication = (jobApplication: JobApplication) => {
        props.loading(true)
        careerApi.acceptJobApplication(jobApplication.company.id, jobApplication.jobOffer.id, jobApplication.id, jobApplication.version)
            .then(() => {
                dispatch(updateJobApplication({jobApplication: {...jobApplication, status: JobApplicationStatus.ACCEPTED}}))
                props.loading(false)
            })
    }

    const denyJobApplication = (jobApplication: JobApplication) => {
        props.loading(true)
        careerApi.denyJobApplication(jobApplication.company.id, jobApplication.jobOffer.id, jobApplication.id, jobApplication.version)
            .then(() => {
                dispatch(updateJobApplication({jobApplication: {...jobApplication, status: JobApplicationStatus.DENIED}}))
                props.loading(false)
            })
    }

    return (
        <Fragment>
            <BackButton/>

            <JobOfferDetailsCard jobOffer={currentJobOffer} company={company}>
                {currentJobOffer.status == JobOfferStatus.DRAFT &&
                    <Button variant="contained" color="success" sx={{marginLeft: "auto", marginRight: "20px"}} onClick={deleteJobOffer}>
                        Veröffentlichen
                    </Button>
                }
                {currentJobOffer.status == JobOfferStatus.OPEN &&
                    <Button variant="contained" color="warning" sx={{marginLeft: "auto", marginRight: "20px"}} onClick={closeJobOffer}>
                        Schließen
                    </Button>
                }
                {currentJobOffer.status == JobOfferStatus.CLOSED &&
                    <Button variant="contained" color="error" sx={{marginLeft: "auto", marginRight: "20px"}} onClick={deleteJobOffer}>
                        Löschen
                    </Button>
                }
                <Button variant="outlined" onClick={edit}>Bearbeiten</Button>
            </JobOfferDetailsCard>

            {jobApplicationsForCurrentJobOfferList.length == 0 &&
                <Fragment>
                    <Typography marginTop="50px">Für dieses Job-Angebot gibt es aktuell keine Bewerbungen</Typography>
                </Fragment>
            }

            {jobApplicationsForCurrentJobOfferList.length > 0 &&
                <Fragment>
                    <br/>
                    <Box width="100%" border={`1px solid ${theme.palette.primary.dark}`} sx={{borderWidth: "2px 0px"}} padding={"5px 0px"}>
                        <Typography paddingLeft={"5px"} textAlign="left" variant="h5" color={theme.palette.primary.main}>Bewerbungen:</Typography>
                    </Box>

                    <JobApplicationFilterList jobApplications={Object.values(jobApplicationsForCurrentJobOffer)} filterableStatus={[JobApplicationStatus.OPEN, JobApplicationStatus.DENIED, JobApplicationStatus.ACCEPTED]} initialSelectedStatus={initalillySelectedStatus} sortFunction={(a, b) => {
                        if (a.status == b.status) {
                            return 0
                        }
                        if (a.status == JobApplicationStatus.OPEN) {
                            return -1
                        }
                        if (a.status == JobApplicationStatus.ACCEPTED && b.status == JobApplicationStatus.DENIED) {
                            return -1
                        } else if (a.status == JobApplicationStatus.DENIED && b.status == JobApplicationStatus.ACCEPTED) {
                            return 1
                        }
                        return 1
                    }}
                                              transformFunction={(jobApplication) => {
                                                  return(
                                                      <JobApplicationCard key={jobApplication.id} jobApplication={jobApplication} selected={jobApplication.id == anchoredJobApplicationId} displayLink displayName>
                                                          {jobApplication.status == JobApplicationStatus.OPEN &&
                                                              <Fragment>
                                                                  <Button variant="contained" color="success" onClick={() => acceptJobApplication(jobApplication)}>Annehmen</Button>
                                                                  <Button variant="contained" color="error" onClick={() => denyJobApplication(jobApplication)}>Ablehnen</Button>
                                                              </Fragment>
                                                          }
                                                      </JobApplicationCard>
                                                  )
                                              }}
                    />
                </Fragment>
            }

        </Fragment>

    )

}

export default MyJobOfferDetails
