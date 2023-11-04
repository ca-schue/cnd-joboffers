import React, {useEffect} from "react";
import {Box, Button, CircularProgress, Fade, InputAdornment, styled, TextField} from "@mui/material";
import {useNavigate} from "react-router-dom";
import DownloadDoneIcon from '@mui/icons-material/DownloadDone';
import {useAppDispatch, useAppSelector} from "../state/hooks";
import {createJobOffer, updateJobOfferAndIncrementVersion} from "../state/reducer/SelectedMemberCompanySlice";
import JobOffer, {JobOfferStatus} from "../model/JobOffer";
import BackButton from "./BackButton";
import { CompanyDTO } from "../../src/.generated/user-service";
import CreateJobOfferRequest from "../model/request/CreateJobOfferRequest";
import {careerApi} from "../api/apis";

interface EditJobOfferProps {
    mode: 'edit' | 'create'
    selected_company: CompanyDTO
    jobOffer?: JobOffer
    loading: ((is_loading: boolean) => void)
}


const Input = styled(TextField)(({ theme }) => ({
    "& input::-webkit-outer-spin-button, & input::-webkit-inner-spin-button": {
        display: "none",
    },
    "& input[type=number]": {
        MozAppearance: "textfield",
    },
}));

function EditJobOffer(props: EditJobOfferProps) {

    const {jobOffer} = props
    const navigate = useNavigate()
    const dispatch = useAppDispatch()

    const userId = useAppSelector(state => state.user.user?.id)

    const [saving, setSaving] = React.useState<'saving' | 'saved' | 'possible' | 'init' | 'default' | 'error'>("init");
    const [areYouSure, setAreYouSure] = React.useState(false);
    const [cannotAddTag, setCannotAddTag] = React.useState<string | undefined>(undefined);

    const [redirectTimeout, setRedirectTimeout] = React.useState<NodeJS.Timeout | undefined>(undefined);

    const [title, setTitle] = React.useState(jobOffer?.title || "");
    const [description, setDescription] = React.useState(jobOffer?.description || "");
    const [currentTag, setCurrentTag] = React.useState("");
    const [tags, setTags] = React.useState<string[]>(jobOffer?.tags || []);
    const [salaryLowerBound, setSalaryLowerBound] = React.useState(jobOffer?.salaryRange?.lowerBound || 0);
    const [salaryHigherBound, setSalaryHigherBound] = React.useState(jobOffer?.salaryRange?.upperBound || 0);

    const goBack = () => {
        if (saving == "possible" && !areYouSure) {
            setAreYouSure(true)
        } else {
            setAreYouSure(false)
            navigate(-1)
        }
    }

    useEffect(() => {
        if (saving == "default" || saving == 'saved') {
            setSaving('possible')
        }
        if (saving == "init") {
            setSaving("default")
        }
    }, [title, description, tags, salaryHigherBound, salaryHigherBound]);

    useEffect(() => {
        setCannotAddTag(undefined)
    }, [currentTag]);

    const handleKeyDown = (key: string) => {
        if (key == 'Enter') {
            const cannotAddTagReason =
                currentTag.length <= 1 && "Der Tag ist zu kurz!" ||
                tags.includes(currentTag) && "Tag existiert schon." ||
                tags.length > 15 && "Maximale Anzahl an Tags erreicht" ||
                currentTag.length > 20 && "Der Tag ist zu lang!" ||
                salaryLowerBound > salaryHigherBound && "Der untere Gehaltsrand muss kleiner sein als der obere Gehaltsrand" ||
                undefined

            if (!cannotAddTagReason) {
                setTags([...tags, currentTag])
                setCurrentTag("")
            } else {
                setCannotAddTag(cannotAddTagReason)
            }
        }
    }

    const removeTag = (tag: string) => {
        setTags([...tags.filter(item => item != tag)])
    }

    useEffect(() => {
        if (redirectTimeout) {
            return () => {
                clearTimeout(redirectTimeout)
            }
        }
    }, [redirectTimeout]);

    const saveJobOffer = () => {
        setSaving("saving")

        if (props.mode == "edit" && jobOffer) {
            props.loading(true)
            careerApi.updateJobOffer(jobOffer.companyId, jobOffer.id, title, description, tags, salaryLowerBound, salaryHigherBound, jobOffer.version).then(() => {
                dispatch(updateJobOfferAndIncrementVersion({jobOffer: {
                        ...jobOffer,
                        title: title,
                        description: description,
                        tags: tags,
                        salaryRange: {
                            lowerBound: salaryLowerBound,
                            upperBound: salaryHigherBound
                        }
                    }}))
                setSaving("saved")
                props.loading(false)
            })
        } else if (props.mode == "create") {
            const request: CreateJobOfferRequest = {
                companyId: props.selected_company.id,
                createdBy: userId!,
                title: title,
                description: description,
                tags: tags,
                status: JobOfferStatus.OPEN,
                lowerBound: salaryLowerBound,
                upperBound: salaryHigherBound
            }
            props.loading(true)
            careerApi.createJobOffer(request).then(id => {
                setSaving("saved")
                dispatch(createJobOffer({jobOfferId: id, createRequest: request}))
                setRedirectTimeout(setTimeout(() => {
                    navigate("/my-companies/job-offers/" + id)
                }, 3000))
                props.loading(false)
            })
        } else {
            setSaving("error")
        }
    }

    return (
        <Box margin="50px 2%" boxSizing="border-box" display="flex" flexDirection="column" gap={"15px"}>
            <Box sx={{display: "flex", width: "90%"}}>
                <BackButton onClick={goBack} sx={{margin: "10px 0px 30px 0px"}} color={areYouSure && "error" || "primary"} text={areYouSure && "Änderungen gehen verloren! Fortfahren?" || undefined}/>
            </Box>
            <TextField sx={{width: "90%"}} label={"Title"} value={title} onChange={(event) => setTitle(event.target.value)} />
            <TextField sx={{width: "90%"}} label={"Beschreibung"} multiline value={description} onChange={(event) => setDescription(event.target.value)}/>
            <TextField sx={{width: "90%"}} helperText={cannotAddTag} color={cannotAddTag && "error" || "primary"} label={"Tags"} value={currentTag} onKeyDown={event => handleKeyDown(event.key)} onChange={(event) => setCurrentTag(event.target.value)}
                       InputProps={{endAdornment: <Button onClick={() => handleKeyDown('Enter')}>+</Button>}}
            />
            {tags.length > 0 &&
                <Box alignSelf="flex-start" marginLeft="10px" marginTop="-5px" textAlign="left">
                    {tags.map((tag) =>
                        <Button key={tag} variant="outlined" sx={{padding: "0px 5px", fontSize: "0.8rem", margin: "0px 10px 5px 0px"}} onClick={() => removeTag(tag)}>{tag}</Button>
                    )}
                    <br />
                    <br />
                </Box>
            }
            <Input sx={{width: "90%"}} type="number" label={"Untere Gehaltsgrenze"} value={salaryLowerBound} onChange={(event) => setSalaryLowerBound(parseInt(event.target.value))}
                       InputProps={{
                           startAdornment: <InputAdornment position="start">€</InputAdornment>,
                       }}
            />
            <Input sx={{width: "90%"}} type="number" label={"Obere Gehaltsgrenze"} value={salaryHigherBound} onChange={(event) => setSalaryHigherBound(parseInt(event.target.value))}
                       InputProps={{
                           startAdornment: <InputAdornment position="start">€ </InputAdornment>,
                       }}
            />
            <Box width="100%" display="flex" flexDirection="row" marginTop="10px" alignItems="center">
                <Button variant="contained" color="success" onClick={saveJobOffer} disabled={saving != "possible"}>
                    {props.mode == "edit" && "Speichern" || (saving == "saved" && "Erstellt. Weiterleitung in 3 Sekunden" || "Erstellen")}
                </Button>
                {saving == "saving" && <CircularProgress sx={{marginLeft: "20px"}}/>}
                {saving == "saved" && <Fade in={true}><DownloadDoneIcon sx={{marginLeft: "20px", color: "green"}} /></Fade>}
            </Box>
        </Box>
    )

}


export default EditJobOffer
