import AuthClient from "../api/authClient";
import EncounterClient from "../api/encounterClient"
import SessionClient from "../api/sessionClient"
import CreatureClient from "../api/creatureClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";
import { v4 as uuidv4 } from 'uuid';

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const ENCOUNTER_LIST_KEY = 'encounter-list';
const ENCOUNTER_MAP_KEY = 'encounter-map';
const SELECTED_ENCOUNTER_KEY = 'selected-encounter-key';
const SESSION_MAP_KEY = 'session-map-key';
const SELECTED_SESSION_KEY = 'selected-session-key';
const NEW_CREATURE_MAP_KEY = 'new-creature-map-key';
const SELECTED_NEW_CREATURE_KEY = 'selected-new-creature-key';
const SELECTED_CREATURE_KEY = 'selected-creature-key';
const CREATURE_MAP_KEY = 'creature-map-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [ENCOUNTER_LIST_KEY]: [],
    [ENCOUNTER_MAP_KEY]: '',
    [SELECTED_ENCOUNTER_KEY]: '',
    [SESSION_MAP_KEY]: '',
    [SELECTED_SESSION_KEY]: '',
    [NEW_CREATURE_MAP_KEY]: '',
    [SELECTED_NEW_CREATURE_KEY]: '',
    [SELECTED_CREATURE_KEY]: '',
    [CREATURE_MAP_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class EncounterLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.encounterClient = new EncounterClient();
        this.sessionClient = new SessionClient();
        this.creatureClient = new CreatureClient();
        this.bindClassMethods(['mount', 'startupActivities',
                                'populateTable', 'populateSessions',
                                'encounterRowClick', 'hideElements',
                                'showElements', 'createButton',
                                'deleteButton', 'createSessionButton',
                                'createSessionFinishButton', 'deleteSessionButton',
                                'updateButton', 'createFinishButton',
                                'addCreatureRowClick', 'addCreatureFinishButton',
                                'addCreatureButton', 'attachEventListeners',
                                'sortCreatureTable', 'creatureTablePopulate',
                                'mapToObj', 'creatureRowClick',
                                'updateCreatureButton', 'deleteCreatureButton'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.addNavbarToPage();
        this.startupActivities();
     };

    async startupActivities() {
        if (await this.client.verifyLogin()) {
            var{email, name} = await this.client.getIdentity().then(result => result);
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            this.dataStore.set([ENCOUNTER_LIST_KEY], await this.encounterClient.getMultipleEncounters());
            var encounterMap = new Map(this.dataStore.get([ENCOUNTER_LIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([ENCOUNTER_MAP_KEY], encounterMap);
            var sessionList = await this.sessionClient.getMultipleSessions();
            var sessionMap = new Map(sessionList.map((obj) => [obj.objectId, obj]));
            this.dataStore.set([SESSION_MAP_KEY], sessionMap)
            await this.populateTable();
            await this.populateSessions('session-search');
            await this.populateSessions('session-list');
            await this.attachEventListeners();
            this.showElements();
        } else {
            window.location.href = "index.html";
        }
    }

    async attachEventListeners() {
        document.getElementById('filter-session-btn').addEventListener('click', await this.populateTable);
        document.getElementById('delete-btn').addEventListener('click', await this.deleteButton);
        document.getElementById('new-session-btn').addEventListener('click', await this.createSessionButton);
        document.getElementById('create-session-finish-btn').addEventListener('click', await this.createSessionFinishButton);
        document.getElementById('delete-session-btn').addEventListener('click', await this.deleteSessionButton);
        document.getElementById('add-creature-btn').addEventListener('click', await this.addCreatureButton);
        document.getElementById('add-creature-finish-btn').addEventListener('click', await this.addCreatureFinishButton);
        document.getElementById('update-btn').addEventListener('click', await this.updateButton);
        document.getElementById('create-btn').addEventListener('click', await this.createButton);
        document.getElementById('create-finish-btn').addEventListener('click', await this.createFinishButton);
        document.getElementById('save-creature-btn').addEventListener('click', await this.updateCreatureButton);
        document.getElementById('delete-creature-btn').addEventListener('click', await this.deleteCreatureButton);
        document.getElementById('encounter-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.encounterRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('add-creature-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.addCreatureRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('creature-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.creatureRowClick(event.target.parentNode.dataset.id)}});
    }

    async populateTable() {
            var table = document.getElementById("encounter-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var encounterList = this.dataStore.get(ENCOUNTER_LIST_KEY);
            encounterList.sort((a, b) => a.objectName.localeCompare(b.objectName));
            var sessionSearch = document.getElementById('session-search').value;
            for(var encounter of encounterList) {
                if (
                    (sessionSearch == '0' || encounter.sessionId == sessionSearch)
                ) {

                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', encounter.objectId);
                    row.setAttribute('data-id', encounter.objectId);
                    var cell1 = row.insertCell(0);
                    cell1.innerHTML = encounter.objectName;
                }
            }
            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async populateSessions(element) {
        var dropDown = document.getElementById(element);
        for (var i = dropDown.options.length; i > 1; i--) {
            dropDown.options.remove(i);
        }
        for (var [key, value] of this.dataStore.get(SESSION_MAP_KEY)) {
            var option = document.createElement('option');
                option.value = key;
                option.innerHTML = value.objectName;
                option.setAttribute('dataset-id', key);
                option.classList.add("session-dropdown");
                dropDown.appendChild(option);
        }
    }

    async encounterRowClick(encounterId) {
        var encounter = this.dataStore.get(ENCOUNTER_MAP_KEY).get(encounterId);
        this.dataStore.set([SELECTED_ENCOUNTER_KEY], encounter);
        var table = document.getElementById('encounter-table');
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(encounterId).setAttribute('class','selectedRow');
        document.getElementById('encounterNameBig').innerText = encounter.objectName;
        document.getElementById('objectName').value = encounter.objectName;
        document.getElementById('currentTurn').value = encounter.encounterTurn;
        document.getElementById('session-list').value = encounter.sessionId;
        this.creatureTablePopulate();
    }

    creatureRowClick(encounterCreatureId) {
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId);
        this.dataStore.set([SELECTED_CREATURE_KEY], creature);
        document.getElementById('edit-creature-name').value = creature.encounterCreatureName;
        document.getElementById('edit-creature-objectName').value = creature.objectName;
        document.getElementById('edit-creature-hp').value = creature.currentHitPoints;
        if (creature.dead) {
            document.getElementById('edit-creature-status').value = 'Dead';
        } else if (creature.knockedOut) {
            document.getElementById('edit-creature-status').value = 'Unconscious';
        } else {
            document.getElementById('edit-creature-status').value = 'Okay';
        }
        document.getElementById('edit-creature-size').value = creature.size;
        document.getElementById('edit-creature-type').value = creature.type;
        var myOffcanvas = document.getElementById('offcanvasEditCreature');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    creatureTablePopulate() {
        var creatureTable = document.getElementById(('creature-table'));
        var creatureBody = creatureTable.getElementsByTagName('tbody')[0]
        creatureBody.innerHTML = '';
        var encounter = this.dataStore.get(SELECTED_ENCOUNTER_KEY);
        if(encounter.creatureMap != null) {
            var creatureMap = new Map(Object.entries(encounter.creatureMap));
            for (var [key, value] of creatureMap) {
                var row = creatureBody.insertRow(-1);
                row.setAttribute('id', value.encounterCreatureId);
                row.setAttribute('data-id', value.encounterCreatureId);
                var cell0 = row.insertCell(0);
                var cell1 = row.insertCell(1);
                var cell2 = row.insertCell(2);
                var cell3 = row.insertCell(3);
                var cell4 = row.insertCell(4);
                var cell5 = row.insertCell(5);
                var cell6 = row.insertCell(6);
                cell0.innerHTML = value.encounterCreatureName;
                cell1.innerHTML = value.objectName;
                cell2.innerHTML = value.isPC
                                    ? "Yes"
                                    : "No";
                cell3.innerHTML = value.challengeRating;
                cell4.innerHTML = value.size;
                cell5.innerHTML = value.type;
                cell6.innerHTML = value.alignment;
            } else (
                creatureMap = '';
            )
            this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
            this.sortCreatureTable(creatureTable);
        }
    }

       sortCreatureTable(table) {
         var switching = true;
         while (switching) {
           switching = false;
           var rows = table.rows;
           for (var i = 1; i < (rows.length - 1); i++) {
             var shouldSwitch = false;
             var x = rows[i];
             var y = rows[i + 1];
             if (x.getElementsByTagName("td")[0].innerHTML > y.getElementsByTagName("td")[0].innerHTML) {
               shouldSwitch = true;
               break;
             }
           }
           if (shouldSwitch) {
             rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
             switching = true;
           }
         }
       }

    async deleteButton() {
        var objectId = this.dataStore.get(SELECTED_ENCOUNTER_KEY).objectId;
        if(objectId != '') {
            this.hideElements();
            await this.encounterClient.deleteEncounter(objectId);
            location.reload();
        }
    }

    async deleteSessionButton() {
        this.hideElements();
        await this.sessionClient.deleteSession(document.getElementById('session-search').value);
        location.reload();
    }

    async updateButton() {
        this.hideElements();
        var encounter = this.dataStore.get(SELECTED_ENCOUNTER_KEY);
        encounter.objectName = document.getElementById('objectName').value;
        encounter.sessionId = document.getElementById('session-list').value;
        await this.encounterClient.updateEncounter(encounter);
        location.reload();
    }

    createButton() {
        this.populateSessions('newSession')
        document.getElementById('newSession').value = document.getElementById('session-search').value;
        if(document.getElementById('newSession').value == '0') { document.getElementById('newSession').value = 'none'; }
        var myOffcanvas = document.getElementById('offcanvasCreate');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async createFinishButton() {
        if(document.getElementById('newName').value != '') {
            var encounter = {};
            encounter.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
            encounter.objectName = document.getElementById('newName').value;
            encounter.sessionId = document.getElementById('newSession').value;
            if (encounter.sessionId == '0') { encounter.sessionId = 'none' };
            this.hideElements();
            document.getElementById('close-btn').click()
            var newEncounter = await this.encounterClient.createEncounter(encounter);
            this.dataStore.set([ENCOUNTER_LIST_KEY], await this.encounterClient.getMultipleEncounters());
            var encounterMap = new Map(this.dataStore.get([ENCOUNTER_LIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([ENCOUNTER_MAP_KEY], encounterMap);
            await this.populateTable();
            this.showElements();
            this.encounterRowClick(newEncounter.objectId);
        }
    }

    createSessionButton() {
            var myOffcanvas = document.getElementById('offcanvasCreateSession');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }

    async createSessionFinishButton() {
        var session = {};
        session.objectName = document.getElementById('new-session-name').value;
        try {
            this.hideElements();
            document.getElementById('close-session-create-btn').click()
            await this.sessionClient.createSession(session);
            location.reload();
        } catch (error) {
            this.showElements();
            document.getElementById('offcanvas-warn-body').innerText = "You already have a encounter with the name " + document.getElementById('objectName').value + " in your library."
            var myOffcanvas = document.getElementById('offcanvasWarn');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }
    }
    async addCreatureButton() {
        if(this.dataStore.get(SELECTED_ENCOUNTER_KEY) != '') {
            document.getElementById('spinner-new-creature').hidden = false;
            document.getElementById('add-creature-table').hidden = true;
            document.getElementById('add-creature-finish-btn').hidden = true;
            document.getElementById('new-creature-field').hidden = true;
            var myOffcanvas = document.getElementById('offcanvasNewCreature');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
            var newCreatureList = await this.creatureClient.getMultipleCreatures();
            newCreatureList.sort((a, b) => a.objectName.localeCompare(b.objectName));
            var newCreatureMap = new Map(newCreatureList.map((obj) => [obj.objectId, obj]));
            this.dataStore.set([NEW_CREATURE_MAP_KEY], newCreatureMap);
            var creatureTable = document.getElementById('add-creature-table');
            var creatureBody = creatureTable.getElementsByTagName('tbody')[0];
            creatureBody.innerHTML = '';
            for(var creature of newCreatureList) {
                var row = creatureBody.insertRow(-1);
               row.setAttribute('id', creature.objectId);
               row.setAttribute('data-id', creature.objectId);
               var cell0 = row.insertCell(0);
               var cell1 = row.insertCell(1);
               var cell2 = row.insertCell(2);
               var cell3 = row.insertCell(3);
               var cell4 = row.insertCell(4);
               var cell5 = row.insertCell(5);
               cell0.innerHTML = creature.objectName;
               cell1.innerHTML = creature.isPC
                                   ? "Yes"
                                   : "No";
               cell2.innerHTML = creature.challengeRating;
               cell3.innerHTML = creature.size;
               cell4.innerHTML = creature.type;
               cell5.innerHTML = creature.alignment;

               document.getElementById('spinner-new-creature').hidden = true;
               document.getElementById('add-creature-table').hidden = false;
               document.getElementById('add-creature-finish-btn').hidden = false;
               document.getElementById('new-creature-field').hidden = false;
            }
        }
    }

    addCreatureRowClick(objectId) {
        var newCreature = this.dataStore.get(NEW_CREATURE_MAP_KEY).get(objectId);
        this.dataStore.set([SELECTED_NEW_CREATURE_KEY], newCreature);
        var table = document.getElementById('add-creature-table')
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(objectId).setAttribute('class','selectedRow');
        document.getElementById('new-creature-name').value = newCreature.objectName;
    }

    async addCreatureFinishButton() {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        if (!creatureMap) { creatureMap = new Map() };
        var newCreature = this.dataStore.get(SELECTED_NEW_CREATURE_KEY);
        newCreature.encounterCreatureName = document.getElementById('new-creature-name').value
        newCreature.encounterCreatureId  = uuidv4();
        newCreature.knockedOut = false;
        newCreature.dead = false;
        newCreature.deathSaves = 3;
        newCreature.currentHitPoints = newCreature.hitPoints;
        creatureMap.set(newCreature.encounterCreatureId, newCreature);
        this.dataStore.get(SELECTED_ENCOUNTER_KEY).creatureMap = this.mapToObj(creatureMap);
        this.dataStore.set(CREATURE_MAP_KEY, creatureMap);
        this.creatureTablePopulate();
        document.getElementById('close-new-creature-btn').click();
    }

    async updateCreatureButton() {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        creature.encounterCreatureName = document.getElementById('edit-creature-name').value;
        document.getElementById('close-edit-creature-btn').click();
        creatureMap.set(creature.encounterCreatureId, creature);
        this.dataStore.set(CREATURE_MAP_KEY);
        this.dataStore.get(SELECTED_ENCOUNTER_KEY).creatureMap = this.mapToObj(creatureMap);
        this.creatureTablePopulate();
        document.getElementById('edit-creature-name').value = '';
        document.getElementById('edit-creature-objectName').value = '';
        document.getElementById('edit-creature-hp').value = '';
        document.getElementById('edit-creature-status').value = '';
        document.getElementById('edit-creature-size').value = '';
        document.getElementById('edit-creature-type').value = '';

    }

    async deleteCreatureButton() {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        document.getElementById('close-edit-creature-btn').click();
        creatureMap.delete(creature.encounterCreatureId);
        this.dataStore.set(CREATURE_MAP_KEY);
        this.dataStore.get(SELECTED_ENCOUNTER_KEY).creatureMap = this.mapToObj(creatureMap);
        this.creatureTablePopulate();
        document.getElementById('edit-creature-name').value = '';
        document.getElementById('edit-creature-objectName').value = '';
        document.getElementById('edit-creature-hp').value = '';
        document.getElementById('edit-creature-status').value = '';
        document.getElementById('edit-creature-size').value = '';
        document.getElementById('edit-creature-type').value = '';
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
     const encounterLibraryScripts = new EncounterLibraryScripts();
     encounterLibraryScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
