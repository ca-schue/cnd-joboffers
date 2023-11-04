import React, {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {overwriteJobOffers} from "../state/reducer/SelectedMemberCompanySlice";
import {Box, Button, Typography} from "@mui/material";
import MyJobOffer from "./MyJobOffer";
import {useNavigate, useParams} from "react-router-dom";
import EditJobOffer from "./EditJobOffer";
import MyJobOfferDetails from "./MyJobOfferDetails";
import { CompanyDTO } from "../../src/.generated/user-service";
import {careerApi} from "../api/apis";

interface MyJobOfferViewProps {
    selected_company: CompanyDTO
    edit: boolean
    create: boolean
    details: boolean
    loading: ((is_loading: boolean) => void)
}

const MyJobOffersView = (props: MyJobOfferViewProps) => {


    const navigate = useNavigate()
    const dispatch = useAppDispatch()
    const { jobOfferId } = useParams()

    const jobOffers = useAppSelector(state => state.selectedMemberCompany.jobOffers)
    //const jobOffers = useAppSelector(state => state.myCompanies.jobOffers && state.myCompanies.jobOffers[props.company.id]) || []
    const atLeastOneJobOfferExists = jobOffers && Object.values(jobOffers).length > 0

    const jobOffer = jobOfferId && jobOffers[jobOfferId] || undefined

    useEffect(() => {
        props.loading(true)
        // TODO: This is only called when Tab "Job Offers" is opened and different company is selected
        careerApi.fetchAllJobOffersForCompany(props.selected_company.id)
            .then(jobOffers => {
                dispatch(overwriteJobOffers({jobOffers: jobOffers}))
                props.loading(false)
            })
            .catch(jobOfferFetchError => {
                // TODO
                props.loading(false)
            })
    }, [props.selected_company.id]);

    const createNewJobOffer = () => {
        navigate("create")
    }

    return (
        <Box margin="10px" boxSizing="border-box" display="flex" flexDirection="column" gap="10px">
            {!props.edit && !props.details && !props.create &&
                <Button sx={{alignSelf: "flex-start"}} variant="contained" color="primary"  onClick={createNewJobOffer}>
                    + Neues Job-Angebot erstellen
                </Button>
            }


            {!props.create && !atLeastOneJobOfferExists &&
                <Typography marginTop={"40%"} fontSize={"1.2rem"} sx={{textAlign: "center"}}>
                    Sie haben keine Job Angebote.
                </Typography>
            }

            {atLeastOneJobOfferExists && !props.edit && !props.details && !props.create &&
                Object.values(jobOffers).map(jobOffer =>
                    <MyJobOffer key={jobOffer.id!} current_job_offer={jobOffer} selected_company={props.selected_company} loading={props.loading}/>
                )
            }

            {props.details && jobOffer &&
                <MyJobOfferDetails current_jobOffer={jobOffer} selected_company={props.selected_company} loading={props.loading}/>
            }

            {props.edit && jobOffer &&
                <EditJobOffer jobOffer={jobOffer} selected_company={props.selected_company} mode="edit" loading={props.loading}/>
            }

            {props.create &&
                <EditJobOffer selected_company={props.selected_company} mode="create" loading={props.loading}/>
            }
        </Box>
    )

}


export default MyJobOffersView
