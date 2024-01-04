/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Company } from '../models/Company';
import type { CompanyInviteUserRequest } from '../models/CompanyInviteUserRequest';
import type { CompanyRegistrationRequest } from '../models/CompanyRegistrationRequest';
import type { PublicCompanyProfile } from '../models/PublicCompanyProfile';
import type { ThrowableProblem } from '../models/ThrowableProblem';
import type { UpdateCompanyDetailsRequest } from '../models/UpdateCompanyDetailsRequest';
import type { UpdateCompanyLinksRequest } from '../models/UpdateCompanyLinksRequest';
import type { UUID } from '../models/UUID';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class CompanyService {

    /**
     * Creates a new company.
     * @param requestBody 
     * @returns Company Registration was successful.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static createNewCompany(
requestBody: CompanyRegistrationRequest,
): CancelablePromise<Company | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/register',
            body: requestBody,
            mediaType: 'application/json',
        });
    }

    /**
     * Get company for provided id.
     * @param companyId 
     * @returns Company Company was found.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static getCompany(
companyId: UUID,
): CancelablePromise<Company | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}',
            path: {
                'company-id': companyId,
            },
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Deletes the company.
     * @param companyId 
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static deleteCompany(
companyId: UUID,
): CancelablePromise<ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/companies/{company-id}',
            path: {
                'company-id': companyId,
            },
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Get company for provided id.
     * @param companyId 
     * @returns PublicCompanyProfile Company was found.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static getPublicCompanyProfile(
companyId: UUID,
): CancelablePromise<PublicCompanyProfile | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/companies/{company-id}/public-profile',
            path: {
                'company-id': companyId,
            },
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Invites an user to join the company.
     * @param companyId 
     * @param requestBody 
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @returns any Invitation was sent.
     * @throws ApiError
     */
    public static inviteUserToCompany(
companyId: UUID,
requestBody: CompanyInviteUserRequest,
): CancelablePromise<ThrowableProblem | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/invite-user',
            path: {
                'company-id': companyId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Subscribes to partner program
     * @param companyId 
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @returns Company Successfully subscribed to partner program.
     * @throws ApiError
     */
    public static subscribeToPartnerProgram(
companyId: UUID,
): CancelablePromise<ThrowableProblem | Company> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/companies/{company-id}/partner-program',
            path: {
                'company-id': companyId,
            },
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Overrides the current company details with the provided data.
     * @param companyId 
     * @param requestBody 
     * @returns Company Registration was successful.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static updateCompanyDetails(
companyId: UUID,
requestBody: UpdateCompanyDetailsRequest,
): CancelablePromise<Company | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/companies/{company-id}/update-company-details',
            path: {
                'company-id': companyId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

    /**
     * Overrides the current company links with the provided data.
     * @param companyId 
     * @param requestBody 
     * @returns Company Registration was successful.
     * @returns ThrowableProblem Returned if any error occurred during the request.
     * @throws ApiError
     */
    public static updateCompanyLinks(
companyId: UUID,
requestBody: UpdateCompanyLinksRequest,
): CancelablePromise<Company | ThrowableProblem> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/companies/{company-id}/update-company-links',
            path: {
                'company-id': companyId,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Company not found for id.`,
            },
        });
    }

}
