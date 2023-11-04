import {jobofferid} from "./JobOffer";
import {JobApplicationResponseDTO, JobApplicationsResponseDTO} from "../.generated/career-service";
import {UserIdDTO, CompanyIdDTO} from "../.generated/user-service";

export type jobapplicationid = string

export default interface JobApplication {
    id: jobapplicationid
    userId: UserIdDTO
    company: {
        id: CompanyIdDTO
        name: string
        location: string
    }
    jobOffer: {
        id: jobofferid
        title: string
    }
    status: JobApplicationStatus
    content: string,
    version: number
}

export class JobApplicationStatus {
    public static readonly DRAFT: JobApplicationStatus = {
        name: "DRAFT",
        text: "Entwurf"
    }
    public static readonly OPEN: JobApplicationStatus = {
        name: "OPEN",
        text: "Offen"
    }
    public static readonly DENIED: JobApplicationStatus = {
        name: "DENIED",
        text: "Abgelehnt"
    }
    public static readonly ACCEPTED: JobApplicationStatus = {
        name: "ACCEPTED",
        text: "Angenommen"
    }

    private constructor(public readonly name: string, public readonly text: string) {
    }
}

export const createJobApplicationsFromResponse = (jobApplicationsResponse: JobApplicationsResponseDTO) => {
    return jobApplicationsResponse.job_applications.map(jobApplication => createJobApplicationFromResponse(jobApplication))
}

export const createJobApplicationFromResponse = (jobApplicationResponse: JobApplicationResponseDTO): JobApplication => {
    return {
        id: jobApplicationResponse.id,
        userId: jobApplicationResponse.user_id,
        company: {
            id: jobApplicationResponse.company.id,
            name: jobApplicationResponse.company.name,
            location: jobApplicationResponse.company.location
        },
        jobOffer: {
            id: jobApplicationResponse.job_offer.id,
            title: jobApplicationResponse.job_offer.title
        },
        status: JobApplicationStatus[jobApplicationResponse.status as keyof typeof JobApplicationStatus]!,
        content: jobApplicationResponse.content,
        version: jobApplicationResponse.version
    }
}
