import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class SpellClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getTokenOrThrow', 'getSingleSpell', 'getMultipleSpells', 'deleteSpell', 'updateSpell', 'createSpell', 'searchTemplate', 'createTemplate'];
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

    async getSingleSpell(spellId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Spell endpoint.");
            const response = await this.axiosClient.get(`spell/${spellId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.spell;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getMultipleSpells(errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Spell endpoint.");
            const response = await this.axiosClient.get(`spell`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.spellList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteSpell(spellId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Spell endpoint.");
            const response = await this.axiosClient.delete(`spell/${spellId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createSpell(spell, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Spell endpoint.");
            const response = await this.axiosClient.post(`spell`, {
                spell: spell
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.spell;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async updateSpell(spell, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Spell endpoint.");
            const response = await this.axiosClient.put(`spell`, {
                spell: spell
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.spell;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async searchTemplate(searchTerms, limit, errorCallback) {
            try {
                const cleanedTerms = searchTerms.replace(' ','%20')
                const token = await this.getTokenOrThrow("Encountered token error trying to call Spell Template endpoint.");
                const response = await this.axiosClient.get(`spellTemplate/search/search=${cleanedTerms}&limit=${limit}`,
                    {headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.templateSpellList;
            } catch (error) {
                this.handleError(error, errorCallback)
            }
        }

    async createTemplate(slug, errorCallback) {
            try {
                const token = await this.getTokenOrThrow("Encountered token error trying to call Spell Template endpoint.");
                const response = await this.axiosClient.post(`spellTemplate/${slug}`, {},
                    {headers: {
                        Authorization: `Bearer ${token}`
                    }});
                return response.data.spell;
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