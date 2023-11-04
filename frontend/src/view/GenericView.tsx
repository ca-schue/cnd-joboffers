import React, {PropsWithChildren, useRef, useState} from "react";
import HeaderBar from "../component/HeaderBar";
import {Box} from "@mui/material";
import AuthModal from "../component/AuthModal";

const GenericView = (props: PropsWithChildren<{}>) => {


    return(
        <Box height="100%" width="100%" display="flex" flexDirection="column">
            <HeaderBar/>
            <Box display="flex" flexDirection="row" flexGrow="1">
                {props.children}
            </Box>
        </Box>
    )

}

export default GenericView
