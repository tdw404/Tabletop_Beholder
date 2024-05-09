
import OrganizationClient from '../api/organizationClient';
import UserRoleClient from '../api/userRoleClient';
import Header from '../components/header';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const DISPLAY_ROLES_KEY = 'display-roles-list';
const ORG_LIST_KEY = 'organization-list-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [DISPLAY_ROLES_KEY]: [],
    [ORG_LIST_KEY]: [],
};



class NewRoleScripts extends BindingClass {
    constructor() {
        super();

        this.bindClassMethods(['mount', 'startupActivities', 'populateDropdown', 'submitButton', 'checkIfExists', 'createNewUserRole'], this);

        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.header = new Header(this.dataStore);
        console.log("newRoleScripts constructor");
    }


    mount() {

        this.header.addHeaderToPage();
        this.organizationClient = new OrganizationClient();
        this.userRoleClient = new UserRoleClient();
        document.getElementById('submit-btn').addEventListener('click', this.submitButton);
        this.startupActivities();
    }

    async startupActivities() {

        if (await this.organizationClient.verifyLogin()) {
            const{email, name} = await this.organizationClient.getIdentity().then(result => result);
            this.dataStore.set([DISPLAY_ROLES_KEY], await this.userRoleClient.getDisplayRolesForUser(email));
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            document.getElementById('displayName').value = name;
            this.populateDropdown();
            document.getElementById('greeting').hidden=false;
            document.getElementById('please-wait').hidden=true;
            document.getElementById('name-form').hidden=false;
            document.getElementById('organizations').hidden=false;
            document.getElementById('jobRoles').hidden=false;
            document.getElementById('submit-btn').hidden=false;
            document.getElementById('cancel-btn').hidden=false;
        } else {
            window.location.href = "index.html"
        }
    }

    async populateDropdown() {
        const select = document.getElementById('organizations');
        const orgList = await this.organizationClient.getAllOrgs();
        this.dataStore.set(ORG_LIST_KEY, orgList);
        for (const org of orgList) {
            var opt = document.createElement('option');
            opt.innerText = org.displayName;
            select.appendChild(opt);
        }
    }

    async submitButton() {
        if(document.getElementById('organizations').selectedIndex == 0 || document.getElementById('jobRoles').selectedIndex == 0) {
            alert("Error: please fill out all fields!")
            return;
        }
        const orgId = this.dataStore.get(ORG_LIST_KEY)[document.getElementById('organizations').selectedIndex -1].orgId
          if(await this.checkIfExists(orgId) == true) {
                alert("Error: you already have a role at this organization.")
        } else {
           await this.createNewUserRole(orgId);
           alert("Role submitted!")
           window.location.href = "index.html";
        }
    }

    async checkIfExists(orgId) {
        for(const checkId of this.dataStore.get(DISPLAY_ROLES_KEY)) {
            if (checkId.orgId == orgId) {
                return true;
            }
        } return false;
       }

    async createNewUserRole(orgId) {
        await this.userRoleClient.createUserRole(
            this.dataStore.get(COGNITO_EMAIL_KEY),
            orgId,
            document.getElementById('jobRoles').value,
            document.getElementById('displayName').value,
            "Pending"
        )
        
    }

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const newRoleScripts = new NewRoleScripts();
    newRoleScripts.mount();
};

window.addEventListener('DOMContentLoaded', main);
