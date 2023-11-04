import type {PayloadAction} from '@reduxjs/toolkit'
import {createSlice} from "@reduxjs/toolkit";
import {defaultMyJobApplicationState} from "../state/MyJobApplicationState";
import JobApplication, {jobapplicationid, JobApplicationStatus} from "../../model/JobApplication";
import { UserIdDTO, CompanyIdDTO } from "../../../src/.generated/user-service";
import {jobofferid} from "../../model/JobOffer";

export const MyJobApplicationSlice = createSlice({
    name: "myJobApplications",
    initialState: defaultMyJobApplicationState,
    reducers: {
        createJobApplication: (state, action: PayloadAction<{id: jobapplicationid, userId: UserIdDTO, companyId: CompanyIdDTO, jobOfferId: jobofferid, status: JobApplicationStatus, content: string}>) => {
            const {id, status, content, userId, companyId, jobOfferId} = action.payload
            state.jobApplications[id] = {
                id: id,
                userId: userId,
                jobOffer: {
                    id: jobOfferId,
                    title: "Please refresh to load this text"
                },
                company: {
                    id: companyId,
                    name: "Please refresh to load this text",
                    location: "Please refresh to load this text"
                },
                content: content,
                status: status,
                version: 0
            }
        },
        setJobApplications: (state, action: PayloadAction<{jobApplications: JobApplication[]}>) => {
            const jobApplications: Record<string, JobApplication> = {}
            action.payload.jobApplications.forEach(item => jobApplications[item.id!] = item)
            state.jobApplications = jobApplications
        },
        clearMyJobApplications: (state) => {
            state.jobApplications = {}
        },
        deleteJobApplication: (state, action: PayloadAction<{jobApplicationId: jobapplicationid}>) => {
            delete state.jobApplications[action.payload.jobApplicationId]
        },
        publishJobApplication: (state, action: PayloadAction<{jobApplicationId: jobapplicationid}>) => {
            state.jobApplications[action.payload.jobApplicationId].status = JobApplicationStatus.OPEN
            state.jobApplications[action.payload.jobApplicationId].version++
        },
        updateJobApplication: (state, action: PayloadAction<{jobApplication: JobApplication}>) => {
            state.jobApplications[action.payload.jobApplication.id] = {
                ...action.payload.jobApplication
            }
        },
        updateJobApplicationAndIncrementVersion: (state, action: PayloadAction<{jobApplication: JobApplication}>) => {
            state.jobApplications[action.payload.jobApplication.id] = {
                ...action.payload.jobApplication,
                version: action.payload.jobApplication.version + 1
            }
        },
    }
})

export const {
    createJobApplication,
    setJobApplications,
    clearMyJobApplications,
    deleteJobApplication,
    publishJobApplication,
    updateJobApplication,
    updateJobApplicationAndIncrementVersion,} = MyJobApplicationSlice.actions
