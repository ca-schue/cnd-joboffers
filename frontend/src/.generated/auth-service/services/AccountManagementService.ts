/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AccountLoginResponse } from '../models/AccountLoginResponse';
import type { InternalAccount } from '../models/InternalAccount';
import type { InternalAccountEmailUpdateRequest } from '../models/InternalAccountEmailUpdateRequest';
import type { InternalAccountPasswordUpdateRequest } from '../models/InternalAccountPasswordUpdateRequest';
import type { InternalAccountRegistrationRequest } from '../models/InternalAccountRegistrationRequest';
import type { ThrowableProblem } from '../models/ThrowableProblem';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class AccountManagementService {

    /**
     * Login to an internal account with basic auth. Returns an access token with claim "subject-type = account"
     * @returns AccountLoginResponse Login to interal Account was successful
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static loginInternalAccount(): CancelablePromise<AccountLoginResponse | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/accounts/loginInternalAccount',
            errors: {
                401: `Wrong credentials`,
            },
        });
    }

    /**
     * Login with via ID-token provided by OIDC provider (issuer must be supported by auth. server). Creates new account if email does not exist. Returns access token with access token with claim "subject-type = account".
     * @returns AccountLoginResponse Login with OIDC was successful. Account exists.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static loginOidcAccount(): CancelablePromise<AccountLoginResponse | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/accounts/loginOIDCAccount',
            errors: {
                401: `Unsupported OIDC identity provider (ID-Token issuer).`,
            },
        });
    }

    /**
     * Registers a new internal account with the provided credentials.
     * @param requestBody 
     * @returns AccountLoginResponse Interal account registration was successful.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static registerInternalAccount(
requestBody: InternalAccountRegistrationRequest,
): CancelablePromise<AccountLoginResponse | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/accounts/registerInternalAccount',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                409: `Account with the email already exists.`,
            },
        });
    }

    /**
     * Updates the login email of the provided internal account
     * @param accountId 
     * @param requestBody 
     * @returns InternalAccount Update was successful
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static updateInternalAccountEmail(
accountId: UUID,
requestBody: InternalAccountEmailUpdateRequest,
): CancelablePromise<InternalAccount | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/accounts/{accountId}/update-internal-email',
            path: {
                'accountId': accountId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Account not found for id.`,
            },
        });
    }

    /**
     * Updates the login password of the provided internal account
     * @param accountId 
     * @param requestBody 
     * @returns any Update was successful
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static updateInternalAccountPassword(
accountId: UUID,
requestBody: InternalAccountPasswordUpdateRequest,
): CancelablePromise<any | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/accounts/{accountId}/update-internal-password',
            path: {
                'accountId': accountId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Account not found for id.`,
            },
        });
    }

    /**
     * Deletes the account and the user profile if available. {accountId} must match account id in access token
     * @param accountId 
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static deleteAccount(
accountId: UUID,
): CancelablePromise<ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/accounts/{accountId}',
            path: {
                'accountId': accountId,
            },
            errors: {
                404: `Account not found for id.`,
            },
        });
    }

}
