import HeaderBar from "../component/HeaderBar";
import React, {Fragment, useState} from "react";
import JobOfferList from "../component/JobOfferList";
import GenericView from "../view/GenericView";


const HomeRoute = () => {
    return (
        <GenericView>
            <JobOfferList />
        </GenericView>
    )
}

export default HomeRoute
