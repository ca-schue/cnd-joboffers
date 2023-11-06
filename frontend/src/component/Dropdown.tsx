import {Badge, Box, Menu, MenuItem, Typography} from "@mui/material";
import ExpandCircleDownIcon from "@mui/icons-material/ExpandCircleDown";
import React, {useEffect, useRef} from "react";
import {BoxProps} from "@mui/material/Box/Box";

interface DropdownProps {
    items: DropdownButtonItem[]
    onChangeSelection?: (value: string) => void
    disabled?: boolean
    textWidth?: string
    outsideBoxProps?: BoxProps
}

interface DropdownButtonItem {
    value: string
    text: string
}

const DropdownButton = (props: DropdownProps) => {

    const {items} = props
    const disabled = props.disabled == true

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const [selectedValue, setSelectedValue] = React.useState<DropdownButtonItem>(items[0] || undefined);
    const open = Boolean(anchorEl);
    const handleClickListItem = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    useEffect(() => {
        if(items[0] != undefined && selectedValue.value == props.items[0].value) {
            setSelectedValue(props.items[0])
        }
    }, [props.items[0].text]);

    const handleMenuItemClick = (
        value: DropdownButtonItem,
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
        <Box bgcolor={disabled && "grey" || "primary.main"} color={"primary.contrastText"} display="flex" flexDirection="row"
             {...props.outsideBoxProps} sx={{...(!disabled && {cursor: 'pointer', "&:hover": {backgroundColor: "primary.light"}}), ...(props.outsideBoxProps?.sx) }}>
            <Box flexGrow="1" width={props.textWidth || "auto"} textAlign="center" display="flex" gap="10px" justifyContent="center" padding={"10px"}
                 onClick={(event) => {if(!disabled) handleClickListItem(event)}}>
                <Typography display="inline-block">{selectedValue.text}</Typography>
                <ExpandCircleDownIcon sx={{transition: "transform 0.5s ease-out",  ...(open && {transform: "rotateX(180deg)"})}} />
            </Box>

            <Menu
                id="lock-menu"
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
            >
                {
                    items.map(item =>
                        <MenuItem
                            key={item.value}
                            disabled={selectedValue.value == item.value}
                            selected={selectedValue.value == item.value}
                            onClick={() => handleMenuItemClick(item)}
                            sx={{width: anchorEl?.clientWidth || "100%",
                                "&:hover": {backgroundColor: "primary.light"}}}
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
