/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ClientCreationRequest } from '../models/ClientCreationRequest';
import type { ClientCreationResponse } from '../models/ClientCreationResponse';
import type { ThrowableProblem } from '../models/ThrowableProblem';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class ClientManagementService {

    /**
     * Creates a new client.
     * @param requestBody 
     * @returns ClientCreationResponse Client was successfully created.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static createNewClient(
requestBody: ClientCreationRequest,
): CancelablePromise<ClientCreationResponse | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/clients/create',
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Resets the password to a new random one. {clientId} must match client id in access token
     * @param clientId 
     * @returns ClientCreationResponse Password was successfully reset.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static resetPassword(
clientId: string,
): CancelablePromise<ClientCreationResponse | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/clients/{clientId}/reset-password',
            path: {
                'clientId': clientId,
            },
        });
    }

    /**
     * Deletes the client. {clientId} must match clientId id in access token
     * @param clientId 
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static deleteClient(
clientId: string,
): CancelablePromise<ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/clients/{clientId}',
            path: {
                'clientId': clientId,
            },
            errors: {
                404: `Client not found for id.`,
            },
        });
    }

}
