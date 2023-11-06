import {CompanyDTO, CompanyIdDTO} from "../../../src/.generated/user-service";
import React, {Fragment, useEffect} from "react";
import {useSearchParams} from "react-router-dom";
import Dropdown from "../Dropdown";
import {useAppSelector} from "../../state/hooks";

interface CompanySelectorInterface {
    companies: { [company_id: string]: CompanyDTO; }
    //selectedCompanyId: CompanyIdDTO | undefined
    onChange: (companyId: CompanyIdDTO) => void
}

const CompanySelector = (props: CompanySelectorInterface) => {

    useEffect(() => {

    }, [props.companies]);

    const ownedCompanyId = useAppSelector(state => state.myNewCompanies.owner_of?.id)

    //const companySearchParam = searchParams.get("company")
    //const selectedCompanyId = props.selectedCompanyId

    //const selectedCompany: CompanyDTO = companySearchParam && findCompanyById(companySearchParam) || selectedCompanyId && findCompanyById(selectedCompanyId) || props.companies[0]

    /*
    if (!selectedCompany) {
        return <Fragment />
    }*/


    const changeSelection = (companyId: CompanyIdDTO) => {
        props.onChange(companyId)
    }

    /*useEffect(() => {
        // TODO: Replaces # in URL for anchor of job application
        if (selectedCompany && companySearchParam != selectedCompany.id) {
            searchParams.set("company", selectedCompany.id)
            setSearchParams(searchParams, {replace: true})
        }
        if (selectedCompanyId && selectedCompanyId != selectedCompany.id) {
            props.onChange(selectedCompany.id)
        }
    }, [selectedCompany, companySearchParam]);

    useEffect(() => {
        if (companySearchParam && companySearchParam != selectedCompanyId) {
            if (findCompanyById(companySearchParam)) {
                props.onChange(companySearchParam)
            } else {
                if (selectedCompany) {
                    searchParams.set("company", selectedCompany.id)
                } else {
                    searchParams.delete("company")
                }
                setSearchParams(searchParams, {replace: true})
            }
        }
    }, [companySearchParam])*/

    /*if (!hasMoreThanOneCompany || !selectedCompany) {
        return <Fragment />
    }*/

    return (
        <Dropdown items={
            Object.values(props.companies).sort((a, b) => a.id != ownedCompanyId ? 1 : -1).map(company => {
                    return {
                        value: company.id,
                        text: company.id != ownedCompanyId ? company.details.name : company.details.name + " " + "(Deine Firma)",
                    }
                }
            )
        } onChangeSelection={(value) => changeSelection(value as CompanyIdDTO)}
        outsideBoxProps={{ zIndex: "5", padding: "7px 0px"}}/>
    )
}

export default CompanySelector
