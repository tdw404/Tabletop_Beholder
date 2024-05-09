import UserRoleClient from '../api/userRoleClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const ORG_ID_KEY = 'organization-id-key';
const USERLIST_KEY = 'userlist-key';
const SELECTED_USER_KEY = 'selected-user-key'
const INDEX = 'index'
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [ORG_ID_KEY]: '',
    [USERLIST_KEY]: [],
    [SELECTED_USER_KEY]: '',
    [INDEX]: '',
};



class UserManagementScripts extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'startupActivities', 'populateTable', 'saveButton'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        console.log("newRoleScripts constructor");
    }


    mount() {
        this.header.addHeaderToPage();
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
            this.dataStore.set([USERLIST_KEY], await this.userRoleClient.getUsersForOrg(orgId))
            
            await this.populateTable();
            var preloads = document.getElementsByClassName('preload')
            for (var i= 0; i < preloads.length; i++) {
                preloads[i].hidden=false
            }
            document.getElementById('loading').hidden=true;
            document.getElementById('save-btn').hidden=false;
            document.getElementById('pending').addEventListener('change', await this.populateTable)
            document.getElementById('save-btn').addEventListener('click', await this.saveButton)
        } else {
            window.location.href = "index.html"
        }
    }

    async populateTable() {
        var table = document.getElementById("user-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var userList = this.dataStore.get(USERLIST_KEY);
        if(userList != null) {
        for(const user of userList) {
             if (user.roleStatus=="Pending" || document.getElementById('pending').checked==false) {
                 var row = newTableBody.insertRow(-1);
                 var cell1 = row.insertCell(0); 
                 var cell2 = row.insertCell(1);
                 var cell3 = row.insertCell(2);
                 var cell4 = row.insertCell(3);
                 cell1.innerHTML = user.userEmail;
                 cell2.innerHTML = user.displayName;
                 cell3.innerHTML = user.jobRole
                 cell4.innerHTML = user.roleStatus;
                 var createClickHandler = function(row, dataStore) {
                     return function() {
                         for (var i = 0; i < table.rows.length; i++){
                             table.rows[i].removeAttribute('class');
                         }
                         row.setAttribute('class','selectedRow')
                         document.getElementById('email').value = user.userEmail
                         document.getElementById('name').value = user.displayName
                         document.getElementById('jobRole').value = user.jobRole
                         document.getElementById('status').value = user.roleStatus
                         dataStore.set([SELECTED_USER_KEY],user)
                     };
                 };
                 row.onclick = createClickHandler(row, this.dataStore);
             }}}
         oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async saveButton() {
        var user = this.dataStore.get(SELECTED_USER_KEY)
        user.displayName = document.getElementById('name').value
        user.jobRole = document.getElementById('jobRole').value
        user.roleStatus = document.getElementById('status').value
        await this.userRoleClient.updateUserRole(
            user.userEmail,
            user.orgId,
            user.jobRole,
            user.displayName,
            user.roleStatus
        )
        var userList = this.dataStore.get(USERLIST_KEY)
        document.getElementById('loading').hidden = false
        document.getElementById('greeting').hidden = true
        document.getElementById('user-table').hidden = true
        document.getElementById('pending').hidden = true
        document.getElementById('pending-label').hidden = true
        await this.startupActivities()
        document.getElementById('loading').hidden = true
        document.getElementById('greeting').hidden = false
        document.getElementById('user-table').hidden = false
        document.getElementById('pending').hidden = false
        document.getElementById('pending-label').hidden = false
    }

}


/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const userManagementScripts = new UserManagementScripts();
    userManagementScripts.mount();
};

window.addEventListener('DOMContentLoaded', main);
