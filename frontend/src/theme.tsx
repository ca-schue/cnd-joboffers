import {createTheme} from "@mui/material";

const theme = createTheme({
    palette: {
        primary: {
            main: "#9C12BF",
            light: "#AF41CB",
            dark: "#6D0C85",
            contrastText: "#FFFFFF",
        },
        secondary: {
            main: "#4411bc",
            light: "#6940C9",
            dark: "#2F0B83",
            contrastText: "#FFFFFF"
        },
        error: {
            main: "#DB2542",
            light: "#E25067",
            dark: "#99192E",
            contrastText: "#FFFFFF"
        },
        info: {
            main: "#0288D1",
            light: "#03A9F4",
            dark: "#01579B",
            contrastText: "#FFFFFF"
        }
    }
})

export default theme