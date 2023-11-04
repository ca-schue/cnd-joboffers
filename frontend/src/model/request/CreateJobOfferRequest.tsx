import { UserIdDTO, CompanyIdDTO } from "../../.generated/user-service";
import {JobOfferStatus} from "../JobOffer";

export default interface CreateJobOfferRequest {
    companyId: CompanyIdDTO,
    createdBy: UserIdDTO,
    title: string,
    description: string,
    tags: string[],
    status: JobOfferStatus,
    lowerBound: number,
    upperBound: number
}
