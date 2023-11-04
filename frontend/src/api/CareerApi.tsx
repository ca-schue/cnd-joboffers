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
        ).then(response => response.id)
    }

    updateJobApplication(jobApplication: JobApplication, content: string): Promise<void> {
        return JobApplicationCommandService.updateJobApplication(
            jobApplication.userId,
            jobApplication.id,
            jobApplication.version.toString(),
            {
                content: content
            }
        )
    }

    publishJobApplication(jobApplication: JobApplication): Promise<void> {
        return JobApplicationCommandService
            .publishJobApplication(jobApplication.company.id, jobApplication.id, jobApplication.version.toString())
    }

    deleteJobApplication(jobApplication: JobApplication): Promise<void> {
        return JobApplicationCommandService.deletedJobApplication(
            jobApplication.userId,
            jobApplication.id,
            jobApplication.version.toString()
        )
    }

    fetchJobApplication(userId: UserIdDTO, jobApplicationId: jobapplicationid): Promise<JobApplication> {
        return JobApplicationQueryService
            .getJobApplicationForUser(userId, jobApplicationId, "-1")
            .then(createJobApplicationFromResponse)
    }
    fetchJobApplicationForUserAndJobOffer(userId: UserIdDTO, jobOfferId: jobofferid): Promise<JobApplication> {
        return JobApplicationQueryService
            .getJobApplicationByUserAndJobOffer(userId, jobOfferId)
            .then(createJobApplicationFromResponse)
    }

    fetchUsersJobApplications(userId: UserIdDTO): Promise<JobApplication[]> {
        return JobApplicationQueryService.getAllJobApplicationsByUserId(userId)
            .then(createJobApplicationsFromResponse)
    }

    fetchAllPublicJobApplicationsByJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid): Promise<JobApplication[]> {
        return JobApplicationQueryService
            .getAllPublishedJobApplicationsByJobOffer(companyId, jobOfferId)
            .then(createJobApplicationsFromResponse)
    }



    /*

        JOB OFFERS

     */

    createJobOffer(payload: CreateJobOfferRequest): Promise<jobofferid> {
        const {companyId, createdBy, title, description, tags, status, lowerBound, upperBound} = payload
        return JobOfferCommandService.createJobOffer(companyId, {
            created_by: createdBy,
            company_id: companyId, //TODO Duplicated info
            title: title,
            description: description,
            tags: tags,
            salary_range: (lowerBound || upperBound) && {
                lower_bound: {amount: lowerBound, currency: "EUR"},
                upper_bound: {amount: upperBound, currency: "EUR"}
            } || undefined,
            status: status.name,
        }).then(response => response.id)
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
        )
    }

    closeJobOffer(companyId: string, jobOfferId: string, version: number): Promise<void> {
        return JobOfferCommandService.closeJobOffer(companyId, jobOfferId, version.toString())
    }

    deleteJobOffer(companyId: string, jobOfferId: string, version: number): Promise<void> {
        return JobOfferCommandService.deleteJobOffer(companyId, jobOfferId, version.toString())
    }


    acceptJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void> {
        return JobApplicationCommandService.acceptJobApplication(companyId, jobOfferId, jobApplicationId, version.toString())
    }

    denyJobApplication(companyId: CompanyIdDTO, jobOfferId: jobofferid, jobApplicationId: jobapplicationid, version: number): Promise<void> {
        return JobApplicationCommandService.denyJobApplication(companyId, jobOfferId, jobApplicationId, version.toString())
    }



    fetchAllAvailableJobOffers(skip: number, limit: number): Promise<Paginated<JobOffer>> {
        return JobOfferQueryService.searchAvailableJobOffers(skip, limit)
            .then(createPaginatedJobOfferResponse)
    }

    fetchJobOffer(companyId: string, jobOfferId: string): Promise<JobOffer> {
        return JobOfferQueryService.getJobOffer(companyId, jobOfferId).then(createJobOfferFromResponse)
    }

    fetchAllJobOffersForCompany(companyId: CompanyIdDTO): Promise<JobOffer[]> {
        return JobOfferQueryService.getAllJobOffersByCompanyId(companyId)
            .then(createJobOffersFromResponse)
    }
}


export default CareerApi
