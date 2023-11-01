import {ReactNode} from "react";

interface alertProps {
    children: ReactNode;
}

const Alert = ({children}: alertProps) => {
    return (
        <div>{children}</div>
    )
}

export default Alert;