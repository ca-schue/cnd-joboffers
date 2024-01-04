import {config} from "../config/config";
import {
    JobApplicationCommandService,
    JobApplicationQueryService,
    JobOfferCommandService,
    JobOfferQueryService,
    OpenAPI
} from "../.generated/career-service";
import JobOffer, {createJobOfferFromResponse, createJobOffersFromResponse, jobofferid} from "../model/JobOffer";
import JobApplication, {
    createJobApplicationFromResponse,
    createJobApplicationsFromResponse,
    jobapplicationid,
    JobApplicationStatus
} from "../model/JobApplication";
import { CompanyIdDTO, UserIdDTO } from "../../src/.generated/user-service";
import CreateJobOfferRequest from "../model/request/CreateJobOfferRequest";
import Paginated, {createPaginatedJobOfferResponse} from "../model/Paginated";
import {ThrowableProblem} from "../.generated/career-service/models/ThrowableProblem";
import ExternalApiError from "../model/ExternalApiError";
import {JobApplicationCreationResponse} from "../.generated/career-service/models/JobApplicationCreationResponse";
import {JobApplicationResponse} from "../.generated/career-service/models/JobApplicationResponse";
import {JobApplicationsResponse} from "../.generated/career-service/models/JobApplicationsResponse";
import {JobOfferCreationResponse} from "../.generated/career-service/models/JobOfferCreationResponse";
import {PaginatedJobOffersResponse} from "../.generated/career-service/models/PaginatedJobOffersResponse";
import {JobOfferResponse} from "../.generated/career-service/models/JobOfferResponse";
import {JobOffersResponse} from "../.generated/career-service/models/JobOffersResponse";
import {handleUnexpectedResponse, parseError } from "./apis";

export interface CareerApi {
    createJobApplication(userId: string, companyId: string, jobOfferId: string, status: JobApplicationStatus, content: string): Promise<jobapplicationid>
    updateJobApplication(jobApplication: JobApplication, content: string): Promise<void>
    publishJobApplication(jobApplication: JobApplication): Promise<void>
    deleteJobApplication(jobApplication: JobApplication): Promise<void>

    fetchJobApplication(userId: UserIdDTO, jobApplicationId: jobapplicationid): Promise<JobApplication>
    fetchJobApplicationForUserAndJobOffer(userId: UserIdDTO, jobOfferId: jobofferid): Promise<JobApplication>
    fetchUsersJobApplications(userId: string): Promise<JobApplication[]>
    fetchAllPublicJobApplicationsByJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid): Promise<JobApplication[]>

    createJobOffer(payload: CreateJobOfferRequest): Promise<jobofferid>
    updateJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid, title: string, description: string, tags: string[], salaryLowerBound: number, salaryHigherBound: number, version: number): Promise<void>
    closeJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid, version: number): Promise<void>
    deleteJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid, version: number): Promise<void>

    acceptJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void>
    denyJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void>

    fetchJobOffer(companyId: string, jobOfferId: string): Promise<JobOffer>
    fetchAllAvailableJobOffers(skip: number, limit: number): Promise<Paginated<JobOffer>>
    fetchAllJobOffersForCompany(companyId: CompanyIdDTO): Promise<JobOffer[]>
}

export class DefaultCareerApi implements CareerApi {

    constructor() {
        OpenAPI.BASE = config.careerServiceUrl
        OpenAPI.HEADERS = { "Accept": "application/json, application/problem+json" }
    }

    /*

        JOB APPLICATIONS

     */

    createJobApplication(userId: string, companyId: string, jobOfferId: string, status: JobApplicationStatus, content: string): Promise<jobapplicationid> {
        return JobApplicationCommandService.createJobApplication(
            userId,
            jobOfferId,
            {
                company_id: companyId,
                content: content,
                status: status.name
            }
        ).catch(parseError<JobApplicationCreationResponse>).then(handleUnexpectedResponse)
        .then(response => response.id)

    }

    updateJobApplication(jobApplication: JobApplication, content: string): Promise<void> {
        return JobApplicationCommandService.updateJobApplication(
            jobApplication.userId,
            jobApplication.id,
            jobApplication.version.toString(),
            {
                content: content
            }
        ).catch(parseError<void>).then(handleUnexpectedResponse)
    }

