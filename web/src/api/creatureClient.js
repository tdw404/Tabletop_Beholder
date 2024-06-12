import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class CreatureClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getTokenOrThrow', 'getSingleCreature', 'getMultipleCreatures', 'deleteCreature', 'updateCreature', 'createCreature', 'searchTemplate', 'createTemplate'];
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

    async getSingleCreature(creatureId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Creature endpoint.");
            const response = await this.axiosClient.get(`creature/${creatureId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.creature;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getMultipleCreatures(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Creature endpoint.");
            const response = await this.axiosClient.get(`creature`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.creatureList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteCreature(creatureId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Creature endpoint.");
            const response = await this.axiosClient.delete(`creature/${creatureId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createCreature(creature, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Creature endpoint.");
            const response = await this.axiosClient.post(`creature`, {
                creature: creature
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.creature;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async updateCreature(creature, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Creature endpoint.");
            const response = await this.axiosClient.put(`creature`, {
                creature: creature
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.creature;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async searchTemplate(searchTerms, limit, errorCallback) {
            try {
                //const cleanedTerms = searchTerms.interpolate
                const token = await this.getTokenOrThrow("Encountered token error trying to call Creature Template endpoint.");
                const response = await this.axiosClient.get(`creatureTemplate/search/search=${searchTerms}&limit=${limit}`,
                    {headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.templateCreatureList;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

    async createTemplate(slug, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call Creature Template endpoint.");
                const response = await this.axiosClient.post(`creatureTemplate/${slug}`, {},
                    {headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.creature;
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