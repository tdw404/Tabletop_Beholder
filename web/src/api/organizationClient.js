import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class OrganizationClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getAllOrgs', 'getOrganization'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();;
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
     * Get the identity of the current user
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The user information for the current user.
     */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async verifyLogin(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();
             return isLoggedIn;

        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        await this.authenticator.logout();
        window.location.href = "index.html";
        ;
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
     * Gets all orgs in the database.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns A list of orgs.
     */
    async getAllOrgs(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Organization endpoint.");
            const response = await this.axiosClient.get(`organizations`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.organizationList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Gets details about a single org.
     * @param orgId The orgId to look for
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns A list of orgs.
     */
        async getOrganization(orgId, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call Organization endpoint.");
                const response = await this.axiosClient.get(`organizations/${orgId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.organization;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}
