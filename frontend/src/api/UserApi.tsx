import {config} from "../config/config";
import {
    CompanyService,
    OpenAPI as UserApiConfig,
    UserService,
    UserDTO,
    UserIdDTO,
    PublicUserProfileDTO,
    CompanyDTO,
    CompanyIdDTO,
    UserSettingsDTO,
    UserProfileDTO,
    CompanyDetailsDTO,
    CompanyLinksDTO,
    UpdateCompanyLinksRequestDTO,
    PublicCompanyProfileDTO
} from "../.generated/user-service";
import store from "../state/Store";
import {handleUnexpectedResponse, parseError} from "./apis";

export interface UserApi {
    fetchUser(userId: string) : Promise<UserDTO>
    fetchPublicUserProfile(userId: UserIdDTO) : Promise<PublicUserProfileDTO>
    fetchCompany(companyId: string) : Promise<CompanyDTO>
    fetchPublicCompanyProfile(companyId: string) : Promise<PublicCompanyProfileDTO>
    createUserProfile(user_profile_email: string, first_name: string, last_name: string): Promise<UserDTO>
    deleteUser(userId: UserIdDTO) : Promise<void>
    subscribe(userId: UserIdDTO, subscription_days: number): Promise<UserDTO>
    updateUserSettings(userId: UserIdDTO, userSettings: UserSettingsDTO): Promise<UserDTO>
    updateUserProfile(userId: UserIdDTO, userProfile: UserProfileDTO): Promise<UserDTO>
    acceptCompanyInvite(userId: UserIdDTO, company_id: CompanyIdDTO): Promise<void>
    createNewCompany(ownerId: UserIdDTO, companyDetails: CompanyDetailsDTO, companyLinks: CompanyLinksDTO ): Promise<CompanyDTO>
    updateCompanyDetails(companyId: CompanyIdDTO, companyDetails: CompanyDetailsDTO): Promise<CompanyDTO>
    updateCompanyLinks(companyId: CompanyIdDTO, companyLinks: UpdateCompanyLinksRequestDTO): Promise<CompanyDTO>
    inviteUserToCompany(companyId: CompanyIdDTO, invitedUserEmail: string): Promise<void>;
    deleteCompany(companyId: CompanyIdDTO): Promise<void>
    subscribeToPartnerProgram(companyId: CompanyIdDTO): Promise<CompanyDTO>
}

export class DefaultUserApi implements UserApi {

    constructor() {
        this.initUserApiConfig()
    }

    private getAuthState() {
        return store.getState().auth
    }

    private initUserApiConfig() {
        UserApiConfig.BASE = config.userServiceUrl
        UserApiConfig.USERNAME = undefined
        UserApiConfig.PASSWORD = undefined
        UserApiConfig.TOKEN = undefined
        UserApiConfig.HEADERS = { "Accept": "application/json, application/problem+json" }
    }

    private setAccessToken() {
        UserApiConfig.TOKEN = this.getAuthState().accessToken
        UserApiConfig.USERNAME = undefined
        UserApiConfig.PASSWORD = undefined
    }

    async subscribeToPartnerProgram(companyId: CompanyIdDTO): Promise<CompanyDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.subscribeToPartnerProgram(companyId)
            .catch(parseError<CompanyDTO>).then(handleUnexpectedResponse)
    }

    async deleteCompany(companyId: CompanyIdDTO): Promise<void> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.deleteCompany(companyId)
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async inviteUserToCompany(companyId: CompanyIdDTO, invitedUserEmail: string): Promise<void> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.inviteUserToCompany(
            companyId,
            {
                user_profile_email: invitedUserEmail
            }
        ).catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async updateCompanyDetails(companyId: CompanyIdDTO, companyDetails: CompanyDetailsDTO): Promise<CompanyDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.updateCompanyDetails(
            companyId,
            companyDetails
        ).catch(parseError<CompanyDTO>).then(handleUnexpectedResponse)
    }

    async updateCompanyLinks(companyId: CompanyIdDTO, companyLinks: UpdateCompanyLinksRequestDTO): Promise<CompanyDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.updateCompanyLinks(
            companyId,
            companyLinks
        ).catch(parseError<CompanyDTO>).then(handleUnexpectedResponse)
    }

    async createNewCompany(ownerId: UserIdDTO, companyDetails: CompanyDetailsDTO, companyLinks: CompanyLinksDTO ): Promise<CompanyDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.createNewCompany({
            owner_id: ownerId,
            details: companyDetails,
            links: companyLinks
        }).catch(parseError<CompanyDTO>).then(handleUnexpectedResponse)
    }

    async acceptCompanyInvite(userId: UserIdDTO, company_id: CompanyIdDTO): Promise<void> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.acceptInvitation(
            userId,
            {
                company_id: company_id
            }).catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async updateUserProfile(userId: UserIdDTO, userProfile: UserProfileDTO): Promise<UserDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.updateUserProfile(
            userId,
            {
                user_profile_email: userProfile.user_profile_email,
                first_name: userProfile.first_name,
                last_name: userProfile.last_name
            }
        ).catch(parseError<UserDTO>).then(handleUnexpectedResponse)
    }

    async updateUserSettings(userId: UserIdDTO, userSettings: UserSettingsDTO): Promise<UserDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.updateUserSettings(
            userId,
            {
                night_mode_active: userSettings.night_mode_active
            }
        ).catch(parseError<UserDTO>).then(handleUnexpectedResponse)
    }

    async subscribe(userId: UserIdDTO, subscription_days: number): Promise<UserDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.subscribe(
            userId,
            {
                extend_by_in_days: subscription_days
            }).catch(parseError<UserDTO>).then(handleUnexpectedResponse)
    }

    async deleteUser(userId: UserIdDTO) : Promise<void> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.deleteUser(userId)
            .catch(parseError<void>).then(handleUnexpectedResponse)
    }

    async fetchUser(userId: UserIdDTO) : Promise<UserDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.getUser(userId)
            .catch(parseError<UserDTO>).then(handleUnexpectedResponse)
    }

    async createUserProfile(user_profile_email: string, first_name: string, last_name: string): Promise<UserDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.registerNewUser({user_profile_email, first_name, last_name})
            .catch(parseError<UserDTO>).then(handleUnexpectedResponse)
    }

    async fetchPublicUserProfile(userId: UserIdDTO) : Promise<PublicUserProfileDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return UserService.getPublicUserProfile(userId)
            .catch(parseError<PublicUserProfileDTO>).then(handleUnexpectedResponse)
    }

    async fetchCompany(companyId: string) : Promise<CompanyDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.getCompany(companyId)
            .catch(parseError<CompanyDTO>).then(handleUnexpectedResponse)
    }

    async fetchPublicCompanyProfile(companyId: string) : Promise<PublicCompanyProfileDTO> {
        this.initUserApiConfig()
        this.setAccessToken()
        return CompanyService.getPublicCompanyProfile(companyId)
            .catch(parseError<PublicCompanyProfileDTO>).then(handleUnexpectedResponse)
    }

}
