import {CompanyDTO} from "../../.generated/user-service";
import JobOffer from "../../model/JobOffer";
import JobApplication from "../../model/JobApplication";

export default interface SelectedMemberCompanyState {
    selected_company?: CompanyDTO
    jobOffers: {[job_offer_id: string]: JobOffer}
    jobApplications: {[job_offer_id: string]: {[job_application_id: string]: JobApplication}}
}

export const defaultSelectedMemberCompanyState: SelectedMemberCompanyState = {
    selected_company: undefined,
    jobOffers: {},
    jobApplications: {}
}