import {useAppSelector} from "../state/hooks";
import {OpenAPI as UserApiConfig} from "../.generated/user-service";
import {OpenAPI as AuthApiConfig} from "../.generated/auth-service";
import {OpenAPI as CareerApiConfig} from "../.generated/career-service";

function ApiProvider() {

    const token = useAppSelector(state => state.auth.accessToken)

    UserApiConfig.TOKEN = token
    AuthApiConfig.TOKEN = token
    CareerApiConfig.TOKEN = token

    console.log("Set access token to apis")

    return null

}


export default ApiProvider
