import {
    CompanyDetailsDTO,
    CompanyDTO,
    CompanyIdDTO,
    CompanyLinksDTO,
    PublicCompanyProfileDTO,
    PublicUserProfileDTO,
    UpdateCompanyLinksRequestDTO,
    UserDTO,
    UserIdDTO,
    UserProfileDTO,
    UserSettingsDTO
} from "../../.generated/user-service";
import {UserApi} from "../UserApi";

export default class MockUserApi implements UserApi {

    fetchPublicCompanyProfile(companyId: string): Promise<PublicCompanyProfileDTO> {
        return new Promise<PublicCompanyProfileDTO>(resolve => {
            const companyDto: PublicCompanyProfileDTO = {
                id: companyId,
                details: {
                    name: "Example Company " + companyId,
                    description: "aslakshgolaga",
                    tags: ["test"]
                },
                links: {
                    social_media: ["twitter", "instagram"],
                    website_url: "testwebsite"
                }
            }
            resolve(companyDto)
        })
    }

    async subscribeToPartnerProgram(companyId: CompanyIdDTO): Promise<CompanyDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<CompanyDTO>(resolve => {
            const companyDto: CompanyDTO = {
                id: companyId,
                details: {
                    name: "Example Company " + companyId,
                    description: "aslakshgolaga",
                    tags: ["test"]
                },
                owner: "ownerId",
                links: {
                    social_media: ["twitter", "instagram"],
                    website_url: "testwebsite"
                },
                members: ["user1", "user2"],
                partner_program: {
                    partnered: true,
                    partner_until: "01.11.2023"
                }
            }
            resolve(companyDto)
        })
    }

    async deleteCompany(companyId: CompanyIdDTO): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3));
        return new Promise<void>(r => { r() });
    }

    async inviteUserToCompany(companyId: CompanyIdDTO, invitedUserEmail: string): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3));
        return new Promise<void>(r => { r() });
    }

    async updateCompanyDetails(companyId: CompanyIdDTO, companyDetails: CompanyDetailsDTO): Promise<CompanyDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<CompanyDTO>(resolve => {
            const companyDto: CompanyDTO = {
                id: companyId,
                details: companyDetails,
                owner: "ownerId",
                links: {
                    social_media: ["twitter", "instagram"],
                    website_url: "testwebsite"
                },
                members: ["user1", "user2"],
                partner_program: {
                    partnered: true,
                    partner_until: "tmr"
                }
            }
            resolve(companyDto)
        })
    }

    async updateCompanyLinks(companyId: CompanyIdDTO, companyLinks: UpdateCompanyLinksRequestDTO): Promise<CompanyDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<CompanyDTO>(resolve => {
            const companyDto: CompanyDTO = {
                id: companyId,
                details: {
                    name: "Example Company " + companyId,
                    description: "aslakshgolaga",
                    tags: ["test"]
                },
                owner: "ownerId",
                links: companyLinks,
                members: ["user1", "user2"],
                partner_program: {
                    partnered: true,
                    partner_until: "tmr"
                }
            }
            resolve(companyDto)
        })
    }

    async createNewCompany(ownerId: UserIdDTO, companyDetails: CompanyDetailsDTO, companyLinks: CompanyLinksDTO ): Promise<CompanyDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<CompanyDTO>(resolve => {
            const companyDto: CompanyDTO = {
                id: "12345798",
                details: companyDetails,
                owner: ownerId,
                links: companyLinks,
                members: [],
                partner_program: {
                    partnered: false
                }
            }
            resolve(companyDto)
        })
    }

    async acceptCompanyInvite(userId: UserIdDTO, company_id: CompanyIdDTO): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3));
        return new Promise<void>(r => { r() });
    }

    async updateUserProfile(userId: UserIdDTO, userProfile: UserProfileDTO): Promise<UserDTO> {
        console.log(userProfile.user_profile_email)
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<UserDTO>(resolve => {
            const userDto: UserDTO = {
                id: userId,
                profile: {
                    user_profile_email: userProfile.user_profile_email,
                    first_name: userProfile.first_name,
                    last_name: userProfile.last_name
                },
                associations: {
                    invited_to: ["company1", "compan2", "company3"],
                    member_of: ["company4", "company5", "company6"]
                },
                settings: {
                    night_mode_active: false
                },
                subscription: {
                    subscribed: false,
                    subscribedUntil: "01.01.2025"
                }
            }
            resolve(userDto)
        });
    }

    async updateUserSettings(userId: UserIdDTO, userSettings: UserSettingsDTO): Promise<UserDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<UserDTO>(resolve => {
            const userDto: UserDTO = {
                id: userId,
                profile: {
                    user_profile_email: "user_profile_email",
                    first_name: "first_name",
                    last_name: "last_name"
                },
                associations: {
                    invited_to: ["company1", "compan2", "company3"],
                    member_of: ["company4", "company5", "company6"]
                },
                settings: {
                    night_mode_active: userSettings.night_mode_active
                },
                subscription: {
                    subscribed: false,
                    subscribedUntil: "01.01.2025"
                }
            }
            resolve(userDto)
        });
    }

    async subscribe(userId: UserIdDTO, subscription_days: number): Promise<UserDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<UserDTO>(resolve => {
            const userDto: UserDTO = {
                id: userId,
                profile: {
                    user_profile_email: "user_profile_email",
                    first_name: "first_name",
                    last_name: "last_name"
                },
                associations: {
                    invited_to: ["company1", "compan2", "company3"],
                    member_of: ["company4", "company5", "company6"]
                },
                settings: {
                    night_mode_active: false
                },
                subscription: {
                    subscribed: true,
                    subscribedUntil: "01.01.2025"
                }
            }
            resolve(userDto)
        });
    }

    async deleteUser(userId: UserIdDTO) : Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(r => { r() });
    }

    async createUserProfile(user_profile_email: string, first_name: string, last_name: string): Promise<UserDTO> {
        return new Promise<UserDTO>(resolve => {
            const userDto: UserDTO = {
                id: "1324567",
                profile: {
                    user_profile_email: user_profile_email,
                    first_name: first_name,
                    last_name: last_name
                },
                associations: {
                    invited_to: ["company1", "compan2", "company3"],
                    member_of: ["company4", "company5", "company6"]
                },
                settings: {
                    night_mode_active: true
                },
                subscription: {
                    subscribed: false,
                    subscribedUntil: "tmr"
                }
            }
            resolve(userDto)
        });
    }

    async fetchUser(userId: string): Promise<UserDTO> {

        //return new Promise<User>(() => {throw new Error("rejected")});

        return new Promise<UserDTO>(resolve => {
            const userDto: UserDTO = {
                id: userId,
                profile: {
                    user_profile_email: "email",
                    first_name: "first-login",
                    last_name: "last-login"
                },
                associations: {
                    invited_to: ["company1", "company2", "company3"],
                    member_of: ["company4", "company5", "company6"]
                },
                settings: {
                    night_mode_active: true
                },
                subscription: {
                    subscribed: false,
                    subscribedUntil: "tmr"
                }
            }
            resolve(userDto)
        });

    }

    async fetchPublicUserProfile(userId: UserIdDTO): Promise<PublicUserProfileDTO> {
        return new Promise<PublicUserProfileDTO>(resolve => {
            const publicUserProfileDto: PublicUserProfileDTO = {
                id: userId,
                first_name: "first",
                last_name: "last",
                user_profile_email: "some_mail"
            };
            resolve(publicUserProfileDto)
        })
    }

    async fetchCompany(companyId: CompanyIdDTO): Promise<CompanyDTO> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<CompanyDTO>(resolve => {
            const companyDto: CompanyDTO = {
                id: companyId,
                details: {
                    name: "Example Company " + companyId,
                    description: "aslakshgolaga",
                    tags: ["test"]
                },
                owner: "ownerId",
                links: {
                    social_media: ["twitter", "instagram"],
                    website_url: "testwebsite"
                },
                members: ["user1", "user2"],
                partner_program: {
                    partnered: true,
                    partner_until: "tmr"
                }
            }
            resolve(companyDto)
        })
    }

}

