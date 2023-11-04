import {PropsWithChildren, useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {updateUser} from "../state/reducer/UserSlice";
import {useNavigate} from "react-router-dom";
import {userApi} from "../api/apis";


interface UpdateUserOnMountProps { }
const UserArea = (props: PropsWithChildren<UpdateUserOnMountProps>) => {

    const accountState = useAppSelector(state => state.account)

    const navigate = useNavigate()

    useEffect(() => {
        if (!accountState.loggedIn || accountState.account === undefined) {
            navigate("/")
        }
    }, [accountState])

    return props.children
}

export default UserArea
