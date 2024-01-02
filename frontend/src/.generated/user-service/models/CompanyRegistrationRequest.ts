/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { CompanyDetails } from './CompanyDetails';
import type { CompanyLinks } from './CompanyLinks';
import type { UUID } from './UUID';

export type CompanyRegistrationRequest = {
    owner_id: UUID;
    details: CompanyDetails;
    links?: CompanyLinks;
};
