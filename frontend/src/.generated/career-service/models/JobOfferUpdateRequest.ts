/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { SalaryRange } from './SalaryRange';

export type JobOfferUpdateRequest = {
    title: string;
    description: string;
    tags: Array<string>;
    salary_range?: SalaryRange;
};

