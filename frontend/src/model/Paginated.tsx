import {PaginatedJobOffersResponseDTO} from "../.generated/career-service";
import JobOffer, {createJobOfferFromResponse, createJobOffersFromResponse} from "./JobOffer";

export default interface Paginated<T> {
    pagination: PaginationInfo
    content: T[]
}

export interface PaginationInfo {
    totalPages: number
    currentPage: number
    totalItemCount: number
    itemCount: number
}

export function createPaginatedJobOfferResponse(response: PaginatedJobOffersResponseDTO): Paginated<JobOffer> {
    return {
        content: response.content.map(jobOffer => createJobOfferFromResponse(jobOffer)),
        pagination: {
            totalPages: response.pagination.total_pages,
            currentPage: response.pagination.current_page,
            totalItemCount: response.pagination.total_item_count,
            itemCount: response.pagination.item_count,
        }
    }
}
