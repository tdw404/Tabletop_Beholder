import AuthClient from "../api/authClient";
import SessionClient from "../api/sessionClient";
import EncounterClient from "../api/encounterClient";
import RunEncounterClient from "../api/runEncounterClient";
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SESSION_LIST_KEY = 'session-list-key';
const SELECTED_SESSION_ID_KEY = 'selected-session-id-key';
const ENCOUNTER_LIST_KEY = 'encounter-list-key';
const SELECTED_ENCOUNTER_ID_KEY = 'selected-encounter-id-key';
const ENCOUNTER_KEY = 'encounter-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SESSION_LIST_KEY]: '',
    [SELECTED_SESSION_ID_KEY]: '0',
    [ENCOUNTER_LIST_KEY]: '',
    [SELECTED_ENCOUNTER_ID_KEY]: '',
    [ENCOUNTER_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class RunEncounterScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.sessionClient = new SessionClient();
        this.encounterClient = new EncounterClient();
        this.runClient = new RunEncounterClient();
        this.bindClassMethods(['mount', 'startupActivities',
                                'showElements', 'hideElements',
                                'mapToObj', 'attachEventListeners',
                                'populateEncounters', 'launchEncounter',
                                'hideElementsPlay', 'showElementsPlay'
                                ], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.provideBars();
        this.startupActivities();
     };

    async startupActivities() {
        if (await this.client.verifyLogin()) {
            const sessionList = await this.sessionClient.getMultipleSessions();
            this.dataStore.set([SESSION_LIST_KEY], sessionList);
            this.populateSessions();
            this.attachEventListeners();
            this.showElements();
        }
    }

    attachEventListeners() {
    document.getElementById('launch-session-btn').addEventListener('click', this.launchEncounter);
        document.getElementById('session-list').addEventListener('change', (event) => {
                                                    if (event.target.closest('select')) {this.populateEncounters(event.target.value)}});
    }

    populateSessions() {
        var dropDown = document.getElementById("session-list");
        for (var i = dropDown.options.length; i > 0; i--) {
            dropDown.options.remove(i);
        }
        for (var session of this.dataStore.get(SESSION_LIST_KEY)) {
            var option = document.createElement('option');
                option.value = session.objectId;
                option.innerHTML = session.objectName;
                option.setAttribute('dataset-id', session.objectId);
                option.classList.add("session-dropdown");
                dropDown.appendChild(option);
        }
    }

    async populateEncounters(sessionId) {
        if(sessionId != this.dataStore.get(SELECTED_SESSION_ID_KEY)) {
            var dropDown = document.getElementById("encounter-list");
            this.dataStore.set([SELECTED_SESSION_ID_KEY], sessionId);
            this.hideElements();
            var encounterList = await this.runClient.getEncounterList(sessionId);
            this.dataStore.set([ENCOUNTER_LIST_KEY], encounterList);
            for (var i = dropDown.options.length; i > 0; i--) {
                        dropDown.options.remove(i);
                    }
            for (var encounter of encounterList) {
                var option = document.createElement('option');
                    option.value = encounter.objectId;
                    option.innerHTML = encounter.objectName;
                    option.setAttribute('dataset-id', encounter.objectId);
                    option.classList.add("encounter-dropdown");
                    dropDown.appendChild(option);
            };
            this.showElements();
        }
    }

    async launchEncounter() {
        if(document.getElementById('encounter-list').value != '0') {
            this.hideElements();
            var warned = false;
            var encounter = await this.encounterClient.getSingleEncounter(document.getElementById('encounter-list').value, (error) => {
               document.getElementById('offcanvas-warn-body').innerText = error.message;
               var myOffcanvas = document.getElementById('offcanvasWarn');
               var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
               bsOffcanvas.show();
               this.showElements();
               warned = true;
            });
            if(!warned) {
                this.dataStore.set([ENCOUNTER_KEY], encounter);
                document.getElementById('encounterNameBig').innerHTML = encounter.objectName;
                var myOffcanvas = document.getElementById('offcanvasInitiative');
                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                bsOffcanvas.show();
            }
        }
    }

    showElements() {
        var show = document.getElementsByClassName('hide-while-loading');
                for(var i = 0; i < show.length; i++) {
                    show[i].hidden = false;
                }
        var hide = document.getElementsByClassName('show-while-loading');
                        for(var i = 0; i < hide.length; i++) {
                            hide[i].hidden = true;
                        }
    }

    hideElements() {
        var show = document.getElementsByClassName('show-while-loading');
                    for(var i = 0; i < show.length; i++) {
                        show[i].hidden = false;
                    }
            var hide = document.getElementsByClassName('hide-while-loading');
                            for(var i = 0; i < hide.length; i++) {
                                hide[i].hidden = true;
                            }
    }

    hideElementsPlay() {
        var hide = document.getElementsByClassName('hide-while-playing');
                        for(var i = 0; i < hide.length; i++) {
                            hide[i].hidden = true;
                        }
    }

    showElementsPlay() {
            var hide = document.getElementsByClassName('hide-while-playing');
                            for(var i = 0; i < hide.length; i++) {
                                hide[i].hidden = false;
                            }
        }

    mapToObj(map){
      const obj = {}
      for (let [k,v] of map)
        obj[k] = v
      return obj
    }
};


 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const runEncounterScripts = new RunEncounterScripts();
     runEncounterScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
