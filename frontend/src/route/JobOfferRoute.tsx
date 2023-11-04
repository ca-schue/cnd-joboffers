import React, {Fragment} from "react";
import HeaderBar from "../component/HeaderBar";
import JobOfferView from "../component/JobOfferView";
import {useParams} from "react-router-dom";
import GenericView from "../view/GenericView";
import JobOfferList from "../component/JobOfferList";

interface JobOfferRouteProps {
    applyForJobOffer?: boolean
}

const JobOfferRoute = (props: JobOfferRouteProps) => {

    const { companyId, jobOfferId } = useParams();

    if (!companyId || !jobOfferId) {
        return null
    }

    return (
        <GenericView>
            <JobOfferView companyId={companyId} jobOfferId={jobOfferId} applyForJobOffer={props.applyForJobOffer || false}/>
        </GenericView>
    )
}

export default JobOfferRoute
