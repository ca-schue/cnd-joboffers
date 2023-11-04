import {PropsWithChildren, useEffect} from "react";
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {updateUser} from "../state/reducer/UserSlice";
import {useNavigate} from "react-router-dom";
import {userApi} from "../api/apis";


interface UpdateUserOnMountProps { }
const UserArea = (props: PropsWithChildren<UpdateUserOnMountProps>) => {

    const dispatch = useAppDispatch()

    const accountState = useAppSelector(state => state.account)
    const userId = useAppSelector(state => state.user.user?.id)

    const navigate = useNavigate()

    useEffect(() => {
        if (!accountState.loggedIn || accountState.account === undefined) {
            navigate("/")
        }
    }, [accountState])


    useEffect(() => {
        if (userId) {
            userApi.fetchUser(userId).then(user => dispatch(updateUser({user: user})))
        }
    },[userId])

    return props.children
}

export default UserArea
