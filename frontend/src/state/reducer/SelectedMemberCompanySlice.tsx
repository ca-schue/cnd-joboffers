import type {PayloadAction} from '@reduxjs/toolkit'
import {createSlice} from "@reduxjs/toolkit";
import {defaultSelectedMemberCompanyState} from "../state/SelectedMemberCompanyState";

import JobOffer, {jobofferid, JobOfferStatus} from "../../model/JobOffer";
import {CompanyDTO, CompanyIdDTO} from "../../../src/.generated/user-service";
import JobApplication, {JobApplicationStatus} from "../../model/JobApplication";
import CreateJobOfferRequest from "../../model/request/CreateJobOfferRequest";


export const SelectedMemberCompanySlice = createSlice({
    name: "myCompanySlice",
    initialState: defaultSelectedMemberCompanyState,
    reducers: {
        setSelectedCompany: (state, action: PayloadAction<{company: CompanyDTO}>) => {
            state.selected_company = action.payload.company
            state.jobOffers = {}
            state.jobApplications = {}
        },
        clearSelectedCompany: (state) => {
            state.selected_company = undefined
            state.jobOffers = {}
            state.jobApplications = {}
        },
        overwriteJobOffers: (state, action: PayloadAction<{jobOffers: JobOffer[]}>) => {
            const newJobOffers: {[job_offer_id: string]: JobOffer} = {}
            action.payload.jobOffers.forEach(jobOffer => {
                newJobOffers[jobOffer.id] = jobOffer
            })
            state.jobOffers = newJobOffers
            state.jobApplications = {}
        },
        createJobOffer: (state,  action: PayloadAction<{jobOfferId: jobofferid, createRequest: CreateJobOfferRequest}>) => {
            const {companyId, createdBy, title, description, tags, status, upperBound, lowerBound} = action.payload.createRequest
            const newState = state.jobOffers
            newState[action.payload.jobOfferId] = {
                companyId: companyId,
                createdBy: createdBy,
                id: action.payload.jobOfferId,
                title: title,
                description: description,
                tags: tags,
                salaryRange: {
                    lowerBound: lowerBound,
                    upperBound: upperBound
                },
                status: status,
                version: 0
            }
            state.jobOffers = newState
        },
        updateJobOfferAndIncrementVersion: (state, action: PayloadAction<{jobOffer: JobOffer}>) => {
            const updatedJobOffer = action.payload.jobOffer
            const newState = state.jobOffers
            newState[updatedJobOffer.id] = {
                ...updatedJobOffer,
                version: updatedJobOffer.version + 1
            }
            state.jobOffers = newState
        },
        closeJobOffer: (state,  action: PayloadAction<{jobOfferId: jobofferid}>) => {
            const jobOffers = state.jobOffers
            if (jobOffers && jobOffers[action.payload.jobOfferId]) {
                jobOffers[action.payload.jobOfferId].status = JobOfferStatus.CLOSED
                jobOffers[action.payload.jobOfferId].version++
            }
            const jobApplications = state.jobApplications[action.payload.jobOfferId]
            if (jobApplications) {
                Object.values(jobApplications).forEach(jobApplication => {
                    if (jobApplication.status == JobApplicationStatus.DRAFT || jobApplication.status == JobApplicationStatus.OPEN) {
                        jobApplication.status = JobApplicationStatus.DENIED
                        jobApplication.version++
                    }
                })
            }
        },
        deleteJobOffer: (state, action: PayloadAction<{jobOffer: JobOffer}>) => {
            const toBeDeletedJobOffer = action.payload.jobOffer
            const newState = {...state.jobOffers}
            if ( newState) {
                delete newState[toBeDeletedJobOffer.id]
                state.jobOffers = newState
            }
        },
        setJobApplicationsForJobOffer: (state,  action: PayloadAction<{jobOffer: JobOffer, jobApplications: JobApplication[]}>) => {
            const {jobApplications} = action.payload
            if (jobApplications.length == 0) {
                state.jobApplications = {}
            } else {
                jobApplications.forEach(jobApplication => {
                    if (!state.jobApplications[action.payload.jobOffer.id]) {
                        state.jobApplications[action.payload.jobOffer.id] = {[jobApplication.id]: jobApplication}
                    } else {
                        state.jobApplications[action.payload.jobOffer.id][jobApplication.id] = jobApplication
                    }
                })
            }
        },
        updateJobApplication: (state,  action: PayloadAction<{jobApplication: JobApplication}>) => {
            const updatedJobApplication: JobApplication = {
                ...action.payload.jobApplication,
                version: action.payload.jobApplication.version + 1
            }
            if (!state.jobApplications) {
                state.jobApplications = { [updatedJobApplication.jobOffer.id] : { [updatedJobApplication.id]: updatedJobApplication}}
            } else {
                state.jobApplications[updatedJobApplication.jobOffer.id][updatedJobApplication.id] = updatedJobApplication
            }
        },
    }
})

export const {
    setSelectedCompany,
    clearSelectedCompany,
    overwriteJobOffers,
    closeJobOffer,
    deleteJobOffer,
    updateJobOfferAndIncrementVersion,
    createJobOffer,
    setJobApplicationsForJobOffer,
    updateJobApplication} = SelectedMemberCompanySlice.actions