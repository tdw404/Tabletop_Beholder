import Authenticator from '../api/authenticator';
import ProjectClient from '../api/projectClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const SEARCH_CRITERIA_KEY = 'search-criteria';
const SEARCH_RESULTS_KEY = 'search-results';
const EMPTY_DATASTORE_STATE = {
    [SEARCH_CRITERIA_KEY]: '',
    [SEARCH_RESULTS_KEY]: [],
};


class ViewProjects extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['getEpochMillis', 'convertToUTC', 'mount', 'getCurrentUserEmail', 'search', 'displaySearchResults', 'getHTMLForSearchResults', 'convertDateToUTC'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        this.userInfo = new Authenticator();
        this.userInfo = this.userInfo.getCurrentUserInfo;
        this.dataStore.addChangeListener(this.displaySearchResults);
    }

    /**
    * Add the header to the page and load the ProjectClient.
    */
    mount() {
        document.getElementById('search-btn').addEventListener('click', this.search);
        document.getElementById('create-btn').addEventListener('click', this.createProject);


        this.header.addHeaderToPage();
        this.client = new ProjectClient();
        this.email = this.getCurrentUserEmail();
        this.loadProjectUponPageLoad();
    }

    async loadProjectUponPageLoad() {

        const searchCriteria = new URLSearchParams(window.location.search).get('orgId');
        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);

        // If the user didn't change the search criteria, do nothing
        if (previousSearchCriteria === searchCriteria) {
            return;
        }

        if (searchCriteria) {
            const results = await this.client.getProjects(searchCriteria);
            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
        } else {
            this.dataStore.setState(EMPTY_DATASTORE_STATE);
        }
    }


    /**
     * Uses the client to obtain the Users email and Name;
     * @returns User Email
     */
    async getCurrentUserEmail() {
        const { email, name } = await this.client.getIdentity().then(result => result);
        console.log("User email variable created: " + email);
        return email;
    }


    async createProject() {
        var createClient = new ProjectClient();
        const orgId = new URLSearchParams(window.location.search).get('orgId');
        const name = document.getElementById('new-project-name').value;

        var time = Date.now();
        var d = new Date(0);
        d.setUTCSeconds(time)
        d = d.toUTCString()

        var date = new Date(d);
        date = date.getTime()

        var dateString = date.toString().substr(0, 10);
        console.log(dateString);
        if (name && orgId && date) {
            const results = await createClient.createProject(name, dateString, orgId).then(response => {
                console.log(response);
            }).catch(e => {
                console.log(e);
            });;
        }

    }


    /**
         * Uses the client to perform the search, 
         * then updates the datastore with the criteria and results.
         * @param evt The "event" object representing the user-initiated event that triggered this method.
         */
    async search(evt) {
        // Prevent submitting the from from reloading the page.
        evt.preventDefault();
        const searchCriteria = new URLSearchParams(window.location.search).get('orgId');

        const previousSearchCriteria = this.dataStore.get(SEARCH_CRITERIA_KEY);



        if (searchCriteria) {
            const results = await this.client.getProjects(searchCriteria);
            this.dataStore.setState({
                [SEARCH_CRITERIA_KEY]: searchCriteria,
                [SEARCH_RESULTS_KEY]: results,
            });
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
            //searchCriteriaDisplay.innerHTML = `"${searchCriteria}"`;
            searchResultsDisplay.innerHTML = this.getHTMLForSearchResults(searchResults);
        }
    }

    /**
     * Create appropriate HTML for displaying searchResults on the page.
     * @param searchResults An array of playlists objects to be displayed on the page.
     * @returns A string of HTML suitable for being dropped on the page.
     */
    getHTMLForSearchResults(searchResults) {
        if (searchResults.length === 0) {
            return '<h4>No results found</h4>';
        }

        var start = null;
        var end = null;
        if (document.getElementById('start').value != "") { start = this.convertDateToUTC(document.getElementById('start').value); }
        if (document.getElementById('end').value != "") { end = this.convertDateToUTC(document.getElementById('end').value); }
        const status = document.getElementById('status').value;
        let html = '<table><tr><th>Project Name</th><th>Completion Percentage</th> <th>Status</th><th>CreationDate</th><th>EndDate</th><th>Description</th></tr>';
        for (const res of searchResults) {
            var projectDate = this.convertToUTC(res.creationDate);
            var endDate = res.endDate;
            if (endDate != null) {
                endDate = this.convertToUTC(res.endDate);
            } else { endDate = '' }
            console.log("PROJECTDATE = " + projectDate);
            if ((res.projectStatus != status)) { continue; }
            if ((start != null)) { if (this.getEpochMillis(start) > this.getEpochMillis(projectDate)) { continue; } }
            if ((end != null)) { if (this.getEpochMillis(end) < this.getEpochMillis(projectDate)) { continue; } }
            html += `
        <tr>
            <td>
                <a href="projectDetail.html?orgId=${res.orgId}&projectId=${res.projectId}">${res.name}</a>
            </td>
            <td>${Math.floor((res.completionPercentage) * 100)+ "\%"}</td>
            <td>${res.projectStatus}</td>
            <td>${this.convertToUTC(res.creationDate).toString().substr(0, 16)}</td>
            <td>${endDate.toString().substr(0, 16)}</td>
            <td>${res.projectDescription}</td>
        </tr>`;
        }
        html += '</table>';

        return html;
    }

    convertDateToUTC(test) {
        var year = test.substring(0, 4);
        var month = test.substr(5, 2) - 1;
        var day = test.substr(8, 2);
        var utcDate1 = new Date(Date.UTC(year, month, day));
        return utcDate1.toUTCString();
    }

    convertToUTC(date) {
        var d = new Date(0);
        d.setUTCSeconds(date)
        return d.toUTCString()
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
    const viewProjects = new ViewProjects();
    viewProjects.mount();
};

window.addEventListener('DOMContentLoaded', main);