import AuthState, {defaultAuthState} from "./AuthState";
import UserState, {defaultUserState} from "./UserState";
import MyJobApplicationState, {defaultMyJobApplicationState} from "./MyJobApplicationState";
import AccountState, {defaultAccountState} from "./AccountState";
import MyCompaniesState, {defaultMyCompaniesState} from "./MyCompaniesState";
import SelectedMemberCompanyState, {defaultSelectedMemberCompanyState} from "./SelectedMemberCompanyState";

export default interface AppState {
    auth: AuthState
    user: UserState
    account: AccountState
    myJobApplications: MyJobApplicationState
    myNewCompanies: MyCompaniesState,
    selectedMemberCompany: SelectedMemberCompanyState
}

export const defaultState: AppState = {
    auth: defaultAuthState,
    user: defaultUserState,
    account: defaultAccountState,
    myJobApplications: defaultMyJobApplicationState,
    myNewCompanies: defaultMyCompaniesState,
    selectedMemberCompany: defaultSelectedMemberCompanyState,
}
