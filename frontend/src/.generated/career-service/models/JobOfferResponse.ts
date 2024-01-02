/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JobOfferStatus } from './JobOfferStatus';
import type { SalaryRange } from './SalaryRange';
import type { UUID } from './UUID';

export type JobOfferResponse = {
    id: UUID;
    company_id: UUID;
    created_by: UUID;
    title: string;
    description: string;
    status: JobOfferStatus;
    tags: Array<string>;
    salary_range?: SalaryRange;
    version: number;
};

