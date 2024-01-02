/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { CompanyDetails } from './CompanyDetails';
import type { CompanyLinks } from './CompanyLinks';
import type { CompanyPartnerProgram } from './CompanyPartnerProgram';
import type { UUID } from './UUID';

export type Company = {
    id: UUID;
    owner: UUID;
    members: Array<UUID>;
    details: CompanyDetails;
    links: CompanyLinks;
    partner_program: CompanyPartnerProgram;
};

