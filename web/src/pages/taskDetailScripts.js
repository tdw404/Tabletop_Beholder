
import TaskClient from '../api/taskClient';
import UserRoleClient from '../api/userRoleClient';
import MaterialsClient from '../api/materialsClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";
import { AxiosError } from 'axios';

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const ORG_ID_KEY = 'organization-id-key';
const TASK_ID_KEY = 'task-id-key';
const PROJECT_ID_KEY = 'project-id-key;'
const MATERIAL_LIST_KEY = 'material-list-key';
const TASK_OBJECT_KEY = 'task-object-key';
const ASSIGNEE_LIST_KEY = 'assignee-list-key';
const USER_OBJECT_KEY = 'user-object-key';
const FILTERED_ASSIGNEE_KEY = 'filtered-assignee-key';
const SELECTED_MATERIAL_KEY = 'selected-material-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [ORG_ID_KEY]: '',
    [PROJECT_ID_KEY]: '',
    [TASK_ID_KEY]: '',
    [MATERIAL_LIST_KEY]: [],
    [TASK_OBJECT_KEY]: '',
    [ASSIGNEE_LIST_KEY]: [],
    [USER_OBJECT_KEY]: '',
    [FILTERED_ASSIGNEE_KEY]: [],
    [SELECTED_MATERIAL_KEY]: -1,
};



