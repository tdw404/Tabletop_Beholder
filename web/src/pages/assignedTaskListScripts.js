
import TaskClient from '../api/taskClient';
import UserRoleClient from '../api/userRoleClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const ORG_ID_KEY = 'organization-id-key';
const TASKLIST_KEY = 'tasklist-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [ORG_ID_KEY]: '',
    [TASKLIST_KEY]: []
};



class AssignedTaskListScripts extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'startupActivities', 'populateTable'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        console.log("newRoleScripts constructor");
    }


    mount() {
        this.header.addHeaderToPage();
        this.taskClient = new TaskClient();
        this.userRoleClient = new UserRoleClient();
        this.startupActivities();
    }

    async startupActivities() {

        if (await this.userRoleClient.verifyLogin()) {
            const orgId = new URLSearchParams(window.location.search).get('orgId');
            this.dataStore.set([ORG_ID_KEY], orgId)
            const{email, name} = await this.userRoleClient.getIdentity().then(result => result);
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            this.dataStore.set([TASKLIST_KEY], await this.taskClient.getTasksForAssignee(orgId, email));

            await this.populateTable();
            var preloads = document.getElementsByClassName('preload')
            for (var i= 0; i < preloads.length; i++) {
                preloads[i].hidden=false
            }
            document.getElementById('loading').hidden=true;
            document.getElementById('view-btn').hidden=false;
            document.getElementById('completed').addEventListener('change', await this.populateTable)
            document.getElementById('start').addEventListener('change', await this.populateTable)
            document.getElementById('end').addEventListener('change', await this.populateTable)
        } else {
            window.location.href = "index.html"
        }
    }

    async populateTable() {
        var table = document.getElementById("task-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var taskList = this.dataStore.get(TASKLIST_KEY);
        for(const task of taskList) {
            const date = new Date(task.startTime *1000)
            if (
                (task.completed==false || document.getElementById('completed').checked==true) &&
                (date >= new Date(document.getElementById('start').value)) &&
                (date <= new Date(document.getElementById('end').value))
            ) {

                var row = newTableBody.insertRow(0);
                var cell1 = row.insertCell(0); 
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                var cell4 = row.insertCell(3);
                cell1.innerHTML = task.name;
                cell2.innerHTML = task.hoursToComplete;
                cell3.innerHTML = date.toDateString();
                cell4.innerHTML = task.completed;
                var createClickHandler = function(row) {
                    return function() {
                        for (var i = 0; i < table.rows.length; i++){
                            table.rows[i].removeAttribute('class');
                        }
                        row.setAttribute('class','selectedRow')
                        document.getElementById('view-btn').setAttribute('href', 'taskDetail.html?orgId=' + new URLSearchParams(window.location.search).get('orgId') + "&taskId=" + task.taskId);
                    };
                };
                row.onclick = createClickHandler(row);
            }
        }
        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

}


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const assignedTaskListScripts = new AssignedTaskListScripts();
    assignedTaskListScripts.mount();
};

window.addEventListener('DOMContentLoaded', main);