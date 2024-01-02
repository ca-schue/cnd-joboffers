/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AcceptCompanyInvitationRequest } from '../models/AcceptCompanyInvitationRequest';
import type { ExtendUserSubscriptionRequest } from '../models/ExtendUserSubscriptionRequest';
import type { PublicUserProfile } from '../models/PublicUserProfile';
import type { UpdateUserProfileRequest } from '../models/UpdateUserProfileRequest';
import type { UpdateUserSettingsRequest } from '../models/UpdateUserSettingsRequest';
import type { User } from '../models/User';
import type { UserRegistrationRequest } from '../models/UserRegistrationRequest';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class UserService {

    /**
     * Register a new user.
     * @param requestBody
     * @returns User Registration was successful.
     * @throws ApiError
     */
    public static registerNewUser(
        requestBody: UserRegistrationRequest,
    ): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/register',
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Get user of provided user id.
     * @param userId
     * @returns User User was found.
     * @throws ApiError
     */
    public static getUser(
        userId: UUID,
    ): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{user-id}',
            path: {
                'user-id': userId,
            },
            errors: {
                404: `User not found for id.`,
            },
        });
    }

    /**
     * Deletes the user.
     * @param userId
     * @returns void
     * @throws ApiError
     */
    public static deleteUser(
        userId: UUID,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/users/{user-id}',
            path: {
                'user-id': userId,
            },
            errors: {
                404: `User not found for id.`,
            },
        });
    }

    /**
     * Get user public profile of provided user id.
     * @param userId
     * @returns PublicUserProfile User was found.
     * @throws ApiError
     */
    public static getPublicUserProfile(
        userId: UUID,
    ): CancelablePromise<PublicUserProfile> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/users/{user-id}/public-profile',
            path: {
                'user-id': userId,
            },
            errors: {
                404: `User not found for id.`,
            },
        });
    }

    /**
     * Update the user settings associated with this user.
     * @param userId
     * @param requestBody
     * @returns User User data was updated.
     * @throws ApiError
     */
    public static updateUserSettings(
        userId: UUID,
        requestBody: UpdateUserSettingsRequest,
    ): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/users/{user-id}/update-user-settings',
            path: {
                'user-id': userId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `User not found for id.`,
            },
        });
    }

    /**
     * Update the user profile associated with this user.
     * @param userId
     * @param requestBody
     * @returns User User data was updated.
     * @throws ApiError
     */
    public static updateUserProfile(
        userId: UUID,
        requestBody: UpdateUserProfileRequest,
    ): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/users/{user-id}/update-user-profile',
            path: {
                'user-id': userId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `User not found for id.`,
            },
        });
    }

    /**
     * Makes the user a subscriber or extends their subscription.
     * @param userId
     * @param requestBody
     * @returns User Subscription was successful.
     * @throws ApiError
     */
    public static subscribe(
        userId: UUID,
        requestBody: ExtendUserSubscriptionRequest,
    ): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/{user-id}/subscribe',
            path: {
                'user-id': userId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Accepts the invitation to be a member of a company.
     * @param userId
     * @param requestBody
     * @returns void
     * @throws ApiError
     */
    public static acceptInvitation(
        userId: UUID,
        requestBody: AcceptCompanyInvitationRequest,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/users/{user-id}/accept-company-invitation',
            path: {
                'user-id': userId,
            },
            body: requestBody,
            mediaType: 'application/json',
        });
    }

}
