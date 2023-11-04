import React from "react";
import {Button, CircularProgress, TextField, Typography} from "@mui/material";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import DropdownButton from "./DropdownButton";
import {JobApplicationStatus} from "../model/JobApplication";
import {careerApi} from "../api/apis";
import {createJobApplication as createJobApplicationInLocalStore} from "../state/reducer/MyJobApplicationSlice";


interface JobApplicationViewerProps {
    companyId: string
    jobOfferId: string
    onCloseButtonCallback: () => void
}

function CreateJobApplicationModal(props: JobApplicationViewerProps) {

    const userId = useAppSelector(state => state.user.user?.id)
    const dispatch = useAppDispatch()

    const [jobApplicationContent, setJobApplicationContent] = React.useState<string | undefined>(undefined);
    const [loading, setLoading] = React.useState(false);
    const [success, setSuccess] = React.useState(false);
    const [error, setError] = React.useState<string | undefined>(undefined);

    function createJobApplication(status: JobApplicationStatus) {
        if (userId && jobApplicationContent) {
            setLoading(true)
            careerApi.createJobApplication(userId, props.companyId, props.jobOfferId, status, jobApplicationContent)
                .then(jobApplicationId => {
                    dispatch(createJobApplicationInLocalStore({
                        id: jobApplicationId,
                        userId: userId,
                        companyId: props.companyId,
                        jobOfferId: props.jobOfferId,
                        status: status,
                        content: jobApplicationContent
                    }))
                    setSuccess(true)
                })
                .catch(error => {
                    setError(error.toString())
                })
                .finally(() =>  setLoading(false))
        }
    }


    return (
        <div style={{display: "flex", flexDirection: "column", alignItems: "center", width: "80vw", minHeight: "100%"}}>
            <Typography variant="h5" sx={{alignSelf: "flex-start"}}>Erzählen Sie uns mehr:</Typography>
            <br/>
            <br/>
            <div style={{display: "flex", flexFlow: "column",  alignItems: "center", width: "95%"}}>
                <TextField multiline disabled={success} error={error != undefined} value={jobApplicationContent} onChange={(event) => {
                    setJobApplicationContent(event.target.value)
                    if (error) {
                        setError(undefined)
                    }
                }} sx={{width: "100%"}} minRows={5} placeholder={"Schreiben Sie hier einen tollen Bewerbungstext :)"}></TextField>
                <div style={{display: "flex", flexDirection: "row", alignItems: "center", alignSelf: "flex-end"}}>
                    {error && <Typography sx={{color: "red", marginRight: "20px"}}>{error}</Typography>}
                    {loading && <CircularProgress sx={{marginRight: "20px"}}/>}
                    {success && <Typography sx={{color: "green", marginRight: "20px"}}>Bewerbung wurde erfolgreich abgeschickt.</Typography>}
                    {success && <Button onClick={props.onCloseButtonCallback}  color="success" variant="contained" sx={{margin: "20px 0px", alignSelf: "flex-end"}}>
                        Schließen
                    </Button>}
                    {!success &&
                        <DropdownButton textWidth={"185px"} items={
                            [
                                {
                                    key: JobApplicationStatus.OPEN.name,
                                    value: JobApplicationStatus.OPEN,
                                    text: "Bewerbung abschicken"
                                },
                                {
                                    key: JobApplicationStatus.DRAFT.name,
                                    value: JobApplicationStatus.DRAFT,
                                    text: "Entwurf anlegen"
                                }
                            ]
                        } onClick={(value) => createJobApplication(value)} disabled={!jobApplicationContent || loading || error != undefined}/>
                    }
                </div>
            </div>
        </div>
    )

}


export default CreateJobApplicationModal
