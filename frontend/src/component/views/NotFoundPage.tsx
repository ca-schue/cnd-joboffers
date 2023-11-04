import {Box, Typography} from "@mui/material";
import React from "react";
import GenericView from "../../view/GenericView";
import cuteCat from "../../assets/cute-cat.jpg"
import catPurring from "../../assets/cat-purring.mp3"
import useSound from "use-sound";

const NotFoundPage = () => {

    const soundUrl = catPurring;
    const [play, {stop}] = useSound(soundUrl, {
        interrupt: true
    });

    return (
        <GenericView>
            <Box width="100%" height="100%" display="flex" justifyContent="center" alignItems="center" flexDirection={"column"}>
                <Typography variant={"h4"}>Hier gibt es leider nichts außer einer süßen Katze!</Typography>
                <Typography variant={"h5"}>Nutze die Gelegenheit und streichel sie ein bisschen</Typography>
                <br/>
                <Box width={"30%"} position={"relative"}>
                    <img src={cuteCat} width={"100%"} />
                    <Box width={"60%"} height={"80%"} position={"absolute"} top={"20%"} left={"20%"}
                         onMouseEnter={() => play()} onMouseLeave={() => stop()} />
                </Box>
            </Box>
        </GenericView>
    )

}

export default NotFoundPage
