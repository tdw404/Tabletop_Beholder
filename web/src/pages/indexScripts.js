import EncounterClient from '../api/encounterClient';
import AuthClient from "../api/authClient";
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class IndexScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.bindClassMethods(['mount'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.provideBars();
        document.getElementById('mc-home-login').addEventListener('click', this.client.login);
        this.startupActivities();
     };

    async startupActivities() {
        if (await this.client.verifyLogin()) {
            document.getElementById('welcomeText').hidden = false;
            let id;
            try { id = localStorage.getItem('beholder:lastEncounter'); } catch {}
            if (!id) return;
            const encounter = await new EncounterClient().getSingleEncounter(id);
            const rosterSize = Object.keys(encounter?.creatureMap || {}).length;
            if (rosterSize && encounter.turnQueue?.length === rosterSize &&
                new Set(encounter.turnQueue).size === rosterSize && encounter.turnQueue.every(id => encounter.creatureMap[id])) {
                const link = document.getElementById('home-run');
                link.href = 'runEncounter.html?encounter=' + encodeURIComponent(id);
                link.textContent = 'Resume encounter';
                document.getElementById('mc-home-run-label').textContent = 'In progress';
                const name = document.getElementById('mc-home-encounter');
                name.textContent = encounter.objectName;
                name.hidden = false;
            }
        } else {
            document.getElementById('mc-home-sign-in').hidden = false;
        }
    }

};

 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const indexScripts = new IndexScripts();
     indexScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
