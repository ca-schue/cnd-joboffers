import {Button, Typography} from "@mui/material";
import React, {PropsWithChildren} from "react";
import {ButtonOwnProps, ButtonProps} from "@mui/material/Button/Button";

interface GenericButtonProps {
    text?: string
    selected?: boolean
    disabled?: boolean
    onClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void
}

const GenericButton = (props: PropsWithChildren<GenericButtonProps>) => {

    return (
        <Button

            sx={{
                padding: "20px 0px",
                borderRadius: "0px",
                ...(props.disabled && {backgroundColor: "grey"}),
                ...(props.selected && {backgroundColor: "#b0aec7"})
            }}
            onClick={(event) => props.onClick(event)} disabled={props.selected || props.disabled || false}
        >
            {props.text && <Typography color={(props.selected || props.disabled) && "#41196e" || undefined} >{props.text}</Typography> || props.children}
        </Button>
    )

}

export default GenericButton
