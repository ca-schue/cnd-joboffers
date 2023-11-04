/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JobOfferCreationRequest } from '../models/JobOfferCreationRequest';
import type { JobOfferCreationResponse } from '../models/JobOfferCreationResponse';
import type { JobOfferUpdateRequest } from '../models/JobOfferUpdateRequest';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class JobOfferCommandService {

    /**
     * Create a new job offer for the logged in user.
     * @param companyId 
     * @param requestBody 
     * @returns JobOfferCreationResponse Job offer created.
     * @throws ApiError
     */
    public static createJobOffer(
companyId: UUID,
requestBody: JobOfferCreationRequest,
): CancelablePromise<JobOfferCreationResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers',
            path: {
                'company-id': companyId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Deletes a specific job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static deleteJobOffer(
companyId: UUID,
jobOfferId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/companies/{company-id}/job-offers/{job-offer-id}',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Update an existing job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifMatch 
     * @param requestBody 
     * @returns void 
     * @throws ApiError
     */
    public static updateJobOfferAttributes(
companyId: UUID,
jobOfferId: UUID,
ifMatch: string,
requestBody: JobOfferUpdateRequest,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/update',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Publishes the job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static publishJobOffer(
companyId: UUID,
jobOfferId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/publish',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

    /**
     * Closes the job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifMatch 
     * @returns void 
     * @throws ApiError
     */
    public static closeJobOffer(
companyId: UUID,
jobOfferId: UUID,
ifMatch: string,
): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/close',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-Match': ifMatch,
            },
        });
    }

}
