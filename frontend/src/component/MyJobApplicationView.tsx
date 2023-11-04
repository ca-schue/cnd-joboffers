import React, {Fragment, useEffect} from "react";
import {Box, Button, CircularProgress, Tooltip, Typography} from "@mui/material";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {useNavigate} from "react-router-dom";
import {deleteJobApplication, publishJobApplication, setJobApplications} from "../state/reducer/MyJobApplicationSlice";
import JobApplication, {JobApplicationStatus} from "../model/JobApplication";
import JobApplicationCard from "./my-companies/job-application/JobApplicationCard";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SendIcon from '@mui/icons-material/Send';
import JobApplicationFilterList from "./my-companies/job-application/JobApplicationFilterList";
import {careerApi} from "../api/apis";

const MyJobApplicationView = () => {


    const navigate = useNavigate()
    const appDispatch = useAppDispatch()

    const userId = useAppSelector(state => state.user.user?.id)
    const jobApplications = Object.values(useAppSelector(state => state.myJobApplications.jobApplications)) || []
    const sortedJobApplications = jobApplications.sort((a, b) => {
        return calculateStatusValue(b.status) - calculateStatusValue(a.status)
    })
    const [loadingJobApplications, setLoadingJobApplications] = React.useState(sortedJobApplications.length == 0);

    function calculateStatusValue(status: JobApplicationStatus) {
        switch (status) {
            case JobApplicationStatus.ACCEPTED: return 100
            case JobApplicationStatus.DRAFT: return 90
            case JobApplicationStatus.OPEN: return 50
            case JobApplicationStatus.DENIED: return 10
            default: return 1
        }
    }

    useEffect(() => {
        if (userId) {
            careerApi.fetchUsersJobApplications(userId).then(jobApplications => {
                appDispatch(setJobApplications({jobApplications: jobApplications!}))
                if (loadingJobApplications) {
                    setLoadingJobApplications(false)
                }
            })
        } else {
            navigate("/")
        }
    }, [userId])


    const DeleteButton = (props: {jobApplication: JobApplication}) => {

        const dispatch = useAppDispatch()

        const removeJobApplication = () => {
            careerApi.deleteJobApplication(props.jobApplication).then(() => {
                dispatch(deleteJobApplication({jobApplicationId: props.jobApplication.id}))
            })
        }

        return (
            <Tooltip title={"Löschen"}>
                <Button variant="contained" color="error" onClick={removeJobApplication} ><DeleteIcon /></Button>
            </Tooltip>
        )
    }

    const EditButton =  (props: {jobApplication: JobApplication}) => {

        const navigate = useNavigate()

        const edit = () => {
            navigate(props.jobApplication.id + "/edit")
        }

        return (
            <Tooltip title={"Bearbeiten"}>
                <Button variant="contained" color="primary" onClick={edit}><EditIcon /></Button>
            </Tooltip>
        )
    }

    const SendButton =  (props: {jobApplication: JobApplication}) => {

        const dispatch = useAppDispatch()

        const send = () => {
            careerApi.publishJobApplication(props.jobApplication).then(() => {
                dispatch(publishJobApplication({jobApplicationId: props.jobApplication.id}))
            })
        }

        return (
            <Tooltip title={"Absenden"}>
                <Button variant="contained" color="success" onClick={send}><SendIcon /></Button>
            </Tooltip>
        )
    }

    return (
        <Box width="100%" height="100%" display="flex" flexDirection="column" padding="20px" boxSizing="border-box">
            {loadingJobApplications && <CircularProgress sx={{alignSelf: "center", marginTop: "60%"}}/>}
            {!loadingJobApplications && sortedJobApplications.length == 0 &&
                <Typography margin="50% auto 0 auto">
                    Du hast keine Bewerbungen bis jetzt! Schreib ein paar :)
                </Typography>
            }

            {!loadingJobApplications && sortedJobApplications.length > 0 &&
                <JobApplicationFilterList
                    jobApplications={jobApplications}
                    filterableStatus={[JobApplicationStatus.DRAFT, JobApplicationStatus.OPEN, JobApplicationStatus.ACCEPTED, JobApplicationStatus.DENIED]}
                    sortFunction={(a, b) => calculateStatusValue(b.status) - calculateStatusValue(a.status)}
                    initialSelectedStatus={[JobApplicationStatus.DRAFT, JobApplicationStatus.OPEN, JobApplicationStatus.ACCEPTED]}
                    transformFunction={(jobApplication) => {
                        return (
                            <JobApplicationCard jobApplication={jobApplication} key={jobApplication.id} displayJobOfferTitle>
                                {jobApplication.status == JobApplicationStatus.OPEN &&
                                    <Fragment>
                                        <DeleteButton jobApplication={jobApplication} />
                                        <EditButton jobApplication={jobApplication} />
                                    </Fragment>
                                }
                                {jobApplication.status == JobApplicationStatus.DRAFT &&
                                    <Fragment>
                                        <DeleteButton jobApplication={jobApplication} />
                                        <SendButton jobApplication={jobApplication} />
                                        <EditButton jobApplication={jobApplication} />
                                    </Fragment>
                                }
                                {(jobApplication.status == JobApplicationStatus.ACCEPTED || jobApplication.status == JobApplicationStatus.DENIED) &&
                                    <DeleteButton jobApplication={jobApplication} />
                                }
                            </JobApplicationCard>
                        )
                    }}
                />
            }
        </Box>

    )


    /*
    return (
        <Box width="100%" height="100%" display="flex" flexDirection="column">
            {loadingJobApplications && <CircularProgress sx={{alignSelf: "center", marginTop: "60%"}}/>}
            {!loadingJobApplications && jobApplications &&
                <Box>
                    {Object.values(jobApplications).map(jobApplication =>
                        <Card key={jobApplication.id} sx={{margin: "20px"}}>
                            <CardContent>
                                <Typography>{jobApplication.content}</Typography>
                                <Button color="error" variant="contained" onClick={() => deleteJobApplication(jobApplication)}>Löschen</Button>
                                <Button variant="contained">Bearbeiten</Button>
                            </CardContent>
                        </Card>
                    )}
                </Box>
            }
        </Box>
    )*/

}


export default MyJobApplicationView
