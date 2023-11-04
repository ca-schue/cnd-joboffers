import {Button} from "@mui/material";
import React, {Fragment} from "react";
import {useNavigate} from "react-router-dom";
import {ButtonProps} from "@mui/material/Button/Button";

interface BackButtonProps {
    text?: string | undefined
}

const BackButton = (props: BackButtonProps & ButtonProps) => {

    const navigate = useNavigate()

    const goBack = () => {
        navigate(-1);
    }

    return (
        <Button onClick={goBack} variant="contained" sx={{marginTop: "10px"}} {...props}>
            {!props.text &&
                <Fragment>
                    <a>←&ensp;</a>
                    <a>Zurück</a>
                </Fragment>
                || <a>{props.text}</a>
            }
        </Button>
    )

}

export default BackButton
