import React, {Fragment} from "react";
import {Button, Card, CardContent, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import JobOffer from "../model/JobOffer";
import {careerApi} from "../api/apis";
import {CompanyDTO} from "../.generated/user-service";
import {overwriteJobOffers, setJobApplicationsForJobOffer} from "../state/reducer/SelectedMemberCompanySlice";
import {useAppDispatch} from "../state/hooks";

interface JobOfferViewProps {
    current_job_offer: JobOffer,
    selected_company: CompanyDTO
    loading: ((is_loading: boolean) => void)
}




function JobOfferView(props: JobOfferViewProps) {

    const dispatch = useAppDispatch()
    const jobOffer = props.current_job_offer
    const navigate = useNavigate()

    const handleJobOfferDetailsClicked = () => {
        props.loading(true)
        careerApi.fetchAllPublicJobApplicationsByJobOffer(props.selected_company.id, jobOffer.id)
            .then(jobApplications => {
                dispatch(setJobApplicationsForJobOffer({jobOffer: props.current_job_offer, jobApplications: jobApplications}));
                props.loading(false)
                navigate(`${jobOffer.id}`)
            })
            .catch(jobOfferFetchError => {
                // TODO
                props.loading(false)
            })
    }

    return (
        <Card sx={{borderRadius: 0, border: "1px solid"}} color="primary">
            <CardContent sx={{padding: "10px", "&:last-child": {paddingBottom: "10px"}}}>
                <Fragment>
                    <Typography variant="h5">{jobOffer.title}</Typography>
                    <br />
                    <br />
                    <Typography sx={{lineHeight: "20px", textAlign: "justify", padding: "10px 20px"}}>{jobOffer.description}</Typography>
                    <br />
                    <br />
                    <div style={{display: "flex", width: "100%", alignItems: "center"}}>
                        {jobOffer.tags?.map(tag =>
                            <Typography marginRight="10px" key={tag} variant="caption">
                                {tag}
                            </Typography>
                        )}
                        <Button variant="contained" onClick={handleJobOfferDetailsClicked} sx={{marginLeft: "auto"}}>
                            Details
                        </Button>
                    </div>
                </Fragment>
            </CardContent>
        </Card>
    )

}


export default JobOfferView
