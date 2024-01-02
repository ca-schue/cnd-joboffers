/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JobApplicationCompanyResponse } from './JobApplicationCompanyResponse';
import type { JobApplicationJobOfferResponse } from './JobApplicationJobOfferResponse';
import type { JobApplicationStatus } from './JobApplicationStatus';
import type { UUID } from './UUID';

export type JobApplicationResponse = {
    id: UUID;
    company: JobApplicationCompanyResponse;
    user_id: UUID;
    status: JobApplicationStatus;
    job_offer: JobApplicationJobOfferResponse;
    content: string;
    version: number;
};

