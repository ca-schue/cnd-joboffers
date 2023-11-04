/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Account } from '../models/Account';
import type { Client } from '../models/Client';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class AuthEndpointsService {

    /**
     * Get detailed account data from the provided access token.
     * @returns Account Token is valid and data could be retrieved.
     * @throws ApiError
     */
    public static tokenIntrospectionAccount(): CancelablePromise<Account> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/token-introspection/account',
        });
    }

    /**
     * Get detailed client data from the provided access token.
     * @returns Client Token is valid and data could be retrieved.
     * @throws ApiError
     */
    public static tokenIntrospectionClient(): CancelablePromise<Client> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/token-introspection/client',
        });
    }

}
