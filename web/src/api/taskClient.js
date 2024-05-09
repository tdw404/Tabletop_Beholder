import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";

export default class TaskClient extends BindingClass {

    constructor(props = {}) {
        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getSingleTask', 'getMultipleTasks', 'deleteTask', 'updateTask', 'createTask', 'getTasksForAssignee'];
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
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }


    async getSingleTask(orgId, taskId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.get(`organizations/${orgId}/tasks/${taskId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.task;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getMultipleTasks(orgId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.get(`organizations/${orgId}/tasks`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.tasks;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async deleteTask(orgId, taskId, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.delete(`organizations/${orgId}/tasks/${taskId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async createTask(orgId, assignee, completed, hoursToComplete, materialsList, name, startTime, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.post(`tasks`, {
                orgId: orgId,
                assignee: assignee,
                completed: completed,
                hoursToComplete: hoursToComplete,
                materialsList: materialsList,
                name: name,
                startTime: startTime
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.task;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async updateTask(orgId, taskId, assignee, completed, hoursToComplete, materialsList, name, startTime, stopTime, taskNotes, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.put(`organizations/${orgId}/tasks/${taskId}`, {
                assignee: assignee,
                completed: completed,
                hoursToComplete: hoursToComplete,
                materialsList: materialsList,
                name: name,
                startTime: startTime,
                stopTime: stopTime,
                taskNotes: taskNotes,
            },
                {headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.task;
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async getTasksForAssignee(orgId, assignee, errorCallback) {
        try {
            const token = await this.getTokenOrThrow("Encountered token error trying to call Task endpoint.");
            const response = await this.axiosClient.get(`organizations/${orgId}/assignees/${assignee}/tasks`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }});
            return response.data.tasks;
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
