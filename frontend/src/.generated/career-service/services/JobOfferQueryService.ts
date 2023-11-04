/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JobOfferResponse } from '../models/JobOfferResponse';
import type { JobOffersResponse } from '../models/JobOffersResponse';
import type { PaginatedJobOffersResponse } from '../models/PaginatedJobOffersResponse';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class JobOfferQueryService {

    /**
     * Returns a list of all available job offers.
     * @param skip 
     * @param limit 
     * @param title 
     * @param companyIds 
     * @returns PaginatedJobOffersResponse List of all job offers available
     * @throws ApiError
     */
    public static searchAvailableJobOffers(
skip?: number,
limit?: number,
title?: string,
companyIds?: Array<UUID>,
): CancelablePromise<PaginatedJobOffersResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/available-job-offers',
            query: {
                'skip': skip,
                'limit': limit,
                'title': title,
                'company-ids': companyIds,
            },
        });
    }

    /**
     * Returns a list of available job offers for a specific company.
     * @param companyId 
     * @returns JobOffersResponse List of all job offers for an user
     * @throws ApiError
     */
    public static getAvailableJobOffersByCompanyId(
companyId: UUID,
): CancelablePromise<JobOffersResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/available-job-offers',
            path: {
                'company-id': companyId,
            },
        });
    }

    /**
     * Returns a list of all job offers for a specific company.
     * @param companyId 
     * @returns JobOffersResponse List of all job offers for an user
     * @throws ApiError
     */
    public static getAllJobOffersByCompanyId(
companyId: UUID,
): CancelablePromise<JobOffersResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/job-offers',
            path: {
                'company-id': companyId,
            },
        });
    }

    /**
     * Returns a specific available job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifNoneMatch 
     * @returns JobOfferResponse The requested job offer if found.
     * @throws ApiError
     */
    public static getAvailableJobOffer(
companyId: UUID,
jobOfferId: UUID,
ifNoneMatch?: string,
): CancelablePromise<JobOfferResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/available-job-offers/{job-offer-id}',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-None-Match': ifNoneMatch,
            },
        });
    }

    /**
     * Returns a specific job offer
     * @param companyId 
     * @param jobOfferId 
     * @param ifNoneMatch 
     * @returns JobOfferResponse The requested job offer if found.
     * @throws ApiError
     */
    public static getJobOffer(
companyId: UUID,
jobOfferId: UUID,
ifNoneMatch?: string,
): CancelablePromise<JobOfferResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/job-offers/{job-offer-id}',
            path: {
                'company-id': companyId,
                'job-offer-id': jobOfferId,
            },
            headers: {
                'IF-None-Match': ifNoneMatch,
            },
        });
    }

}
