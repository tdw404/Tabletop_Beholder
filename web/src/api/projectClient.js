import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
  */
export default class ProjectClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['updateProject', 'getProject', 'clientLoaded', 'getIdentity', 'login', 'logout', 'getProjects', 'createProject'];
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

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }
    //---------------------------------------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Gets the playlist for the given ID.
     * @param id Unique identifier for a playlist
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The playlist's metadata.
     */
    async getProjects(orgId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create projects");
            const response = await this.axiosClient.get(`organizations/${orgId}/projects`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.projectList;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getProject(orgId, projectId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can create projects");
            const response = await this.axiosClient.get(`organizations/${orgId}/projects/${projectId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }});
            
            return response.data.project;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }


    /**
     * Create a new project owned by the current users organization.
     * @param name The name of the peoject to create.
     * @param time creation time(now).
     * @param orgId the current organization the user is working under.
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The project that has been created.
     */
    async createProject(name, time, orgId, errorCallback) {
        const pending = "Pending";
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Project endpoint.");
            const response = await this.axiosClient.post(`projects`, {
                orgId: orgId,
                name: name,
                projectStatus: pending,
                creationDate: time
            }, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return response.data.project;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    /**
     * Add a song to a playlist.
     * @param id The id of the playlist to add a song to.
     * @param asin The asin that uniquely identifies the album.
     * @param trackNumber The track number of the song on the album.
     * @returns The list of songs on a playlist.
     */
    async updateProject(completionPercentage, creationDate, endDate, name, orgId, projectDescription, projectId, projectStatus, taskList, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Only authenticated users can add a song to a playlist.");
            const response = await this.axiosClient.put(`organizations/${orgId}/projects/${projectId}`, {
                completionPercentage: completionPercentage,
                creationDate: creationDate,
                endDate: endDate,
                name: name,
                orgId: orgId,
                projectDescription: projectDescription,
                projectStatus: projectStatus,
                taskList: taskList,
                projectId: projectId
            }, 
                {headers: {
                    Authorization: `Bearer ${token}`
            }});
            return response.data.songList;
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
