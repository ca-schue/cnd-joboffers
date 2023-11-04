/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { User } from '../models/User';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class AuthService {

    /**
     * Get the user data from the provided access token.
     * @returns User Token is valid and user data could be retrieved.
     * @throws ApiError
     */
    public static getRequestUserData(): CancelablePromise<User> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/token-introspection',
        });
    }

}
