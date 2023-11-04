import JobOffer, {createJobOfferFromResponse, createJobOffersFromResponse, jobofferid} from "../../model/JobOffer";
import JobApplication, {
    createJobApplicationFromResponse,
    createJobApplicationsFromResponse,
    jobapplicationid,
    JobApplicationStatus
} from "../../model/JobApplication";
import { CompanyIdDTO } from "../../../src/.generated/user-service";
import CreateJobOfferRequest from "../../model/request/CreateJobOfferRequest";
import CareerApi from "../CareerApi";
import Paginated, {createPaginatedJobOfferResponse} from "../../model/Paginated";

export default class MockCareerApi implements CareerApi {
    async closeJobOffer(companyId: string, jobOfferId: string, version: number): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }
    async acceptJobApplication(companyId: string, jobOfferId: string, jobApplicationId: string, version: number): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }
    async denyJobApplication(companyId: string, jobOfferId: string, jobApplicationId: string, version: number): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }

    async publishJobApplication(jobApplication: JobApplication): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }

    async fetchJobApplicationForUserAndJobOffer(userId: string, jobOfferId: string): Promise<JobApplication> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<JobApplication>(resolve => resolve(
            createJobApplicationFromResponse(
                {
                    id: "1",
                    company: {
                        name: "companyName",
                        id: "companyId",
                        location: "location"
                    },
                    status: "ACCEPTED",
                    user_id: userId,
                    content: "Hello this is my job application",
                    job_offer: {
                        id: jobOfferId,
                        title: "JobOfferTitle"
                    },
                    version: 1
                }
            )
        ))
    }

    async deleteJobApplication(jobApplication: JobApplication): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }

    async updateJobApplication(jobApplication: JobApplication, content: string): Promise<void> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>(resolve => resolve())
    }

    async createJobOffer(payload: CreateJobOfferRequest): Promise<string> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<string>((resolve, reject) => {
            resolve("randomId")
        })
    }

    async fetchAllPublicJobApplicationsByJobOffer(companyId: CompanyIdDTO, jobOfferId: jobofferid): Promise<JobApplication[]> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<JobApplication[]>((resolve, reject) => {
            resolve([]
                /*
                [
                    createJobApplicationFromResponse(
                        {
                            id: "1",
                            company: {
                                name: "companyName, id: " + companyId,
                                id: companyId,
                                location: "location"
                            },
                            status: "ACCEPTED",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Hello this is my job application for companyId: " + companyId + ", jobOfferId: " + jobOfferId,
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle, jobOfferId: " + jobOfferId
                            },
                            version: 1
                        }
                    ),
                    createJobApplicationFromResponse(
                        {
                            id: "10",
                            company: {
                                name: "companyName",
                                id: companyId,
                                location: "location"
                            },
                            status: "SEND",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Et egestas quis ipsum suspendisse ultrices gravida dictum fusce ut. Odio tempor orci dapibus ultrices in. Vitae et leo duis ut. Placerat vestibulum lectus mauris ultrices eros in. Sed nisi lacus sed viverra tellus in hac. Enim sed faucibus turpis in. Blandit massa enim nec dui. Lectus quam id leo in vitae. Mauris pellentesque pulvinar pellentesque habitant morbi tristique. Est velit egestas dui id ornare arcu. Mauris in aliquam sem fringilla ut. Amet massa vitae tortor condimentum lacinia quis vel eros donec.\n" +
                                "\n" +
                                "Vitae sapien pellentesque habitant morbi tristique senectus et netus. Vel orci porta non pulvinar neque laoreet. Vitae tortor condimentum lacinia quis vel. Egestas quis ipsum suspendisse ultrices gravida dictum. Suspendisse potenti nullam ac tortor vitae purus. Volutpat sed cras ornare arcu. Quis vel eros donec ac odio tempor orci. Duis ultricies lacus sed turpis tincidunt. Ac auctor augue mauris augue neque gravida in. Adipiscing elit duis tristique sollicitudin nibh sit amet commodo nulla. Et netus et malesuada fames ac turpis egestas integer. Diam sollicitudin tempor id eu nisl nunc mi ipsum faucibus. Neque volutpat ac tincidunt vitae semper quis lectus nulla. Elementum nisi quis eleifend quam adipiscing vitae. Consectetur adipiscing elit pellentesque habitant morbi tristique.\n" +
                                "\n" +
                                "At quis risus sed vulputate odio ut enim blandit volutpat. Lobortis mattis aliquam faucibus purus. Pharetra massa massa ultricies mi quis hendrerit dolor magna. Eget nunc scelerisque viverra mauris in. Mauris in aliquam sem fringilla. Odio tempor orci dapibus ultrices in iaculis nunc. In tellus integer feugiat scelerisque varius morbi. In vitae turpis massa sed elementum tempus. Bibendum est ultricies integer quis auctor elit sed vulputate mi. Imperdiet nulla malesuada pellentesque elit eget gravida cum. At imperdiet dui accumsan sit. Sed risus ultricies tristique nulla. Rutrum quisque non tellus orci ac auctor augue.\n" +
                                "\n" +
                                "Ultrices eros in cursus turpis massa. Aenean pharetra magna ac placerat. Fusce ut placerat orci nulla pellentesque dignissim. Sed felis eget velit aliquet sagittis id consectetur purus. Duis tristique sollicitudin nibh sit. Neque aliquam vestibulum morbi blandit. Tellus rutrum tellus pellentesque eu tincidunt tortor. Bibendum ut tristique et egestas quis. Nisl rhoncus mattis rhoncus urna neque viverra justo nec. Ornare arcu dui vivamus arcu felis bibendum. Diam sollicitudin tempor id eu. Aliquet nibh praesent tristique magna sit amet purus gravida. Viverra vitae congue eu consequat ac felis donec et odio.\n" +
                                "\n" +
                                "Ultricies tristique nulla aliquet enim. Morbi tincidunt ornare massa eget egestas purus. Venenatis tellus in metus vulputate eu scelerisque felis. Feugiat vivamus at augue eget arcu dictum varius duis at. Semper feugiat nibh sed pulvinar proin gravida hendrerit. Arcu odio ut sem nulla. Sit amet luctus venenatis lectus magna fringilla. Porttitor eget dolor morbi non arcu risus quis. Venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Cursus vitae congue mauris rhoncus aenean vel elit scelerisque mauris. Aliquam nulla facilisi cras fermentum odio eu feugiat pretium nibh. In nulla posuere sollicitudin aliquam ultrices sagittis.\n" +
                                "\n" +
                                "Mi proin sed libero enim sed faucibus turpis in eu. Felis donec et odio pellentesque. Odio ut enim blandit volutpat maecenas volutpat blandit. Purus sit amet luctus venenatis lectus. Sit amet consectetur adipiscing elit. Amet consectetur adipiscing elit ut. Auctor elit sed vulputate mi. Ornare massa eget egestas purus viverra accumsan in nisl nisi. Morbi tristique senectus et netus et. Gravida cum sociis natoque penatibus et. Id eu nisl nunc mi ipsum faucibus vitae aliquet. Et netus et malesuada fames ac. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor.\n" +
                                "\n" +
                                "Vivamus at augue eget arcu dictum varius duis at consectetur. Pellentesque habitant morbi tristique senectus et netus et malesuada. Interdum velit laoreet id donec ultrices tincidunt arcu. Fermentum iaculis eu non diam phasellus vestibulum. Aenean et tortor at risus viverra adipiscing. Mauris vitae ultricies leo integer malesuada nunc vel risus. Vitae justo eget magna fermentum iaculis eu non. Vitae tortor condimentum lacinia quis vel eros. Et magnis dis parturient montes nascetur ridiculus. Suspendisse sed nisi lacus sed viverra tellus. Volutpat lacus laoreet non curabitur gravida arcu. Potenti nullam ac tortor vitae purus faucibus ornare. Viverra ipsum nunc aliquet bibendum enim facilisis. Phasellus egestas tellus rutrum tellus. Consequat nisl vel pretium lectus. A lacus vestibulum sed arcu. Sit amet commodo nulla facilisi nullam vehicula ipsum a.\n" +
                                "\n" +
                                "Dignissim convallis aenean et tortor at risus viverra adipiscing. Leo vel orci porta non pulvinar neque laoreet suspendisse interdum. Mauris rhoncus aenean vel elit scelerisque mauris pellentesque pulvinar. Ultricies lacus sed turpis tincidunt. Adipiscing commodo elit at imperdiet dui accumsan sit amet nulla. Luctus accumsan tortor posuere ac ut consequat semper viverra. Ultrices dui sapien eget mi. Dignissim enim sit amet venenatis. Ornare suspendisse sed nisi lacus. Netus et malesuada fames ac turpis egestas. Orci eu lobortis elementum nibh tellus molestie nunc non blandit. Elementum nibh tellus molestie nunc non blandit. Arcu ac tortor dignissim convallis aenean et tortor. Dictum at tempor commodo ullamcorper a lacus vestibulum. Porta nibh venenatis cras sed felis eget. Bibendum est ultricies integer quis auctor elit sed vulputate.\n" +
                                "\n" +
                                "Facilisis magna etiam tempor orci eu lobortis elementum nibh. Elementum nibh tellus molestie nunc non. Vivamus arcu felis bibendum ut tristique et egestas quis. Cursus risus at ultrices mi tempus imperdiet. Vestibulum rhoncus est pellentesque elit. Convallis aenean et tortor at. At in tellus integer feugiat scelerisque varius morbi. Tellus integer feugiat scelerisque varius morbi enim. Senectus et netus et malesuada fames ac turpis egestas sed. Condimentum mattis pellentesque id nibh tortor id. A cras semper auctor neque vitae tempus quam pellentesque nec. Sed odio morbi quis commodo odio. Quis enim lobortis scelerisque fermentum dui faucibus in ornare quam.\n" +
                                "\n" +
                                "Egestas egestas fringilla phasellus faucibus. Nunc consequat interdum varius sit amet. Id ornare arcu odio ut. Amet cursus sit amet dictum sit amet justo. Lobortis scelerisque fermentum dui faucibus in. Est placerat in egestas erat imperdiet sed. Id donec ultrices tincidunt arcu non sodales neque sodales. Massa sapien faucibus et molestie ac. Bibendum ut tristique et egestas quis ipsum. Eget arcu dictum varius duis. Blandit libero volutpat sed cras. Sit amet consectetur adipiscing elit ut aliquam purus. Suscipit adipiscing bibendum est ultricies integer quis auctor elit sed. Magna eget est lorem ipsum.",
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle"
                            },
                            version: 1
                        }
                    ),
                    createJobApplicationFromResponse(
                        {
                            id: "12",
                            company: {
                                name: "companyName",
                                id: companyId,
                                location: "location"
                            },
                            status: "ACCEPTED",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Hello this is my job application",
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle"
                            },
                            version: 1
                        }
                    ),
                    createJobApplicationFromResponse(
                        {
                            id: "5",
                            company: {
                                name: "companyName",
                                id: companyId,
                                location: "location"
                            },
                            status: "SEND",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Hello this is my job application",
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle"
                            },
                            version: 1
                        }
                    ),
                    createJobApplicationFromResponse(
                        {
                            id: "4",
                            company: {
                                name: "companyName",
                                id: companyId,
                                location: "location"
                            },
                            status: "DENIED",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Hello this is my job application",
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle"
                            },
                            version: 1
                        }
                    ),
                    createJobApplicationFromResponse(
                        {
                            id: "2",
                            company: {
                                name: "companyName",
                                id: companyId,
                                location: "location"
                            },
                            status: "DENIED",
                            user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                            content: "Hello this is my job application",
                            job_offer: {
                                id: jobOfferId,
                                title: "JobOfferTitle"
                            },
                            version: 1
                        }
                    )
                ]
            */
            )
        })
    }

    async fetchJobApplication(jobApplicationId: string): Promise<JobApplication> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<JobApplication>((resolve, reject) => {
            resolve(
                createJobApplicationFromResponse(
                    {
                        id: jobApplicationId,
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "test",
                        user_id: "e07b6782-f5f4-42f0-8186-177a6c515ebd",
                        content: "Hello this is my job application",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1
                    }
                )
            )
        })
    }

    async fetchUsersJobApplications(userId: string): Promise<JobApplication[]> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<JobApplication[]>((resolve, reject) => {
            resolve([]
                /*createJobApplicationsFromResponse({
                job_applications: [
                    {
                        id: "1",
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "SEND",
                        user_id: userId,
                        content: "Hello this is my job application 1",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1
                    },
                    {
                        id: "2",
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "SEND",
                        user_id: userId,
                        content: "Hello this is my job application 2",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1

                    },
                    {
                        id: "3",
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "DRAFT",
                        user_id: userId,
                        content: "Hello this is my job application 3",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1
                    },
                    {
                        id: "4",
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "ACCEPTED",
                        user_id: userId,
                        content: "Hello this is my job application 3",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1
                    },
                    {
                        id: "5",
                        company: {
                            name: "companyName",
                            id: "companyId",
                            location: "location"
                        },
                        status: "DENIED",
                        user_id: userId,
                        content: "Hello this is my job application 3",
                        job_offer: {
                            id: "jobOfferId",
                            title: "JobOfferTitle"
                        },
                        version: 1
                    }
                ]
            })*/
            )
        })
    }

    async createJobApplication(userId: string, companyId: string, jobOfferId: string, status: JobApplicationStatus, content: string): Promise<jobapplicationid> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<jobapplicationid>((resolve) => {
            resolve("125125")
        })
    }

    async deleteJobOffer(companyId: string, jobOfferId: string, version: number) {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>((resolve) => {
            resolve()
        })
    }

   async updateJobOffer(companyId: string, jobOfferId: string, title: string, description: string, tags: string[], salaryLowerBound: number, salaryHigherBound: number, version: number) {
       await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<void>((resolve) => {
            resolve()
        })
    }

    async fetchJobOffer(companyId: string, jobOfferId: string): Promise<JobOffer> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<JobOffer>((resolve, reject) => {
            resolve(createJobOfferFromResponse(
                {
                    id: jobOfferId,
                    company_id: companyId,
                    created_by: "createdBy",
                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas sed tempus urna et pharetra pharetra. Egestas quis ipsum suspendisse ultrices gravida dictum fusce. Et egestas quis ipsum suspendisse ultrices gravida dictum fusce ut. Non tellus orci ac auctor. Amet mattis vulputate enim nulla aliquet porttitor lacus. Sed turpis tincidunt id aliquet risus. Fermentum iaculis eu non diam phasellus. Adipiscing commodo elit at imperdiet dui accumsan sit amet nulla. Eget gravida cum sociis natoque penatibus et magnis.\n" +
                        "\n" +
                        "Lacus laoreet non curabitur gravida arcu ac tortor. Nibh nisl condimentum id venenatis a condimentum vitae. Cras tincidunt lobortis feugiat vivamus at augue eget. Ante in nibh mauris cursus mattis. Pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Aenean sed adipiscing diam donec adipiscing tristique risus nec. Feugiat sed lectus vestibulum mattis ullamcorper velit sed. Euismod nisi porta lorem mollis aliquam ut porttitor. Egestas erat imperdiet sed euismod. Auctor elit sed vulputate mi sit amet. Eros donec ac odio tempor orci. Mollis nunc sed id semper risus in hendrerit gravida. Maecenas sed enim ut sem viverra aliquet eget sit amet.\n" +
                        "\n" +
                        "Nisi scelerisque eu ultrices vitae. Rhoncus est pellentesque elit ullamcorper dignissim cras. Est sit amet facilisis magna etiam. Suspendisse ultrices gravida dictum fusce ut placerat orci nulla pellentesque. Vitae proin sagittis nisl rhoncus mattis. At ultrices mi tempus imperdiet nulla malesuada pellentesque. Quam nulla porttitor massa id neque aliquam vestibulum morbi. Orci porta non pulvinar neque laoreet suspendisse interdum consectetur. Amet risus nullam eget felis eget nunc. Turpis egestas integer eget aliquet nibh. Sit amet facilisis magna etiam tempor orci eu lobortis elementum. Ornare arcu odio ut sem nulla pharetra diam sit amet. Sit amet consectetur adipiscing elit ut aliquam. Laoreet sit amet cursus sit. Integer eget aliquet nibh praesent tristique magna sit. Ultricies tristique nulla aliquet enim tortor at. Quis eleifend quam adipiscing vitae. Feugiat sed lectus vestibulum mattis ullamcorper velit. At consectetur lorem donec massa sapien faucibus et molestie ac. Lorem ipsum dolor sit amet consectetur.\n" +
                        "\n" +
                        "Tortor pretium viverra suspendisse potenti nullam ac tortor vitae purus. Sit amet luctus venenatis lectus magna fringilla urna porttitor rhoncus. Tortor id aliquet lectus proin. Vitae nunc sed velit dignissim sodales ut eu sem integer. Velit euismod in pellentesque massa placerat duis ultricies. Amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar. Augue ut lectus arcu bibendum at varius vel. Et odio pellentesque diam volutpat commodo sed. Accumsan in nisl nisi scelerisque eu ultrices vitae auctor. Cum sociis natoque penatibus et. Egestas pretium aenean pharetra magna ac placerat. Arcu felis bibendum ut tristique et egestas quis ipsum suspendisse. Molestie at elementum eu facilisis sed odio.\n" +
                        "\n" +
                        "Sit amet aliquam id diam maecenas. Donec adipiscing tristique risus nec feugiat. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Adipiscing bibendum est ultricies integer quis auctor elit sed vulputate. Viverra ipsum nunc aliquet bibendum enim facilisis gravida neque convallis. Sagittis nisl rhoncus mattis rhoncus urna neque viverra justo. Ultrices in iaculis nunc sed augue lacus. Turpis egestas integer eget aliquet nibh praesent tristique magna sit. Imperdiet nulla malesuada pellentesque elit. Id consectetur purus ut faucibus pulvinar elementum integer enim. Quis viverra nibh cras pulvinar. Sit amet facilisis magna etiam tempor.",
                    title: "Hello this is a title, companyId: " + companyId + ", jobOfferId: " + jobOfferId,
                    status: "OPEN",
                    tags: ["Tag1", "Tag2", "Tag3"],
                    salary_range: {
                        lower_bound: {
                            amount: 50000,
                            currency: "EUR"
                        },
                        upper_bound: {
                            amount: 70000,
                            currency: "EUR"
                        }
                    },
                    version: 1
                }
            ))
        })
    }

    async fetchAllAvailableJobOffers(skip: number, limit: number): Promise<Paginated<JobOffer>> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        return new Promise<Paginated<JobOffer>>((resolve, reject) => {
                resolve(
                    createPaginatedJobOfferResponse({
                        pagination: {
                            total_pages: 0,
                            item_count: 0,
                            total_item_count: 0,
                            current_page: skip
                        },
                        content: []

                        /*createPaginatedJobOfferResponse(
                        {
                            pagination: {
                                total_pages: 10,
                                item_count: 11,
                                total_item_count: 120,
                                current_page: skip
                            },
                            content: [
                                {
                                    id: "4865624",
                                    company_id: "63d4f61e-4861-40b0-9686-2a54bf7b3025",
                                    created_by: "createdBy",
                                    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas sed tempus urna et pharetra pharetra. Egestas quis ipsum suspendisse ultrices gravida dictum fusce. Et egestas quis ipsum suspendisse ultrices gravida dictum fusce ut. Non tellus orci ac auctor. Amet mattis vulputate enim nulla aliquet porttitor lacus. Sed turpis tincidunt id aliquet risus. Fermentum iaculis eu non diam phasellus. Adipiscing commodo elit at imperdiet dui accumsan sit amet nulla. Eget gravida cum sociis natoque penatibus et magnis.\n" +
                                        "\n" +
                                        "Lacus laoreet non curabitur gravida arcu ac tortor. Nibh nisl condimentum id venenatis a condimentum vitae. Cras tincidunt lobortis feugiat vivamus at augue eget. Ante in nibh mauris cursus mattis. Pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Aenean sed adipiscing diam donec adipiscing tristique risus nec. Feugiat sed lectus vestibulum mattis ullamcorper velit sed. Euismod nisi porta lorem mollis aliquam ut porttitor. Egestas erat imperdiet sed euismod. Auctor elit sed vulputate mi sit amet. Eros donec ac odio tempor orci. Mollis nunc sed id semper risus in hendrerit gravida. Maecenas sed enim ut sem viverra aliquet eget sit amet.\n" +
                                        "\n" +
                                        "Nisi scelerisque eu ultrices vitae. Rhoncus est pellentesque elit ullamcorper dignissim cras. Est sit amet facilisis magna etiam. Suspendisse ultrices gravida dictum fusce ut placerat orci nulla pellentesque. Vitae proin sagittis nisl rhoncus mattis. At ultrices mi tempus imperdiet nulla malesuada pellentesque. Quam nulla porttitor massa id neque aliquam vestibulum morbi. Orci porta non pulvinar neque laoreet suspendisse interdum consectetur. Amet risus nullam eget felis eget nunc. Turpis egestas integer eget aliquet nibh. Sit amet facilisis magna etiam tempor orci eu lobortis elementum. Ornare arcu odio ut sem nulla pharetra diam sit amet. Sit amet consectetur adipiscing elit ut aliquam. Laoreet sit amet cursus sit. Integer eget aliquet nibh praesent tristique magna sit. Ultricies tristique nulla aliquet enim tortor at. Quis eleifend quam adipiscing vitae. Feugiat sed lectus vestibulum mattis ullamcorper velit. At consectetur lorem donec massa sapien faucibus et molestie ac. Lorem ipsum dolor sit amet consectetur.\n" +
                                        "\n" +
                                        "Tortor pretium viverra suspendisse potenti nullam ac tortor vitae purus. Sit amet luctus venenatis lectus magna fringilla urna porttitor rhoncus. Tortor id aliquet lectus proin. Vitae nunc sed velit dignissim sodales ut eu sem integer. Velit euismod in pellentesque massa placerat duis ultricies. Amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar. Augue ut lectus arcu bibendum at varius vel. Et odio pellentesque diam volutpat commodo sed. Accumsan in nisl nisi scelerisque eu ultrices vitae auctor. Cum sociis natoque penatibus et. Egestas pretium aenean pharetra magna ac placerat. Arcu felis bibendum ut tristique et egestas quis ipsum suspendisse. Molestie at elementum eu facilisis sed odio.\n" +
                                        "\n" +
                                        "Sit amet aliquam id diam maecenas. Donec adipiscing tristique risus nec feugiat. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Adipiscing bibendum est ultricies integer quis auctor elit sed vulputate. Viverra ipsum nunc aliquet bibendum enim facilisis gravida neque convallis. Sagittis nisl rhoncus mattis rhoncus urna neque viverra justo. Ultrices in iaculis nunc sed augue lacus. Turpis egestas integer eget aliquet nibh praesent tristique magna sit. Imperdiet nulla malesuada pellentesque elit. Id consectetur purus ut faucibus pulvinar elementum integer enim. Quis viverra nibh cras pulvinar. Sit amet facilisis magna etiam tempor.",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "1463274",
                                    company_id: "63d4f61e-4861-40b0-9686-2a54bf7b3025",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "1235437143",
                                    company_id: "63d4f61e-4861-40b0-9686-2a54bf7b3025",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "123545737",
                                    company_id: "63d4f61e-4861-40b0-9686-2a54bf7b3025",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "324613",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "132465437",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "5679242",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "345876",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "1234253",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "2347285586",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                },
                                {
                                    id: "54327274",
                                    company_id: "7048f387-22d4-46ec-9ba0-3eefbcc39493",
                                    created_by: "createdBy",
                                    description: "Description",
                                    title: "Hello this is a title",
                                    status: "OPEN",
                                    tags: ["Tag1", "Tag2", "Tag3"],
                                    salary_range: {
                                        lower_bound: {
                                            amount: 50000,
                                            currency: "EUR"
                                        },
                                        upper_bound: {
                                            amount: 70000,
                                            currency: "EUR"
                                        }
                                    },
                                    version: 1
                                }
                            ]
                        }
                    */
                    }));
            }
        );
    }

    async fetchAllJobOffersForCompany(companyId: string): Promise<JobOffer[]> {
        await new Promise(r => setTimeout(r, 1000 * 3))
        if(companyId == "company6") {
            return new Promise<JobOffer[]>((resolve, reject) => {
                resolve(
                    createJobOffersFromResponse({job_offers: []})
                )
            })
        }
        return new Promise<JobOffer[]>((resolve, reject) => {
            resolve([]
                    /*createJobOffersFromResponse(
                        {
                        job_offers: [
                            {
                                id: "4865624" + companyId,
                                company_id: companyId,
                                created_by: "createdBy",
                                description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas sed tempus urna et pharetra pharetra. Egestas quis ipsum suspendisse ultrices gravida dictum fusce. Et egestas quis ipsum suspendisse ultrices gravida dictum fusce ut. Non tellus orci ac auctor. Amet mattis vulputate enim nulla aliquet porttitor lacus. Sed turpis tincidunt id aliquet risus. Fermentum iaculis eu non diam phasellus. Adipiscing commodo elit at imperdiet dui accumsan sit amet nulla. Eget gravida cum sociis natoque penatibus et magnis.\n" +
                                    "\n" +
                                    "Lacus laoreet non curabitur gravida arcu ac tortor. Nibh nisl condimentum id venenatis a condimentum vitae. Cras tincidunt lobortis feugiat vivamus at augue eget. Ante in nibh mauris cursus mattis. Pulvinar etiam non quam lacus suspendisse faucibus interdum posuere. Aenean sed adipiscing diam donec adipiscing tristique risus nec. Feugiat sed lectus vestibulum mattis ullamcorper velit sed. Euismod nisi porta lorem mollis aliquam ut porttitor. Egestas erat imperdiet sed euismod. Auctor elit sed vulputate mi sit amet. Eros donec ac odio tempor orci. Mollis nunc sed id semper risus in hendrerit gravida. Maecenas sed enim ut sem viverra aliquet eget sit amet.\n" +
                                    "\n" +
                                    "Nisi scelerisque eu ultrices vitae. Rhoncus est pellentesque elit ullamcorper dignissim cras. Est sit amet facilisis magna etiam. Suspendisse ultrices gravida dictum fusce ut placerat orci nulla pellentesque. Vitae proin sagittis nisl rhoncus mattis. At ultrices mi tempus imperdiet nulla malesuada pellentesque. Quam nulla porttitor massa id neque aliquam vestibulum morbi. Orci porta non pulvinar neque laoreet suspendisse interdum consectetur. Amet risus nullam eget felis eget nunc. Turpis egestas integer eget aliquet nibh. Sit amet facilisis magna etiam tempor orci eu lobortis elementum. Ornare arcu odio ut sem nulla pharetra diam sit amet. Sit amet consectetur adipiscing elit ut aliquam. Laoreet sit amet cursus sit. Integer eget aliquet nibh praesent tristique magna sit. Ultricies tristique nulla aliquet enim tortor at. Quis eleifend quam adipiscing vitae. Feugiat sed lectus vestibulum mattis ullamcorper velit. At consectetur lorem donec massa sapien faucibus et molestie ac. Lorem ipsum dolor sit amet consectetur.\n" +
                                    "\n" +
                                    "Tortor pretium viverra suspendisse potenti nullam ac tortor vitae purus. Sit amet luctus venenatis lectus magna fringilla urna porttitor rhoncus. Tortor id aliquet lectus proin. Vitae nunc sed velit dignissim sodales ut eu sem integer. Velit euismod in pellentesque massa placerat duis ultricies. Amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar. Augue ut lectus arcu bibendum at varius vel. Et odio pellentesque diam volutpat commodo sed. Accumsan in nisl nisi scelerisque eu ultrices vitae auctor. Cum sociis natoque penatibus et. Egestas pretium aenean pharetra magna ac placerat. Arcu felis bibendum ut tristique et egestas quis ipsum suspendisse. Molestie at elementum eu facilisis sed odio.\n" +
                                    "\n" +
                                    "Sit amet aliquam id diam maecenas. Donec adipiscing tristique risus nec feugiat. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis. Adipiscing bibendum est ultricies integer quis auctor elit sed vulputate. Viverra ipsum nunc aliquet bibendum enim facilisis gravida neque convallis. Sagittis nisl rhoncus mattis rhoncus urna neque viverra justo. Ultrices in iaculis nunc sed augue lacus. Turpis egestas integer eget aliquet nibh praesent tristique magna sit. Imperdiet nulla malesuada pellentesque elit. Id consectetur purus ut faucibus pulvinar elementum integer enim. Quis viverra nibh cras pulvinar. Sit amet facilisis magna etiam tempor.",
                                title: "Hello this is a title, companyId: " + companyId,
                                status: "OPEN",
                                tags: ["Tag1", "Tag2", "Tag3"],
                                salary_range: {
                                    lower_bound: {
                                        amount: 50000,
                                        currency: "EUR"
                                    },
                                    upper_bound: {
                                        amount: 70000,
                                        currency: "EUR"
                                    }
                                },
                                version: 1
                            },
                            {
                                id: "1463274"+ companyId,
                                company_id: companyId,
                                created_by: "createdBy",
                                description: "Description",
                                title: "Hello this is a title, companyId:"+ companyId,
                                status: "OPEN",
                                tags: ["Tag1", "Tag2", "Tag3"],
                                salary_range: {
                                    lower_bound: {
                                        amount: 50000,
                                        currency: "EUR"
                                    },
                                    upper_bound: {
                                        amount: 70000,
                                        currency: "EUR"
                                    }
                                },
                                version: 1
                            }
                        ]
                    })*/
                )
            }
        )
    }

}
