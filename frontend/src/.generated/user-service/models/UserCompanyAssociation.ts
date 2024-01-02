/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { UUID } from './UUID';

export type UserCompanyAssociation = {
    member_of: Array<UUID>;
    invited_to: Array<UUID>;
    owner_of?: UUID;
};

