import BindingClass from "../util/bindingClass";
import ProjectClient from '../api/projectClient';
import Header from '../components/header';
import DataStore from "../util/DataStore";
import TaskClient from "../api/taskClient";

const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};

const TASK_SEARCH_CRITERIA_KEY = 'search-criteria';
const TASK_SEARCH_RESULTS_KEY = 'search-results';


class ProjectDetailScript extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['deleteTask', 'deleteTaskFromProject', 'populateTable', 'convertDateToUTC', 'getEpochMillis', 'updateProject', 'addTaskToProject', 'createTask', 'loadTaskForProject', 'getHTMLForProject', 'convertToUTC', 'displaySearchResults', 'mount', 'loadProjectUponPageLoad'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.taskDataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.dataStore.addChangeListener(this.displaySearchResults);
        this.taskDataStore.addChangeListener(this.populateTable);
    }

    /**
    * Add the header to the page and load the ProjectClient.
    */
    async mount() {
        this.header.addHeaderToPage();
        this.projectClient = new ProjectClient();
        this.taskClient = new TaskClient();
        await this.loadProjectUponPageLoad();
        document.getElementById('create-task').addEventListener('click', this.addTaskToProject);
        document.getElementById('update-project').addEventListener('click', this.updateProject);
    }

    async createTask() {
        //alert("createTask called")
        var startTime = this.dataStore.get(SEARCH_RESULTS_KEY);
        startTime = startTime.creationDate;
        var taskClient = new TaskClient();
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const assignee = null;
        const completed = false;
        const hoursToComplete = null;
        const materialsList = null;
        const name = document.getElementById('new-task-name').value;
        var results = null;
        if (orgId && name) {

            results = await taskClient.createTask(orgId, assignee, completed, hoursToComplete, materialsList, name, startTime).then(response => {

                const searchCriteria = this.taskDataStore.get(TASK_SEARCH_CRITERIA_KEY);
                var searchResults = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);
                searchResults.push(response);
                //console.log(searchResults)
                this.taskDataStore.setState({
                    [TASK_SEARCH_CRITERIA_KEY]: searchCriteria,
                    [TASK_SEARCH_RESULTS_KEY]: searchResults,
                });
                //console.log(response);
                return response;
            }).catch(e => {
                console.log(e);
            });;

        }
    }

    async addTaskToProject() {
        await this.createTask();
        var projectClient = new ProjectClient();
        //const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        var searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const completionPercentage = searchResults.completionPercentage;
        const creationDate = searchResults.creationDate;
        const endDate = searchResults.endDate;
        const name = searchResults.name;
        const orgId = searchResults.orgId;
        const projectDescription = searchResults.projectDescription;
        const projectId = searchResults.projectId;
        const projectStatus = searchResults.projectStatus;

        const taskList = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);

        if (projectId && orgId) {
            const results = await projectClient.updateProject(completionPercentage, creationDate, endDate, name, orgId, projectDescription, projectId, projectStatus, taskList).then(response => {
                console.log(response);
            }).catch(e => {
                console.log(e);
            });;
        }
    }

    async updateProject() {
        // const safety = await this.loadProjectUponPageLoad();
        // const safety2 = await this.loadTaskForProject();
        var projectClient = new ProjectClient();
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const projectId = new URLSearchParams(window.location.search).get('projectId');
        const name = document.getElementById('new-project-name').value;
        const projectStatus = document.getElementById('status').value;
        const projectDescription = document.getElementById('new-project-description').value;

        var end = null;
        if (document.getElementById('endDate').value != "") { end = this.convertDateToUTC(document.getElementById('endDate').value); }
        if ((end != null)) { end = this.getEpochMillis(end); }
        var endDate = null;

        var endDate = new Date(0);
        endDate.setUTCSeconds(end);
        endDate = endDate.toUTCString()

        var date = new Date(endDate);
        date = date.getTime()
        var dateString = date.toString().substr(0, 10);

        if(end == null) {
            dateString = null
        }

        console.log(dateString);
        const taskList = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);
        var completionPercentage = this.dataStore.get(SEARCH_RESULTS_KEY);
        completionPercentage = completionPercentage.completionPercentage;
        var creationDate = this.dataStore.get(SEARCH_RESULTS_KEY);
        creationDate = creationDate.creationDate;
        if (orgId && projectId && projectStatus) {
            const results = await projectClient.updateProject(completionPercentage, creationDate, dateString, name, orgId, projectDescription, projectId, projectStatus, taskList).then(response => {
            }).catch(e => {
                console.log(e);
            });;
        }
    }

    async loadTaskForProject() {
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);
        const iterableList = searchResults.taskList;
        var taskIds = new Array();
        if (searchResults.taskList) {
            for (const res of iterableList) {
                taskIds.push(res.taskId);
            }
        }

        var taskList = new Array();
        if (orgId && taskIds) {
            for (const id of taskIds) {
                const results = await this.taskClient.getSingleTask(orgId, id);
                taskList.push(results);
            }
            this.taskDataStore.setState({
                [TASK_SEARCH_CRITERIA_KEY]: orgId,
                [TASK_SEARCH_RESULTS_KEY]: taskList,
            });
        } else {
            this.taskDataStore.setState(EMPTY_DATASTORE_STATE);
        }

    }

    async loadProjectUponPageLoad() {

        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const projectId = new URLSearchParams(window.location.search).get('projectId');

        if (orgId && projectId) {
            const results = await this.projectClient.getProject(orgId, projectId);
            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: projectId,
                [SEARCH_RESULTS_KEY]: results,
            });
            this.loadTaskForProject();
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }
    }

    /**
     * Pulls search results from the datastore and displays them on the html page.
     */
    displaySearchResults() {
        const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        const searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const searchResultsContainer = document.getElementById('search-results-container');
        const searchCriteriaDisplay = document.getElementById('search-criteria-display');
        const searchResultsDisplay = document.getElementById('search-results-display');

        if (searchCriteria === '') {
            searchResultsContainer.classList.add('hidden');
            searchCriteriaDisplay.innerHTML = '';
            searchResultsDisplay.innerHTML = '';
        } else {
            searchResultsContainer.classList.remove('hidden');
            searchResultsDisplay.innerHTML = this.getHTMLForProject(searchResults);
        }
    }




    async populateTable() {
        var preloads = document.getElementsByClassName('preload');
        for (var i = 0; i < preloads.length; i++) {
            preloads[i].hidden = false;
        }

        var table = document.getElementById("task-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var taskList = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);
        const projectId = new URLSearchParams(window.location.search).get('projectId');

        for (const task of taskList) {
            var row = newTableBody.insertRow();
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);
            var cell5 = row.insertCell(4);
            var cell6 = row.insertCell(5);

            cell1.innerHTML = '<a href="taskDetail.html?orgId=' + task.orgId + '&taskId=' + task.taskId + '&projectId=' + projectId + "\">" + task.name + '</a>'
            cell2.innerHTML = task.assignee;
            cell3.innerHTML = task.completed;
            cell4.innerHTML = task.hoursToComplete;
            cell5.innerHTML = task.taskNotes;

            var deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete Task';
            deleteButton.className = 'task-delete-button';
            deleteButton.setAttribute('data-task-id', task.taskId);
            deleteButton.addEventListener('click', (event) => {
                var taskId = event.target.getAttribute('data-task-id');
                this.deleteTaskFromProject(taskId);
            });

            cell6.appendChild(deleteButton);
        }

        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async deleteTaskFromProject(taskId) {
        await this.deleteTask(taskId);
        var projectClient = new ProjectClient();
        //const searchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);
        var searchResults = this.dataStore.get(SEARCH_RESULTS_KEY);

        const completionPercentage = searchResults.completionPercentage;
        const creationDate = searchResults.creationDate;
        const endDate = searchResults.endDate;
        const name = searchResults.name;
        const orgId = searchResults.orgId;
        const projectDescription = searchResults.projectDescription;
        const projectId = searchResults.projectId;
        const projectStatus = searchResults.projectStatus;

        const taskList = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);

        if (projectId && orgId) {
            const results = await projectClient.updateProject(completionPercentage, creationDate, endDate, name, orgId, projectDescription, projectId, projectStatus, taskList).then(response => {
            }).catch(e => {
                console.log(e);
            });;
        }
    }


    async deleteTask(taskId) {
        var taskClient = new TaskClient();
        const orgId = new URLSearchParams(window.location.search).get('orgId');

        if (orgId && taskId) {

            const results = await taskClient.deleteTask(orgId, taskId).then(response => {

                const searchCriteria = this.taskDataStore.get(TASK_SEARCH_CRITERIA_KEY);
                var searchResults = this.taskDataStore.get(TASK_SEARCH_RESULTS_KEY);
                var index = 0;
                if (searchResults != null) {
                    for (const task of searchResults) {
                        if (task.taskId === taskId) { break; } else { index = index + 1; }
                    }
                    searchResults.splice(index, 1);
                }
                this.taskDataStore.setState({
                    [TASK_SEARCH_CRITERIA_KEY]: searchCriteria,
                    [TASK_SEARCH_RESULTS_KEY]: searchResults,
                });
                return response;
            }).catch(e => {
                console.log(e);
            });;
        }
    }


    getHTMLForProject(searchResults) {
        if (searchResults.length === 0) {
            return '<h4>No results found</h4>';
        }

        const editNameField = document.getElementById('new-project-name');
        const editStatusField = document.getElementById('status');
        const editDescriptionField = document.getElementById('new-project-description');
        const editEndDateField = document.getElementById('endDate');



        editNameField.value = searchResults.name;
        editStatusField.value = searchResults.projectStatus;
        editDescriptionField.value = searchResults.projectDescription;

        if (searchResults.endDate != null) {
            var convertedEndDate = searchResults.endDate;
            var date = new Date(convertedEndDate * 1000);
            date = date.toISOString().slice(0, 10).toString();
            editEndDateField.value = date;
        }


        let html = '<table><tr><th>Project Name</th><th>Completion Percentage</th> <th>Status</th><th>Creation Date</th><th>End Date</th><th>Description</th></tr>';
        const res = searchResults;
        var endDate = res.endDate;
        if (endDate != null) {
            endDate = this.convertToUTC(res.endDate);
        } else { endDate = '' }

        html += `
        <tr>
            <td>${res.name}</td>
            <td>${Math.floor((res.completionPercentage) * 100) + "\%"}</td>
            <td>${res.projectStatus}</td>
            <td>${this.convertToUTC(res.creationDate).toString().substr(0, 16)}</td>
            <td>${endDate.toString().substr(0, 16)}</td>
            <td>${res.projectDescription}</td>
        </tr>`;

        html += '</table>';

        return html;
    }

    convertToUTC(date) {
        var d = new Date(0);
        d.setUTCSeconds(date)
        return d.toUTCString()
    }

    convertDateToUTC(test) {
        var year = test.substring(0, 4);
        var month = test.substr(5, 2) - 1;
        var day = test.substr(8, 2);
        var utcDate1 = new Date(Date.UTC(year, month, day));
        return utcDate1.toUTCString();
    }


    getEpochMillis(UTCStr) {
        var someDate = new Date(UTCStr);
        someDate = someDate.getTime()
        return someDate;
    };

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const projectDetailScript = new ProjectDetailScript();
    projectDetailScript.mount();
};

window.addEventListener('DOMContentLoaded', main);