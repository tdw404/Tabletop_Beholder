import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class UserRoleClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getRolesForUser', 'getUserRole', 'getDisplayRolesForUser'];
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
             return await this.authenticator.isUserLoggedIn();

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
    async getRolesForUser(userEmail, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
            const response = await this.axiosClient.get(`userroles/${userEmail}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.userRoleList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Gets roles for a single user, paired with org name.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns A list of orgs.
     */
    async getDisplayRolesForUser(userEmail, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
            const response = await this.axiosClient.get(`displayRoles/${userEmail}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.userDisplayRoles;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

        /**
     * Gets details about a single userRole.
     * @param userEmail The Cognito-associated email address to look for
     * @param orgId The orgId to look for
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns A single UserRole.
     */
        async getUserRole(userEmail, orgId, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
                const response = await this.axiosClient.get(`userroles/${userEmail}/organizations/${orgId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.userRole;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

        async getUsersForOrg(orgId, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
                const response = await this.axiosClient.get(`/userroles/byorg/${orgId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.userRoles;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }


    /**
         * Create a new UserRole.
         * @param userEmail
         * @param orgId
         * @param jobRole
         * @param displayName
         * @param roleStatus
         * @param tags Metadata tags to associate with a playlist.
         * @param errorCallback (Optional) A function to execute if the call fails.
         * @returns The playlist that has been created.
         */
        async createUserRole(userEmail, orgId, jobRole, displayName, roleStatus, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
                const response = await this.axiosClient.post(`userroles`, {
                    userEmail: userEmail,
                    orgId: orgId,
                    jobRole: jobRole,
                    displayName: displayName,
                    roleStatus: roleStatus
                }, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                return response.data.userRole;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

         /**
             * Update a UserRole.
             * @param userEmail
             * @param orgId
             * @param jobRole
             * @param displayName
             * @param roleStatus
             * @param tags Metadata tags to associate with a playlist.
             * @param errorCallback (Optional) A function to execute if the call fails.
             * @returns The playlist that has been created.
             */
            async updateUserRole(userEmail, orgId, jobRole, displayName, roleStatus, errorCallback) {
                try {
                    const token = await this.getTokenOrThrow("Encountered token error trying to call UserRole endpoint.");
                    const response = await this.axiosClient.put(`userroles/${userEmail}/organizations/${orgId}`, {
                        jobRole: jobRole,
                        displayName: displayName,
                        roleStatus: roleStatus
                    }, {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    });
                    return response.data.userRole;
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
