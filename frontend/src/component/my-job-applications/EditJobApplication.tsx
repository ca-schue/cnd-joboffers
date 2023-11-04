import {useNavigate, useParams} from "react-router-dom";
import React, {Fragment, useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "../../state/hooks";
import {updateJobApplication, updateJobApplicationAndIncrementVersion} from "../../state/reducer/MyJobApplicationSlice";
import {Box, Button, Card, CardContent, CircularProgress, Fade, TextField, Tooltip, Typography} from "@mui/material";
import BackButton from "../BackButton";
import DownloadDoneIcon from "@mui/icons-material/DownloadDone";
import JobOffer from "../../model/JobOffer";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import {careerApi} from "../../api/apis";

const EditJobApplication = () => {

    const { jobApplicationId} = useParams()
    const userId = useAppSelector(state => state.user.user?.id)
    const dispatch = useAppDispatch()
    const jobApplication = useAppSelector(state => jobApplicationId && state.myJobApplications.jobApplications[jobApplicationId] || undefined)

    const [jobOffer, setJobOffer] = useState<JobOffer | undefined>(undefined);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState<'saving' | 'saved' | 'unchanged' | 'changed'>("unchanged");
    const [isExpanded, setIsExpanded] = useState(false);

    const navigate = useNavigate()

    const [content, setContent] = React.useState(jobApplication?.content || "");

    const jobOfferTitle = jobOffer?.title || jobApplication?.jobOffer?.title || ""

    useEffect(() => {
        if (userId && jobApplicationId) {
            careerApi.fetchJobApplication(userId, jobApplicationId).then(jobApplication => {
                dispatch(updateJobApplication({jobApplication: jobApplication}))
                setLoading(false)
            })
        }
    }, [jobApplicationId]);

    useEffect(() => {
        if (jobApplication) {
            careerApi.fetchJobOffer(jobApplication.company.id, jobApplication.jobOffer.id).then(jobOffer => {
                setJobOffer(jobOffer)
            })
        }
    }, [jobApplication?.jobOffer?.id]);


    useEffect(() => {
        if (!loading && !jobApplication) {
            navigate("/profile/job-applications")
        }
    }, [loading, jobApplication]);

    const saveJobApplication = () => {
        if (jobApplication) {
            setSaving("saving")
            careerApi.updateJobApplication(jobApplication, content).then(() => {
                dispatch(updateJobApplicationAndIncrementVersion({jobApplication: {
                    ...jobApplication,
                        content: content
                }}))
                setSaving("saved")
            })
        }
    }

    const toggleExpansion = () => {
        setIsExpanded(!isExpanded)
    }

    return(
        <Box margin="50px 2%" boxSizing="border-box" display="flex" flexDirection="column" gap={"15px"}>
            {loading && <CircularProgress sx={{margin: "auto"}}/>}
            {!loading && jobApplication &&
                <Fragment>
                    <Box sx={{display: "flex", width: "90%"}}>
                        <BackButton sx={{margin: "10px 0px 30px 0px"}} />
                    </Box>
                    <Typography variant="h5" textAlign="left">Bewerbung bearbeiten:</Typography>
                    <Card sx={{width: "90%", borderRadius: "0px", border: "1px solid black"}}>
                        <CardContent sx={{"&:last-child": {paddingBottom: "15px"}}}>
                            <Typography color="primary.light" display="inline" fontSize="1.2rem">{jobOfferTitle}</Typography>
                            {jobOffer &&
                                <Fragment>
                                    {isExpanded && <br/>}
                                    <Typography sx={{ overflow: "hidden", ...(!isExpanded && {height: "0px"}) }}>
                                        {jobOffer.description}
                                    </Typography>
                                    <Box display="flex" justifyContent="center" padding={"0px"} width="100%" onClick={toggleExpansion} sx={{ cursor: 'pointer' }}>
                                        {!isExpanded && <Tooltip title={"Beschreibung anzeigen"}>
                                            <ExpandMoreIcon />
                                        </Tooltip>}
                                        {isExpanded && <ExpandLessIcon />}
                                    </Box>
                                </Fragment>
                            }
                        </CardContent>
                    </Card>
                    <br />
                    <TextField multiline sx={{width: "90%"}} label={"Bewerbungstext"} disabled={saving == "saving"} value={content} onChange={(event) => {
                        setContent(event.target.value)
                        if (saving != "saving" && saving != "changed") {
                            setSaving("changed")
                        }
                    }} />
                    <Box width="100%" display="flex" flexDirection="row" marginTop="10px" alignItems="center" gap="20px">
                        <Button variant="contained" color="primary" onClick={saveJobApplication} disabled={saving == "saving" || saving == "saved" || saving == "unchanged"}>Speichern</Button>
                        {saving == "saving" && <CircularProgress />}
                        {saving == "saved" && <Fade in={true}><DownloadDoneIcon sx={{color: "green"}} /></Fade>}
                    </Box>
                </Fragment>
            }
        </Box>
    )

}

export default EditJobApplication
