import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class SessionClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getTokenOrThrow', 'getSingleSession', 'getMultipleSessions', 'deleteSession', 'updateSession', 'createSession'];
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

   async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    async getSingleSession(sessionId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Sessioned token error trying to call Session endpoint.");
            const response = await this.axiosClient.get(`session/${sessionId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.session;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getMultipleSessions(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Sessioned token error trying to call Session endpoint.");
            const response = await this.axiosClient.get(`session`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.sessionList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteSession(sessionId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Sessioned token error trying to call Session endpoint.");
            const response = await this.axiosClient.delete(`session/${sessionId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createSession(session, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Sessioned token error trying to call Session endpoint.");
            const response = await this.axiosClient.post(`session`, {
                session: session
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.session;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async updateSession(session, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Sessioned token error trying to call Session endpoint.");
            const response = await this.axiosClient.put(`session`, {
                session: session
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.session;
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
            console.error(errorFromApi);
            if (errorFromApi.includes('ERR_DUP')) {
                throw new error(errorFromApi);
            };
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}