import React, {Fragment, useEffect} from "react";
import {Card, CardActionArea, CardContent, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import JobOffer from "../model/JobOffer";
import { CompanyDTO } from "../../src/.generated/user-service";
import {userApi} from "../api/apis";

interface JobOfferCardProps {
    jobOffer: JobOffer
}


function JobOfferCard(props: JobOfferCardProps) {

    const [company, setCompany] = React.useState<null | CompanyDTO>(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (props.jobOffer.companyId) {
            userApi.fetchCompany(props.jobOffer.companyId)
                .then(company => setCompany(company))
                // TODO: On error!
        }
    }, [props.jobOffer.companyId]);

    const goToJobOffer = () => {
        const url = `/companies/${props.jobOffer.companyId}/job-offers/${props.jobOffer.id}`
        navigate(url)
    }

    return (
        <Card sx={{margin: "10px 0px 10px 0px", borderRadius: 0, border: "1px solid", width: "80%"}} color="primary">
            <CardContent sx={{padding: "10px", "&:last-child": {paddingBottom: "10px"}}}>
                <CardActionArea onClick={goToJobOffer}>

                    <div style={{display: "flex", justifyContent: "space-between"}}>
                        <Typography variant="h5">{props.jobOffer.title}</Typography>
                        {company && <Typography>{company.details.name}</Typography>}
                    </div>
                    {props.jobOffer.description && <Fragment>
                        <br/>
                        <div style={{maxHeight: "60px"}}>
                            <Typography sx={{
                                overflow: "hidden",
                                lineHeight: "20px",
                                height: "60px"
                            }}>{props.jobOffer.description}</Typography>
                        </div>
                    </Fragment>}
                    <br/>
                    <div style={{display: "inline-block"}}>
                        {props.jobOffer.tags?.map(tag =>
                            <Typography display="inline" marginRight="10px" key={tag}>
                                {tag}
                            </Typography>
                        )}
                    </div>
                </CardActionArea>
            </CardContent>
        </Card>
    )

}


export default JobOfferCard
