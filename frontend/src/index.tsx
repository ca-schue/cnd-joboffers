import React from 'react'
import 'dotenv/config'
import {createRoot} from 'react-dom/client'
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import './main.css'
import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider} from "react-router-dom";
import HomeRoute from "./route/HomeRoute";
import JobOfferRoute from "./route/JobOfferRoute";
import {Provider} from "react-redux";
import store from "./state/Store";
import ProfileRoute from "./route/ProfileRoute";
import MyCompaniesRoute from "./route/MyCompaniesRoute";
import {ThemeProvider} from "@mui/material";
import theme from "./theme";
import NotFoundPage from "./component/views/NotFoundPage";
import ApiProvider from "./api/ApiProvider";
import { AuthProvider } from "react-oidc-context";
import { User as OidcUser } from "oidc-client-ts";
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import {config} from "./config/config";

export { default as ReactFromModule } from 'react'


const container = document.getElementById('root')!

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route ErrorBoundary={NotFoundPage}>
            <Route path="/" element={<HomeRoute/>}/>
            <Route path="/my-companies" element={<MyCompaniesRoute view="company-details"/>}/>
            <Route path="/my-companies/invites" element={<MyCompaniesRoute view="company-invites"/>}/>
            <Route path="/my-companies/create" element={<MyCompaniesRoute view="create-company"/>}/>
            <Route path="/my-companies/company-details" element={<MyCompaniesRoute view="company-details"/>}/>
            <Route path="/my-companies/job-offers" element={<MyCompaniesRoute view="job-offers"/>}/>
            <Route path="/my-companies/job-offers/:jobOfferId" element={<MyCompaniesRoute view="job-offer-details"/>}/>
            <Route path="/my-companies/job-offers/:jobOfferId/edit"
                   element={<MyCompaniesRoute view="edit-job-offer"/>}/>
            <Route path="/my-companies/job-offers/create" element={<MyCompaniesRoute view="create-job-offer"/>}/>
            <Route path="/profile" element={<ProfileRoute view="profile"/>}/>
            <Route path="/profile/account" element={<ProfileRoute view="account"/>}/>
            <Route path="/profile/job-applications" element={<ProfileRoute view="job-applications"/>}/>
            <Route path="/profile/job-applications/:jobApplicationId/edit"
                   element={<ProfileRoute view="edit-job-application"/>}/>
            <Route path="/companies/:companyId/job-offers/:jobOfferId" element={<JobOfferRoute/>}/>
            <Route path="/companies/:companyId/job-offers/:jobOfferId/apply"
                   element={<JobOfferRoute applyForJobOffer/>}/>
        </Route>
    ),
);

const oidcConfig = {
    authority: config.oidcProviderDiscoveryEndpoint,
    client_id: config.oidcClientId,
    client_secret: config.oidcClientSecret,
    redirect_uri: config.oidcClientRedirectUrl,
};

createRoot(container).render(
    <AuthProvider {...oidcConfig}>
        <Provider store={store}>
            <ThemeProvider theme={theme}>
                <ApiProvider/>
                <RouterProvider router={router}/>
            </ThemeProvider>
        </Provider>
    </AuthProvider>
)