    publishJobApplication(jobApplication: JobApplication): Promise<void> {
        return JobApplicationCommandService
            .publishJobApplication(jobApplication.company.id, jobApplication.id, jobApplication.version.toString())
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    deleteJobApplication(jobApplication: JobApplication): Promise<void> {
        return JobApplicationCommandService.deletedJobApplication(
            jobApplication.userId,
            jobApplication.id,
            jobApplication.version.toString()
        ).catch(parseError<void>).then(handleUnexpectedResponse)
    }

    fetchJobApplication(userId: UserIdDTO, jobApplicationId: jobapplicationid): Promise<JobApplication> {
        return JobApplicationQueryService
            .getJobApplicationForUser(userId, jobApplicationId, "-1")
            .catch(parseError<JobApplicationResponse>).then(handleUnexpectedResponse)
            .then(createJobApplicationFromResponse)
    }
    fetchJobApplicationForUserAndJobOffer(userId: UserIdDTO, jobOfferId: jobofferid): Promise<JobApplication> {
        return JobApplicationQueryService
            .getJobApplicationByUserAndJobOffer(userId, jobOfferId)
            .catch(parseError<JobApplicationResponse>).then(handleUnexpectedResponse)
            .then(createJobApplicationFromResponse)
    }

    fetchUsersJobApplications(userId: UserIdDTO): Promise<JobApplication[]> {
        return JobApplicationQueryService.getAllJobApplicationsByUserId(userId)
            .catch(parseError<JobApplicationsResponse>).then(handleUnexpectedResponse)
            .then(createJobApplicationsFromResponse)
    }

    fetchAllPublicJobApplicationsByJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid): Promise<JobApplication[]> {
        return JobApplicationQueryService
            .getAllPublishedJobApplicationsByJobOffer(companyId, jobOfferId)
            .catch(parseError<JobApplicationsResponse>).then(handleUnexpectedResponse)
            .then(createJobApplicationsFromResponse)
    }



    /*

        JOB OFFERS

     */

    createJobOffer(payload: CreateJobOfferRequest): Promise<jobofferid> {
        const {companyId, createdBy, title, description, tags, status, lowerBound, upperBound} = payload
        return JobOfferCommandService.createJobOffer(companyId, {
            created_by: createdBy,
            company_id: companyId,
            title: title,
            description: description,
            tags: tags,
            salary_range: (lowerBound || upperBound) && {
                lower_bound: {amount: lowerBound, currency: "EUR"},
                upper_bound: {amount: upperBound, currency: "EUR"}
            } || undefined,
            status: status.name,
        })
            .catch(parseError<JobOfferCreationResponse>).then(handleUnexpectedResponse)
            .then(response => response.id)
    }

    updateJobOffer(companyId: string, jobOfferId: string, title: string, description: string, tags: string[], salaryLowerBound: number, salaryHigherBound: number, version: number): Promise<void> {
        return JobOfferCommandService.updateJobOfferAttributes(
                companyId,
                jobOfferId,
                version.toString(),
                {
                    title: title,
                    description: description,
                    tags: tags,
                    salary_range: {
                        lower_bound: {amount: salaryLowerBound, currency: "EUR"},
                        upper_bound: {amount: salaryHigherBound, currency: "EUR"}
                    }
                }
            ).catch(parseError<void>).then(handleUnexpectedResponse)
    }

    closeJobOffer(companyId: string, jobOfferId: string, version: number): Promise<void> {
        return JobOfferCommandService.closeJobOffer(companyId, jobOfferId, version.toString())
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    deleteJobOffer(companyId: string, jobOfferId: string, version: number): Promise<void> {
        return JobOfferCommandService.deleteJobOffer(companyId, jobOfferId, version.toString())
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }


    acceptJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void> {
        return JobApplicationCommandService.acceptJobApplication(companyId, jobOfferId, jobApplicationId, version.toString())
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    denyJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void> {
        return JobApplicationCommandService.denyJobApplication(companyId, jobOfferId, jobApplicationId, version.toString())
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }



    fetchAllAvailableJobOffers(skip: number, limit: number): Promise<Paginated<JobOffer>> {
        return JobOfferQueryService.searchAvailableJobOffers(skip, limit)
            .catch(parseError<PaginatedJobOffersResponse>).then(handleUnexpectedResponse)
            .then(createPaginatedJobOfferResponse)
    }

    fetchJobOffer(companyId: string, jobOfferId: string): Promise<JobOffer> {
        return JobOfferQueryService.getJobOffer(companyId, jobOfferId)
            .catch(parseError<JobOfferResponse>).then(handleUnexpectedResponse)
            .then(createJobOfferFromResponse)
    }

    fetchAllJobOffersForCompany(companyId: CompanyIdDTO): Promise<JobOffer[]> {
        return JobOfferQueryService.getAllJobOffersByCompanyId(companyId)
            .catch(parseError<JobOffersResponse>).then(handleUnexpectedResponse)
            .then(createJobOffersFromResponse)
    }
}


export default CareerApi
