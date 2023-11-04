import JobApplication, {JobApplicationStatus} from "../../../model/JobApplication";
import {Box, Button, Checkbox} from "@mui/material";
import React, {Fragment, ReactNode, useMemo, useState} from "react";

interface JobApplicationFilterListProps {
    jobApplications: JobApplication[]
    initialSelectedStatus: JobApplicationStatus[]
    filterableStatus: JobApplicationStatus[]
    sortFunction?: (a: JobApplication, b: JobApplication) => number
    transformFunction: (jobApplication: JobApplication) => ReactNode
}

const JobApplicationFilterList = (props: JobApplicationFilterListProps) => {

    const jobApplications = props.jobApplications

    const [selectedStatus, setSelectedStatus] = useState<JobApplicationStatus[]>(props.initialSelectedStatus);

    const displayedJobOffers =
        useMemo(() => jobApplications
            .filter(jobApplication => selectedStatus.includes(jobApplication.status))
            .sort(props.sortFunction), [props.jobApplications, props.sortFunction, selectedStatus])

    const selectAllStatus = () => {
        if (!allSelected) {
            setSelectedStatus(props.filterableStatus)
        } else {
            setSelectedStatus([])
        }
    }

    const toggleStatus = (status: JobApplicationStatus) => {
        const index = selectedStatus.indexOf(status)
        if (index >= 0) {
            const newArray = [...selectedStatus]
            newArray.splice(index, 1)
            setSelectedStatus(newArray)
        } else {
            setSelectedStatus([...selectedStatus, status])
        }
    };

    const allSelected = selectedStatus.length == props.filterableStatus.length

    return (
        <Fragment>
            <Box display="flex" flexWrap="wrap" justifyContent="space-between">
                <Button variant={allSelected && "contained" || "outlined"} onClick={() => selectAllStatus()} sx={{flex: "0 0 100%"}}>
                    Alle Auswählen
                </Button>
                {props.filterableStatus.map(status =>
                    <Button key={status.name} onClick={() => toggleStatus(status)}>
                        <Checkbox readOnly checked={selectedStatus.includes(status)} />
                        {status.text}
                    </Button>
                )}
            </Box>
            {
                displayedJobOffers.map(jobApplication => props.transformFunction(jobApplication))
            }
        </Fragment>
    )

}

export default JobApplicationFilterList
