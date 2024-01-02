/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JobOfferStatus } from './JobOfferStatus';
import type { SalaryRange } from './SalaryRange';
import type { UUID } from './UUID';

export type JobOfferCreationRequest = {
    company_id: UUID;
    created_by: UUID;
    status: JobOfferStatus;
    title: string;
    description: string;
    tags: Array<string>;
    salary_range?: SalaryRange;
};

