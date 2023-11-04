import JobApplication, {JobApplicationStatus} from "../../../model/JobApplication";
import {Box, Card, CardContent, Tooltip, Typography} from "@mui/material";
import React, {PropsWithChildren, useEffect, useLayoutEffect, useRef, useState} from "react";
import { PublicUserProfileDTO } from "../../../../src/.generated/user-service";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import LinkOutlinedIcon from '@mui/icons-material/LinkOutlined';
import {userApi} from "../../../api/apis";

interface JobApplicationCardProps {
    jobApplication: JobApplication
    selected?: boolean
    displayLink?: boolean
    displayName?: boolean
    displayJobOfferTitle?: boolean
}

const JobApplicationCard = (props: PropsWithChildren<JobApplicationCardProps>) => {

    const {jobApplication, selected, displayLink, displayName, displayJobOfferTitle} = props
    const [user, setUser] = useState<PublicUserProfileDTO | undefined>(undefined)
    const [isOverflow, setIsOverflow] = useState(false)
    const [isExpanded, setIsExpanded] = useState(props.selected || false)
    const [wasCopied, setWasCopied] = useState(false)
    const [tooltipIsOpen, setTooltipIsOpen] = React.useState(false);


    const cardRef = useRef<HTMLSpanElement>(null)
    const contentRef = useRef<HTMLSpanElement>(null)

    const applicantName = user && `${user.last_name}, ${user.first_name}` || ""
    const status = jobApplication.status

    useLayoutEffect(() => {
        if (selected) {
            cardRef.current?.scrollIntoView()
        }
    }, [selected]);

    useEffect(() => {
        userApi.fetchPublicUserProfile(jobApplication.userId).then(profile =>
            setUser(profile)
        )
    }, [props.jobApplication.userId]);

    useLayoutEffect(() => {
        const { current } = contentRef;
        if (current) {
            const hasOverflow = current.scrollHeight > current.clientHeight;
            setIsOverflow(hasOverflow);
        }
    }, [contentRef]);

    const toggleExpansion = () => {
        setIsExpanded(!isExpanded)
    }

    const copyLinkToJobApplication = () => {
        const copyUrl = window.location.href
        navigator.clipboard.writeText(copyUrl.slice(0, copyUrl.lastIndexOf("#")) + "#" + jobApplication.id)
        setWasCopied(true)
    }

    useEffect(() => {
        if (wasCopied) {
            const timeId = setTimeout(() => {
                setWasCopied(false)
            }, 3000)

            return () => {
                clearTimeout(timeId)
            }
        }
    }, [wasCopied]);

    return(
        <Box ref={cardRef}>
            <Card key={jobApplication.id} sx={{margin: "20px 0px", borderRadius: "0px", border: "solid 1px", borderColor: jobApplication.status == JobApplicationStatus.ACCEPTED && "green" || jobApplication.status == JobApplicationStatus.DENIED && "red" || status == JobApplicationStatus.DRAFT && "primary.light" || "black"}}>
                <CardContent sx={{padding: "10px", "&:last-child": {paddingBottom: "10px"}}}>
                    <Box display="flex" width="100%" alignItems="center">
                        {displayJobOfferTitle && <Typography color="primary.light" display="inline" fontSize="1.2rem">{jobApplication.jobOffer.title}</Typography>}
                        {displayName && <Typography display="inline" fontSize="1.2rem">{applicantName}</Typography>}
                        {
                            displayLink &&
                            <Tooltip title={!wasCopied && "Link zu Bewerbung kopieren" || "Link kopiert"} open={wasCopied || tooltipIsOpen} onOpen={() => setTooltipIsOpen(true)} onClose={() => setTooltipIsOpen(false)}>
                                <LinkOutlinedIcon fontSize="small" sx={{marginLeft: "10px", cursor: 'pointer', ...(wasCopied && {color: "green"})}} onClick={copyLinkToJobApplication}/>
                            </Tooltip>
                        }
                        <Typography display="inline" marginLeft="auto" fontSize="1.2rem" sx={{...(status == JobApplicationStatus.ACCEPTED && {color: "green"} || status == JobApplicationStatus.DENIED && {color: "red"} || status == JobApplicationStatus.DRAFT && {color: "primary.light"})}}>
                            {status != JobApplicationStatus.OPEN && status.text}
                        </Typography>
                    </Box>
                    <br/>
                    <Box>
                        <Typography sx={{overflow: "hidden", lineHeight: "20px", ...(!isExpanded && { height: "60px"})}} ref={contentRef}>
                            {jobApplication.content}
                        </Typography>
                        {isOverflow &&
                            <Box padding={"10px 0px 15px 0px"} width="100%" onClick={toggleExpansion} sx={{ cursor: 'pointer' }}>
                                {!isExpanded && <Tooltip title={"Alles anzeigen"}>
                                    <ExpandMoreIcon />
                                </Tooltip>}
                                {isExpanded && <ExpandLessIcon />}
                            </Box>
                        }
                    </Box>
                    <Box display="flex" flexDirection="row" justifyContent="flex-end" gap="15px">
                        {props.children}
                    </Box>
                </CardContent>
            </Card>
        </Box>
    )

}

export default JobApplicationCard
