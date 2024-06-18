import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class EncounterClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getTokenOrThrow',
                                'getEncounterList', 'setInitiative'];
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

    async getEncounterList(sessionId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call RunEncounter endpoint.");
            const response = await this.axiosClient.get(`runEncounter/list/${sessionId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounterNameList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async setInitiative(encounterId, queueList, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call RunEncounter endpoint.");
            const response = await this.axiosClient.post(`runEncounter/${encounterId}?activity=setInitiative`, {
                queueList: queueList
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounter;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async nextTurn(encounterId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call RunEncounter endpoint.");
            const response = await this.axiosClient.post(`runEncounter/${encounterId}?activity=nextTurn`, {}, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounter;
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