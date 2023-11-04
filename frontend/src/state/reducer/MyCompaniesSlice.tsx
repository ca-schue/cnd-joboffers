import type {PayloadAction} from '@reduxjs/toolkit'
import {createSlice} from "@reduxjs/toolkit";
import {defaultMyCompaniesState} from "../state/MyCompaniesState";
import {CompanyDTO, CompanyIdDTO} from "../../../src/.generated/user-service";
import JobOffer from "../../model/JobOffer";

export const MyCompaniesSlice = createSlice({
    name: "myCompanySlice",
    initialState: defaultMyCompaniesState,
    reducers: {
        setOwnerCompany: (state, action: PayloadAction<{company: CompanyDTO}>) => {
            state.owner_of = action.payload.company
        },
        updateMemberCompany: (state, action: PayloadAction<{updatedMemberCompany: CompanyDTO}>) => {
            const updatedMemberCompany = action.payload.updatedMemberCompany
            const newMemberOfCompanies = {...state.member_of}
            if (newMemberOfCompanies[updatedMemberCompany.id]) {
                newMemberOfCompanies[updatedMemberCompany.id] = {
                    ...updatedMemberCompany
                }
                state.member_of = newMemberOfCompanies
            }
        },
        setMemberCompanies: (state, action: PayloadAction<{memberOfCompanies: CompanyDTO []}>) => {
            const newMemberOfCompanies: {[company_id: string]: CompanyDTO} = {}
            action.payload.memberOfCompanies.forEach(memberOfCompany => {
                newMemberOfCompanies[memberOfCompany.id] = memberOfCompany
            })
            state.member_of = newMemberOfCompanies
        },
        removeInvite: (state, action: PayloadAction<{invitedToCompany: CompanyDTO}>) => {
            const invitedToCompany = action.payload.invitedToCompany
            const newState = {...state.invited_to}
            if (newState) {
                delete newState[invitedToCompany.id]
                state.invited_to = newState
            }
        },
        addInvitedToCompany: (state, action: PayloadAction<{company: CompanyDTO}>) => {
            state.invited_to = {...state.invited_to, [action.payload.company.id]: action.payload.company}
        },
        addMemberCompany: (state, action: PayloadAction<{company: CompanyDTO}>) => {
            state.member_of = {...state.member_of, [action.payload.company.id]: action.payload.company}
        },
        clearMyCompanies: (state) => {
            state.owner_of = undefined
            state.invited_to = {}
            state.member_of = {}
        }
    }
})

export const {
    setOwnerCompany,
    setMemberCompanies,
    updateMemberCompany,
    addMemberCompany,
    addInvitedToCompany,
    removeInvite,
    clearMyCompanies} = MyCompaniesSlice.actions