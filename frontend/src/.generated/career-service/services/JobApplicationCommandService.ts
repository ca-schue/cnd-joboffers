/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JobApplicationCreationRequest } from '../models/JobApplicationCreationRequest';
import type { JobApplicationCreationResponse } from '../models/JobApplicationCreationResponse';
import type { JobApplicationUpdateRequest } from '../models/JobApplicationUpdateRequest';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class JobApplicationCommandService {

    /**
     * Accepts the job application
     * @param companyId 
     * @param jobOfferId 
     * @param jobApplicationId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static acceptJobApplication(
companyId: UUID,
jobOfferId: UUID,
jobApplicationId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}/accept',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Denies the job application
     * @param companyId 
     * @param jobOfferId 
     * @param jobApplicationId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static denyJobApplication(
companyId: UUID,
jobOfferId: UUID,
jobApplicationId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}/deny',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Creates a new job application
     * @param userId 
     * @param jobOfferId 
     * @param requestBody 
     * @returns JobApplicationCreationResponse The newly created job application
     * @throws ApiError
     */
    public static createJobApplication(
userId: UUID,
jobOfferId: UUID,
requestBody: JobApplicationCreationRequest,
): CancelablePromise<JobApplicationCreationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/{user-id}/job-offers/{job-offer-id}/create-job-applications',
            path: {
                'user-id': userId,
                'job-offer-id': jobOfferId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Deletes a specific job application
     * @param userId 
     * @param jobApplicationId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static deletedJobApplication(
userId: UUID,
jobApplicationId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/users/{user-id}/job-applications/{job-application-id}',
            path: {
                'user-id': userId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Closes the job application
     * @param userId 
     * @param jobApplicationId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static publishJobApplication(
userId: UUID,
jobApplicationId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/{user-id}/job-applications/{job-application-id}/publish',
            path: {
                'user-id': userId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Update the job application content
     * @param userId 
     * @param jobApplicationId 
     * @param ifMatch 
     * @param requestBody 
     * @returns void 
     * @throws ApiError
     */
    public static updateJobApplication(
userId: UUID,
jobApplicationId: UUID,
ifMatch: string,
requestBody: JobApplicationUpdateRequest,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/{user-id}/job-applications/{job-application-id}/update',
            path: {
                'user-id': userId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

}
