import JobApplication from "../../model/JobApplication";

export default interface MyJobApplicationState {
    jobApplications: Record<string, JobApplication>
}

export const defaultMyJobApplicationState: MyJobApplicationState = {
    jobApplications: {}
}
