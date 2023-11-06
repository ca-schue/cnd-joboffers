import React, {Fragment, PropsWithChildren} from "react";
import {Card, CardContent, Typography} from "@mui/material";
import JobOffer from "../model/JobOffer";
import {CompanyDTO, PublicCompanyProfileDTO} from "../../src/.generated/user-service";

interface JobOfferDetailsCardProps {
    jobOffer: JobOffer
    company: CompanyDTO | PublicCompanyProfileDTO
}

const JobOfferDetailsCard = (props: PropsWithChildren<JobOfferDetailsCardProps>) => {

    const {jobOffer, company} = props


    return(
        <Card sx={{borderRadius: 0, border: "1px solid"}} color="primary">
            <CardContent sx={{padding: "10px", "&:last-child": {paddingBottom: "10px"}}}>
                <div style={{display: "flex", justifyContent: "space-between"}}>
                    <Typography variant="h5">{jobOffer.title}</Typography>
                    {company && <Typography>{company.details.name}</Typography>}
                </div>
                {jobOffer.description && <Fragment>
                    <br />
                    <Typography sx={{lineHeight: "20px", textAlign: "justify"}}>{jobOffer.description}</Typography>
                </Fragment>}
                <br />
                <div style={{display: "flex", width: "100%", alignItems: "center"}}>
                    {jobOffer.tags.map(tag =>
                        <Typography marginRight="10px" key={tag}>
                            {tag}
                        </Typography>
                    )}
                    <div style={{marginLeft: "auto"}}>

                    </div>
                    {props.children}
                </div>
            </CardContent>
        </Card>
    )

}

export default JobOfferDetailsCard
