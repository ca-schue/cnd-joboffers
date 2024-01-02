/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { JobApplicationStatus } from './JobApplicationStatus';
import type { UUID } from './UUID';

export type JobApplicationCreationRequest = {
    company_id: UUID;
    status: JobApplicationStatus;
    content: string;
};
