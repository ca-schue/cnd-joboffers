import JobOffer from "../../model/JobOffer";
import { CompanyDTO } from "../../../src/.generated/user-service";
import JobApplication from "../../model/JobApplication";

export default interface MyCompaniesState {
    owner_of?: CompanyDTO
    member_of: {[company_id: string]: CompanyDTO}
    invited_to: {[company_id: string]: CompanyDTO}
}

export const defaultMyCompaniesState: MyCompaniesState = {
    owner_of: undefined,
    member_of: {},
    invited_to: {}
}
