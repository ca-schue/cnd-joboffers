/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JobApplicationResponse } from '../models/JobApplicationResponse';
import type { JobApplicationsResponse } from '../models/JobApplicationsResponse';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class JobApplicationQueryService {

    /**
     * Returns a list of all job applications for a specific job offer.
     * @param companyId
     * @param jobOfferId
     * @returns JobApplicationsResponse List of all job applications for an user
     * @throws ApiError
     */
    public static getAllPublishedJobApplicationsByJobOffer(
        companyId: UUID,
        jobOfferId: UUID,
    ): CancelablePromise<JobApplicationsResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/job-applications',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
        });
    }

    /**
     * Get a specific job application for a job offer
     * @param companyId
     * @param jobOfferId
     * @param jobApplicationId
     * @param ifNoneMatch
     * @returns JobApplicationResponse A specific job application
     * @throws ApiError
     */
    public static getJobApplicationForCompany(
        companyId: UUID,
        jobOfferId: UUID,
        jobApplicationId: UUID,
        ifNoneMatch?: string,
    ): CancelablePromise<JobApplicationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/job-offers/{job-offer-id}/job-applications/{job-application-id}',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-None-Match': ifNoneMatch,
            },
        });
    }

    /**
     * Returns a list of all applications for a specific user.
     * @param userId
     * @returns JobApplicationsResponse List of all job applications for an user
     * @throws ApiError
     */
    public static getAllJobApplicationsByUserId(
        userId: UUID,
    ): CancelablePromise<JobApplicationsResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{user-id}/job-applications',
            path: {
                'user-id': userId,
            },
        });
    }

    /**
     * Returns the job application for a specific user and job offer
     * @param userId
     * @param jobOfferId
     * @param ifNoneMatch
     * @returns JobApplicationResponse The existing job application
     * @throws ApiError
     */
    public static getJobApplicationByUserAndJobOffer(
        userId: UUID,
        jobOfferId: UUID,
        ifNoneMatch?: string,
    ): CancelablePromise<JobApplicationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{user-id}/job-offers/{job-offer-id}/job-application',
            path: {
                'user-id': userId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-None-Match': ifNoneMatch,
            },
        });
    }

    /**
     * Get a specific job application for a job offer
     * @param userId
     * @param jobApplicationId
     * @param ifNoneMatch
     * @returns JobApplicationResponse A specific job application
     * @throws ApiError
     */
    public static getJobApplicationForUser(
        userId: UUID,
        jobApplicationId: UUID,
        ifNoneMatch?: string,
    ): CancelablePromise<JobApplicationResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{user-id}/job-applications/{job-application-id}',
            path: {
                'user-id': userId,
                'job-application-id': jobApplicationId,
            },
            headers: {
                'IF-None-Match': ifNoneMatch,
            },
        });
    }

}
