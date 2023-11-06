import React, {useEffect} from "react";
import {Box, Button, Modal, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {useAppSelector} from "../state/hooks";
import CreateJobApplicationModal from "./CreateJobApplicationModal";
import JobOffer from "../model/JobOffer";
import {PublicCompanyProfileDTO} from "../../src/.generated/user-service";
import JobOfferDetailsCard from "./JobOfferDetailsCard";
import BackButton from "./BackButton";
import {careerApi, userApi} from "../api/apis";
import JobApplication from "../model/JobApplication";

interface JobOfferViewProps {
    jobOffer?: JobOffer
    companyId: string,
    jobOfferId: string,
    applyForJobOffer?: boolean
}

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
    height: "75vh",
    overflowY: "auto",
};

function JobOfferView(props: JobOfferViewProps) {


    const userState = useAppSelector(state => state.user)
    const accountState = useAppSelector(state => state.account)
    const jobApplicationInLocalStore = useAppSelector(state =>
        Object.values(state.myJobApplications.jobApplications).find(jobApplication => jobApplication.jobOffer.id == props.jobOfferId)
    )
    const navigate = useNavigate()
    const open = props.applyForJobOffer || false
    const handleOpen = () => {
        navigate("apply", { relative: "path", replace: true })
    };
    const handleClose = () => {
        navigate("..", { relative: "path", replace: true });
    }

    const [existingJobApplication, setExistingJobApplication] = React.useState<JobApplication | undefined>(jobApplicationInLocalStore);
    const [jobOffer, setJobOffer] = React.useState<null | JobOffer>(
        props.jobOffer && props.jobOffer.id == props.jobOfferId && props.jobOffer || null
    );
    const [company, setCompany] = React.useState<null | PublicCompanyProfileDTO>(null);

    const hasAlreadyApplied = jobApplicationInLocalStore || existingJobApplication

    useEffect(() => {
        if (props.jobOfferId && props.companyId) {
            careerApi.fetchJobOffer(props.companyId, props.jobOfferId).then(jobOffer => setJobOffer(jobOffer))
            if (!existingJobApplication && userState.user?.id) {
                careerApi.fetchJobApplicationForUserAndJobOffer(userState.user?.id, props.jobOfferId)
                    .then(result => setExistingJobApplication(result))
                    .catch(() => {})
            }
        }
    }, [props.jobOfferId]);

    useEffect(() => {
        if (props.companyId) {
            userApi.fetchPublicCompanyProfile(props.companyId).then(company => setCompany(company))
        }
    }, [props.companyId]);

    return (
        jobOffer && company &&
        <Box margin="10px" width="100%">
            <BackButton sx={{margin: "10px 0px"}}/>
            <JobOfferDetailsCard company={company} jobOffer={jobOffer}>
                <Box display="flex" flexDirection="row" gap="15px">
                    {accountState.loggedIn && userState.user?.associations.member_of.includes(jobOffer.companyId) &&
                        <Button variant="contained" onClick={() =>
                            navigate("/my-companies/job-offers/" + jobOffer.id + "?company=" + jobOffer.companyId)
                        }
                                disabled={open} color="primary">
                            Firmen-Seite
                        </Button>
                    }
                    {accountState.loggedIn && !hasAlreadyApplied &&
                        <Button variant="contained" onClick={handleOpen}
                                disabled={open} color="success">
                            Bewerben
                        </Button>
                    }
                    {accountState.loggedIn && hasAlreadyApplied &&
                        <Typography color={"success.main"}>
                            Sie haben sich auf diesen Job bereits beworben.
                        </Typography>
                    }
                </Box>
            </JobOfferDetailsCard>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box sx={style}>
                    <CreateJobApplicationModal companyId={props.companyId} jobOfferId={props.jobOfferId}
                                               onCloseButtonCallback={handleClose}/>
                </Box>
            </Modal>
        </Box>
    )

}


export default JobOfferView