class TaskDetailScripts extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'startupActivities', 'populateTable', 'populateAssigneeList', 'populateMaterialList', 'saveButton', 'cancelButton', 'reactivateButton', 'completeButton', 'plusButton', 'minusButton', 'removeButton', 'addMaterial'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
    }


    mount() {
        this.header.addHeaderToPage();
        this.taskClient = new TaskClient();
        this.userRoleClient = new UserRoleClient();
        this.materialsClient = new MaterialsClient();
        this.startupActivities();
    }

    async startupActivities() {

        if (await this.userRoleClient.verifyLogin()) {
            const orgId = new URLSearchParams(window.location.search).get('orgId');
            this.dataStore.set([ORG_ID_KEY], orgId)
            const taskId = new URLSearchParams(window.location.search).get('taskId');
            this.dataStore.set([TASK_ID_KEY], taskId)
            this.dataStore.set([PROJECT_ID_KEY], new URLSearchParams(window.location.search).get('projectId'))
            const{email, name} = await this.userRoleClient.getIdentity().then(result => result);
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            const task = await this.taskClient.getSingleTask(orgId, taskId);
            this.dataStore.set([TASK_OBJECT_KEY], task);
            await this.populateAssigneeList();
            document.getElementById('name').value = task.name;
            if(task.completed == true) {
                document.getElementById('completed').value = "Yes"
            } else {
                document.getElementById('completed').value = "No"
            }
            document.getElementById('hours').value = task.hoursToComplete
            if(task.startTime != null) { document.getElementById('startTime').value = (new Date(task.startTime *1000)).toDateString() }
            if(task.endTime != null) { document.getElementById('endTime').value = (new Date(task.endTime *1000)).toDateString() }
            document.getElementById('taskNotes').value = task.taskNotes
            if (task.startTime != null) {
                const startDate = new Date(task.startTime *1000)
                document.getElementById('startTime').value = startDate.toDateString()
            }
            if (task.stopTime != null) {
                const endDate = new Date(task.stopTime *1000)
                document.getElementById('endTime').value = endDate.toDateString()
            }
            this.dataStore.set([USER_OBJECT_KEY], await this.userRoleClient.getUserRole(email, orgId))
            if (this.dataStore.get(USER_OBJECT_KEY).jobRole == "Manager") {
                document.getElementById('name').removeAttribute('disabled')
                document.getElementById('users').removeAttribute('disabled')
                document.getElementById('hours').removeAttribute('disabled')
            }

            await this.populateMaterialList();
            await this.populateTable();
             var preloads = document.getElementsByClassName('preload')
             for (var i= 0; i < preloads.length; i++) {
                 preloads[i].hidden=false
             }
             document.getElementById('loading').hidden=true;
             document.getElementById('save-btn').hidden=false;
             document.getElementById('cancel-btn').hidden=false;
             document.getElementById('remove-btn').hidden=false;
             document.getElementById('plus-btn').hidden=false;
             document.getElementById('minus-btn').hidden=false;

             if(task.completed) {
                document.getElementById('uncomplete-btn').hidden=false;
             } else {
                document.getElementById('complete-btn').hidden=false;
             }
            document.getElementById('save-btn').addEventListener('click', await this.saveButton)
            document.getElementById('cancel-btn').addEventListener('click', await this.cancelButton)
            document.getElementById('uncomplete-btn').addEventListener('click', await this.reactivateButton)
            document.getElementById('complete-btn').addEventListener('click', await this.completeButton)
            document.getElementById('plus-btn').addEventListener('click', await this.plusButton)
            document.getElementById('minus-btn').addEventListener('click', await this.minusButton)
            document.getElementById('remove-btn').addEventListener('click', await this.removeButton)
            document.getElementById('materials').addEventListener('change', await this.addMaterial)
        } else {
            window.location.href = "index.html"
        }
    }

    async populateAssigneeList() {
        var assigneeList = await this.userRoleClient.getUsersForOrg(this.dataStore.get([ORG_ID_KEY]));
        assigneeList = assigneeList.filter((user) => user.roleStatus == 'Active')
        this.dataStore.set([FILTERED_ASSIGNEE_KEY], assigneeList)
        const select = document.getElementById('users');
        for (const user of assigneeList) {
            var opt = document.createElement('option');
            opt.innerText = user.displayName.concat(" ~ ", user.jobRole);
            select.appendChild(opt);
        }
        const assignee = this.dataStore.get([TASK_OBJECT_KEY]).assignee;
        if (assignee !== null) {

            for (let i=0; i < assigneeList.length; i++) {
                if (assigneeList[i].userEmail === assignee) {
                    select.selectedIndex = i + 1;
                }
            }
        }
    }

    async populateMaterialList() {
        const materialList = await this.materialsClient.getMultipleMaterials(this.dataStore.get(ORG_ID_KEY))
        this.dataStore.set([MATERIAL_LIST_KEY], materialList)
        const select = document.getElementById('materials');
        if (materialList != null) {
            for (const material of materialList) {
                var opt = document.createElement('option');
                opt.innerText = material.name;
                select.appendChild(opt);
            }
        }
    }

    async populateTable() {
        var table = document.getElementById("material-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var materialList = this.dataStore.get(TASK_OBJECT_KEY).materialsList;
        if(materialList != null) {
        for(const material of materialList) {
                const fullMaterial = await this.materialsClient.getSingleMaterial(material.orgId, material.materialId)
                if (fullMaterial != null) {
                    var row = newTableBody.insertRow(-1);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    cell1.innerHTML = fullMaterial.name;
                    cell2.innerHTML = material.inventoryCount;
                    var createClickHandler = function(row, dataStore) {
                    return function() {
                        for (var i = 0; i < table.rows.length; i++){
                            table.rows[i].removeAttribute('class');
                        }
                        row.setAttribute('class','selectedRow');
                        dataStore.set([SELECTED_MATERIAL_KEY], row.rowIndex - 1)
                    }};
                row.onclick = createClickHandler(row, this.dataStore);
                
            }
        }}
        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async saveButton() {
        const task = this.dataStore.get(TASK_OBJECT_KEY)
        task.name = document.getElementById("name").value
        task.hoursToComplete = document.getElementById('hours').value
        task.taskNotes = document.getElementById('taskNotes').value
        if (document.getElementById('users').selectedIndex == 0) {
            task.assignee = null
        } else {
            task.assignee = this.dataStore.get([FILTERED_ASSIGNEE_KEY])[document.getElementById('users').selectedIndex - 1].userEmail
        }
        await this.taskClient.updateTask(
                task.orgId,
                task.taskId,
                task.assignee,
                task.completed,
                task.hoursToComplete,
                task.materialsList,
                task.name,
                task.startTime,
                task.stopTime,
                task.TaskNotes
        )
        this.cancelButton()    
    }

    async cancelButton() {
        var outboundPath = ""
        if(this.dataStore.get(PROJECT_ID_KEY) == null) {
            outboundPath = "assignedTaskList.html?orgId=" + this.dataStore.get(ORG_ID_KEY)
        } else {
            outboundPath = "projectDetail.html?orgId=" + this.dataStore.get(ORG_ID_KEY) + "&projectId=" + this.dataStore.get(PROJECT_ID_KEY)
        }
        window.location.href = outboundPath
    }

    async reactivateButton() {
        this.dataStore.get(TASK_OBJECT_KEY).completed = false
        this.dataStore.get(TASK_OBJECT_KEY).stopTime = ''
        this.saveButton()
    }

    async completeButton() {
        this.dataStore.get(TASK_OBJECT_KEY).completed = true
        var time = Date.now();
        var d = new Date(0);
        d.setUTCSeconds(time)
        d = d.toUTCString()
        var date = new Date(d);
        date = date.getTime()
        date.toString().substr(0, 10)
        this.dataStore.get(TASK_OBJECT_KEY).stopTime = date.toString().substr(0, 10)
        this.saveButton()
    }

    async plusButton() {
        if(this.dataStore.get(SELECTED_MATERIAL_KEY) >= 0) {
            const task = this.dataStore.get(TASK_OBJECT_KEY)
            const index = this.dataStore.get(SELECTED_MATERIAL_KEY)
            var count = task["materialsList"][index]["inventoryCount"]
            count ++
            task["materialsList"][index]["inventoryCount"] = count
            document.getElementById('material-table').rows[index + 1].cells[1].innerHTML = count
        }
    }

    async minusButton() {
        if(this.dataStore.get(SELECTED_MATERIAL_KEY) >= 0) {
            const task = this.dataStore.get(TASK_OBJECT_KEY)
            const index = this.dataStore.get(SELECTED_MATERIAL_KEY)
            var count = task["materialsList"][index]["inventoryCount"]
            count --
            task["materialsList"][index]["inventoryCount"] = Math.max(count, 0)
            document.getElementById('material-table').rows[index + 1].cells[1].innerHTML = Math.max(count, 0)
        }
    }

    async removeButton() {
        if(this.dataStore.get(SELECTED_MATERIAL_KEY) >= 0) {
            const task = this.dataStore.get(TASK_OBJECT_KEY)
            const index = this.dataStore.get(SELECTED_MATERIAL_KEY)
            task["materialsList"].splice(1, index + 1)
            document.getElementById('material-table').deleteRow(index + 1)
        }
    }

    async addMaterial(){
        const task = this.dataStore.get(TASK_OBJECT_KEY)
        const material = this.dataStore.get(MATERIAL_LIST_KEY)[document.getElementById('materials').selectedIndex - 1]
        const orgId = material.orgId
        var jsonObj = {
                    orgId: material.orgId,
                    materialId: material.materialId,
                    inventoryCount: 1
                }
        if (task["materialsList"] != null) {
            task["materialsList"].splice(task["materialsList"].length, 0, jsonObj)
        } else {
            //  var matListObj = {
            //      materialsList: []
            //  }
             task["materialsList"] = new Array()
             task["materialsList"].push(jsonObj)
        }
        var table = document.getElementById("material-table");
        var tableBody = table.getElementsByTagName('tbody')[0];
        var row = tableBody.insertRow(-1);
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        cell1.innerHTML = material.name;
        cell2.innerHTML = 1;
        var createClickHandler = function(row, dataStore) {
        return function() {
            for (var i = 0; i < table.rows.length; i++){
                table.rows[i].removeAttribute('class');
            }
            row.setAttribute('class','selectedRow');
            dataStore.set([SELECTED_MATERIAL_KEY], row.rowIndex - 1)
        }}
        row.onclick = createClickHandler(row, this.dataStore);
        document.getElementById('materials').selectedIndex = 0
    }

}


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const taskDetailScripts = new TaskDetailScripts();
    taskDetailScripts.mount();
};

window.addEventListener('DOMContentLoaded', main);
