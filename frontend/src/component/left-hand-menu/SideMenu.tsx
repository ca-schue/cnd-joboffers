import {Box} from "@mui/material";
import React, {PropsWithChildren} from "react";


const SideMenu = (props: PropsWithChildren<{}>) => {

    return (
        <Box sx={{backgroundColor: "#dacbeb"}}
             width="30%" maxWidth="300px"  height="100%"
             display="flex" flexDirection="column" alignContent="center">
            {props.children}
        </Box>
    )

}

export default SideMenu
