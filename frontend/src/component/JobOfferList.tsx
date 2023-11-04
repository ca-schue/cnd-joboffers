import React, {Fragment, useEffect} from "react";
import {Box, CircularProgress, Container, makeStyles, TablePagination, Typography} from "@mui/material";
import JobOfferCard from "./JobOfferCard";
import JobOffer from "../model/JobOffer";
import {careerApi} from "../api/apis";
import catCrying from "../assets/cat-crying.gif"


function JobOfferList() {

    const [availableJobOffers, setAvailableJobOffers] = React.useState<null | Array<JobOffer>>(null);
    const [loading, setLoading] = React.useState(true);
    const [page, setPage] = React.useState(0);
    const [entriesPerPage, setEntriesPerPage] = React.useState(10);
    const [maxPageCount, setMaxPageCount] = React.useState(50);

    let displayJobOffers = availableJobOffers && [...availableJobOffers] || []
    displayJobOffers = displayJobOffers.splice(entriesPerPage * page, entriesPerPage)

    useEffect(() => {
        if (displayJobOffers.length == 0) {
            setLoading(true)
        }

        careerApi.fetchAllAvailableJobOffers(page, entriesPerPage).then(jobOffers => {
            setAvailableJobOffers(jobOffers.content)

            if (page != jobOffers.pagination.currentPage) {
                setPage(jobOffers.pagination.currentPage)
            }

            setMaxPageCount(
                Math.max(jobOffers.pagination.totalItemCount / entriesPerPage + (jobOffers.pagination.totalItemCount % entriesPerPage > 1 && 1 || 0), 1)
            )
            if (loading) {
                setLoading(false)
            }
        })
    }, [page, entriesPerPage]);

    const handleChangePage = (
        event: React.MouseEvent<HTMLButtonElement> | null,
        newPage: number,
    ) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        setEntriesPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    return (
        <Box width="100%" display="flex" flexDirection="column" alignItems="center">
            {loading &&
                <Box width="100%" height="60%" marginTop="10%" display="flex" flexDirection="column" alignItems="center" justifyContent="center">
                    <CircularProgress size="60px" sx={{marginBottom: "50px"}}/>
                    <Typography variant={"h6"}>Lade das nächste passende Job-Angebot für Sie!</Typography>
                </Box>
            }
            {!loading && displayJobOffers.length > 0 &&
                <Fragment>
                    {displayJobOffers?.map(jobOffer => <JobOfferCard jobOffer={jobOffer} key={jobOffer.id}/>)}
                    <TablePagination
                        component="div"
                        count={maxPageCount}
                        page={page}
                        onPageChange={handleChangePage}
                        rowsPerPage={entriesPerPage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                    />
                </Fragment>
            }
            {!loading && displayJobOffers.length == 0 &&
                <Box width="100%" height="60%" marginTop="10%" textAlign="center">
                    <img src={catCrying} style={{marginBottom: "50px"}}/>
                    <Typography variant="h6">Aktuell gibt es keine offenen Job-Angebote</Typography>
                </Box>
            }
        </Box>
    )

}


export default JobOfferList
