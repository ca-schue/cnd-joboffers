import {JobOfferResponseDTO, JobOffersResponseDTO} from "../.generated/career-service";
import {UserIdDTO, CompanyIdDTO} from "../.generated/user-service";

export type jobofferid = string

export default interface JobOffer {
    id: jobofferid
    companyId: CompanyIdDTO
    createdBy: UserIdDTO
    title: string
    description: string
    status: JobOfferStatus
    tags: string[]
    salaryRange?: SalaryRange
    version: number
}

export interface SalaryRange {
    lowerBound?: number
    upperBound?: number
}



export class  JobOfferStatus {
    public static readonly DRAFT: JobOfferStatus = {
        name: "DRAFT",
        text: "Entwurf"
    }
    public static readonly OPEN: JobOfferStatus = {
        name: "OPEN",
        text: "Offen"
    }
    public static readonly CLOSED: JobOfferStatus = {
        name: "CLOSED",
        text: "Geschlossen"
    }
    private constructor(public readonly name: string, public readonly text: string) {
    }
}

export const createJobOffersFromResponse = (jobOffersResponse: JobOffersResponseDTO): JobOffer[] => {
    return jobOffersResponse.job_offers.map(jobOffer => createJobOfferFromResponse(jobOffer))
}

export const createJobOfferFromResponse = (jobOfferResponse: JobOfferResponseDTO): JobOffer => {
    return {
        id: jobOfferResponse.id,
        companyId: jobOfferResponse.company_id,
        createdBy: jobOfferResponse.created_by,
        title: jobOfferResponse.title,
        description: jobOfferResponse.description,
        status: JobOfferStatus[jobOfferResponse.status as keyof typeof JobOfferStatus],
        tags: jobOfferResponse.tags,
        salaryRange: createSalaryRange(
            jobOfferResponse.salary_range?.lower_bound.amount,
            jobOfferResponse.salary_range?.upper_bound.amount
        ),
        version: jobOfferResponse.version
    }
}

function createSalaryRange(lowerBound?: number, upperBound?: number): SalaryRange | undefined {
    if (!lowerBound && !upperBound) {
        return undefined
    }
    return {
        lowerBound: lowerBound,
        upperBound: upperBound
    }
}
