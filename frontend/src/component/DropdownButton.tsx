import {Box, Menu, MenuItem, Typography} from "@mui/material";
import ExpandCircleDownIcon from "@mui/icons-material/ExpandCircleDown";
import React from "react";
import {BoxProps} from "@mui/material/Box/Box";

interface DropdownButtonProps<T> {
    items: DropdownButtonItem<T>[]
    onClick: (value: T) => void
    onChangeSelection?: (value: T) => void
    disabled?: boolean
    textWidth?: string
    outsideBoxProps?: BoxProps
}

interface DropdownButtonItem<T> {
    key: string
    value: T
    text: string
}

const DropdownButton = <T,>(props: DropdownButtonProps<T>) => {

    const {items} = props
    const disabled = props.disabled == true

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const [selectedValue, setSelectedValue] = React.useState<DropdownButtonItem<T>>(items[0] || undefined);
    const open = Boolean(anchorEl);
    const handleClickListItem = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuItemClick = (
        value: DropdownButtonItem<T>,
    ) => {
        if (value != selectedValue) {
            if (props.onChangeSelection) {
                props.onChangeSelection(value.value)
            }
            setSelectedValue(value)
        }
        setAnchorEl(null)
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <Box bgcolor={disabled && "grey" || "primary.main"} color={"primary.contrastText"} margin={"20px 0px"} display="flex" flexDirection="row"
             {...props.outsideBoxProps} sx={{...(!disabled && {cursor: 'pointer'}), ...(props.outsideBoxProps?.sx) }}>
            <Box flexGrow="1" width={props.textWidth || "auto"} textAlign="center" display={"inline-block"} onClick={() => { if(!disabled) props.onClick(selectedValue.value)}} padding={"10px"}>
                <Typography>{selectedValue.text}</Typography>
            </Box>
            <Box sx={{
                ...(!disabled && {"&:hover": {backgroundColor: "primary.light"}})
            }} display={"inline-block"} onClick={(event) => {if(!disabled) handleClickListItem(event)}} padding={"10px"} borderLeft={"1px solid white"} >
                <ExpandCircleDownIcon sx={{transition: "transform 0.5s ease-out",  ...(open && {transform: "rotateX(180deg)"})}} />
            </Box>

            <Menu
                id="lock-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: "bottom",
                    horizontal: "left",
                }}
                transformOrigin={{
                    vertical: "top",
                    horizontal: "right",
                }}
            >
                {
                    items.map(item =>
                        <MenuItem
                            key={item.key}
                            disabled={selectedValue.value == item.value}
                            selected={selectedValue.value == item.value}
                            onClick={() => handleMenuItemClick(item)}
                            sx={{width: "100%"}}
                        >
                            {item.text}
                        </MenuItem>
                    )
                }

            </Menu>
        </Box>
    )

}

export default DropdownButton
