/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JobOfferResponse } from './JobOfferResponse';
import type { Pagination } from './Pagination';

export type PaginatedJobOffersResponse = {
    pagination: Pagination;
    content: Array<JobOfferResponse>;
};
