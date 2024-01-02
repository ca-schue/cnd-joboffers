/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { UserCompanyAssociation } from './UserCompanyAssociation';
import type { UserProfile } from './UserProfile';
import type { UserSettings } from './UserSettings';
import type { UserSubscription } from './UserSubscription';
import type { UUID } from './UUID';

export type User = {
    id: UUID;
    profile: UserProfile;
    associations: UserCompanyAssociation;
    settings: UserSettings;
    subscription: UserSubscription;
};

