import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";
//TODO - adding creatures needs to add them to the end of the turn order if turnQueue exists
//TODO - removing creatures also needs to remove them from the turnQueue
export default class EncounterClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getTokenOrThrow', 'getSingleEncounter', 'getMultipleEncounters', 'deleteEncounter', 'updateEncounter', 'createEncounter'];
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

    async getSingleEncounter(encounterId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Encounter endpoint.");
            const response = await this.axiosClient.get(`encounter/${encounterId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounter;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getMultipleEncounters(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Encounter endpoint.");
            const response = await this.axiosClient.get(`encounter`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounterList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteEncounter(encounterId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Encounter endpoint.");
            const response = await this.axiosClient.delete(`encounter/${encounterId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createEncounter(encounter, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Encounter endpoint.");
            const response = await this.axiosClient.post(`encounter`, {
                encounter: encounter
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.encounter;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async updateEncounter(encounter, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Encounter endpoint.");
            const response = await this.axiosClient.put(`encounter`, {
                encounter: encounter
            },
                {headers: {
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