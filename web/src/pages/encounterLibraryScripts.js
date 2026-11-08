import {bindLibraryLayout, renderLibraryRow, showLibraryDetail} from '../components/libraryLayout';
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

 //TODO Block adding/removing creature from an encounter that has started
 //TODO clear CREATURE_MAP_KEY when clicking on row or
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
                                'creatureTablePopulate',
                                'mapToObj', 'creatureRowClick',
                                'updateCreatureButton', 'deleteCreatureButton',
                                'lockElements', 'unlockElements'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.provideBars();
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
        document.getElementById('mc-roster-search').addEventListener('input', event => {
            const term = event.target.value.trim().toLowerCase();
            document.querySelectorAll('#add-creature-table tbody tr').forEach(row => {
                row.hidden = !row.cells[0].textContent.toLowerCase().includes(term);
            });
        });
        document.getElementById('objectName').addEventListener('input', () => this.markEncounterDirty());
        document.getElementById('session-list').addEventListener('change', () => this.markEncounterDirty());
        window.addEventListener('beforeunload', event => {
            if (this.encounterDirty) {event.preventDefault(); event.returnValue = '';}
        });

        document.getElementById('mc-encounter-search').addEventListener('input', this.populateTable);
        document.getElementById('session-search').addEventListener('change', this.populateTable);
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
        bindLibraryLayout('encounter-table', this.encounterRowClick);
        document.getElementById('add-creature-table').addEventListener('click', (event) => {
            const row = event.target.closest('tbody tr[data-id]');
            if (row) this.addCreatureRowClick(row.dataset.id);
        });
        document.getElementById('creature-table').addEventListener('click', (event) => {
            const row = event.target.closest('tbody tr[data-id]');
            if (row) this.creatureRowClick(row.dataset.id);
        });
        const picker = document.getElementById('offcanvasNewCreature');
        picker.addEventListener('shown.bs.offcanvas', () => document.getElementById('mc-roster-search').focus());
        picker.addEventListener('hidden.bs.offcanvas', () => document.getElementById('add-creature-btn').focus({preventScroll: true}));
    }

    async populateTable() {
            var table = document.getElementById("encounter-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var encounterList = this.dataStore.get(ENCOUNTER_LIST_KEY);
            encounterList.sort((a, b) => a.objectName.localeCompare(b.objectName));
            var sessionSearch = document.getElementById('session-search').value;
            const nameSearch = document.getElementById('mc-encounter-search').value.trim().toLowerCase();
            for(var encounter of encounterList) {
                if (
                    (sessionSearch == '0' || encounter.sessionId == sessionSearch) && encounter.objectName.toLowerCase().includes(nameSearch)
                ) {

                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', encounter.objectId);
                    row.setAttribute('data-id', encounter.objectId);
                    var cell1 = row.insertCell(0);
                    renderLibraryRow(cell1, encounter.objectName, [this.dataStore.get(SESSION_MAP_KEY).get(encounter.sessionId)?.objectName || 'No session', Object.keys(encounter.creatureMap || {}).length + ' in roster']);
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
        if (this.savingEncounter || (this.encounterDirty && !window.confirm('Discard unsaved encounter changes?'))) return;
        if (!this.dataStore.get(ENCOUNTER_MAP_KEY).has(encounterId)) return;
        this.encounterDirty = false;
        document.getElementById('mc-encounter-feedback').textContent = '';

        this.dataStore.set([CREATURE_MAP_KEY], '');
        const encounter = structuredClone(this.dataStore.get(ENCOUNTER_MAP_KEY).get(encounterId));
        this.dataStore.set([SELECTED_ENCOUNTER_KEY], encounter);
        var table = document.getElementById('encounter-table');
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(encounterId)?.setAttribute('class','selectedRow');
        document.getElementById('encounterNameBig').innerText = encounter.objectName;
        document.getElementById('objectName').value = encounter.objectName;
        document.getElementById('currentRound').value = encounter.encounterRound;
        document.getElementById('session-list').value = encounter.sessionId;
        this.creatureTablePopulate();
        this.updateRunLink(encounter);
        showLibraryDetail();
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
            for (const value of [...creatureMap.values()].sort((a, b) => a.encounterCreatureName.localeCompare(b.encounterCreatureName))) {
                var row = creatureBody.insertRow(-1);
                row.setAttribute('id', value.encounterCreatureId);
                row.setAttribute('data-id', value.encounterCreatureId);
                renderLibraryRow(row.insertCell(), value.encounterCreatureName,
                    [value.isPC ? 'PC' : 'NPC', (value.currentHitPoints ?? value.hitPoints ?? '—') + ' HP',
                        'AC ' + (value.armorClass ?? '—'), value.type]);
            }
            this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
        } else {
                         creatureMap = '';
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

    markEncounterDirty() {
        if (!this.dataStore.get(SELECTED_ENCOUNTER_KEY)) return;
        this.encounterDirty = true;
        document.getElementById('mc-run-saved').hidden = true;
        document.getElementById('mc-encounter-feedback').textContent = 'Unsaved changes';
    }

    updateRunLink(encounter) {
        const link = document.getElementById('mc-run-saved');
        link.hidden = !Object.keys(encounter.creatureMap || {}).length;
        link.href = 'runEncounter.html?encounter=' + encodeURIComponent(encounter.objectId);
    }

    async updateButton() {
        const encounter = this.dataStore.get(SELECTED_ENCOUNTER_KEY);
        if (!encounter || this.savingEncounter) return;
        const name = document.getElementById('objectName');
        const feedback = document.getElementById('mc-encounter-feedback');
        if (!name.value.trim()) {
            feedback.textContent = 'Enter an encounter name.';
            name.focus();
            return;
        }
        const session = document.getElementById('session-list').value || encounter.sessionId;
        const save = document.getElementById('update-btn');
        const controls = [save, name, document.getElementById('session-list'), document.getElementById('add-creature-btn')];
        controls.forEach(control => {control.disabled = true;});
        this.savingEncounter = true;
        feedback.textContent = 'Saving…';
        let saved;
        let conflict = false;
        try {
            saved = await this.encounterClient.updateEncounter(
                {...encounter, objectName: name.value.trim(), sessionId: session},
                error => { conflict = error.response?.status === 409; }
            );
        } finally {
            this.savingEncounter = false;
            controls.forEach(control => {control.disabled = false;});
        }
        if (!saved) {
            feedback.textContent = conflict
                ? 'This encounter changed. Your edits are still here. Reload the page to review the saved encounter before editing again.'
                : 'Save failed. Your edits are still here.';
            return;
        }
        this.encounterDirty = false;
        this.dataStore.get(ENCOUNTER_MAP_KEY).set(saved.objectId, saved);
        const list = this.dataStore.get(ENCOUNTER_LIST_KEY);
        const index = list.findIndex(entry => entry.objectId === saved.objectId);
        if (index >= 0) list[index] = saved;
        await this.populateTable();
        await this.encounterRowClick(saved.objectId);
        feedback.textContent = 'Changes saved.';
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
            this.dataStore.set([CREATURE_MAP_KEY], '');
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
        if (this.savingEncounter) return;
        this.dataStore.set([SELECTED_NEW_CREATURE_KEY], '');
        document.getElementById('mc-roster-search').value = '';
        document.getElementById('mc-roster-quantity').value = '1';
        document.getElementById('mc-roster-feedback').textContent = '';
        if(this.dataStore.get(SELECTED_ENCOUNTER_KEY) != '') {
            document.getElementById('spinner-new-creature').hidden = false;
            document.getElementById('add-creature-table').hidden = true;
            document.getElementById('add-creature-finish-btn').hidden = true;
            document.getElementById('new-creature-field').hidden = true;
            var myOffcanvas = document.getElementById('offcanvasNewCreature');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
            var newCreatureList = await this.creatureClient.getMultipleCreatures();
            if (!newCreatureList?.length) {
                document.getElementById('mc-roster-feedback').textContent = newCreatureList ? 'No creatures in your library.' : 'Could not load the creature library.';
                document.getElementById('spinner-new-creature').hidden = true;
                return;
            }
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
               renderLibraryRow(row.insertCell(), creature.objectName,
                   [creature.isPC ? 'PC' : 'NPC', creature.size, creature.type,
                       (creature.hitPoints ?? '—') + ' HP', 'AC ' + (creature.armorClass ?? '—')]);

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
        const quantity = document.getElementById('mc-roster-quantity');
        quantity.disabled = Boolean(newCreature.isPC);
        if (newCreature.isPC) quantity.value = '1';
    }

    async addCreatureFinishButton() {
        const template = this.dataStore.get(SELECTED_NEW_CREATURE_KEY);
        const encounter = this.dataStore.get(SELECTED_ENCOUNTER_KEY);
        if (this.savingEncounter || !encounter) return;
        const feedback = document.getElementById('mc-roster-feedback');
        if (!template) {
            feedback.textContent = 'Select a creature first.';
            return;
        }
        const quantity = document.getElementById('mc-roster-quantity');
        if (!quantity.reportValidity()) return;
        const count = template.isPC ? 1 : quantity.valueAsNumber;
        const baseName = document.getElementById('new-creature-name').value.trim() || template.objectName;
        const creatureMap = this.dataStore.get(CREATURE_MAP_KEY) || new Map();
        const usedNames = new Set([...creatureMap.values()].map(creature => creature.encounterCreatureName));
        let suffix = 1;
        for (let copy = 0; copy < count; copy++) {
            let name = count === 1 ? baseName : baseName + ' ' + suffix++;
            while (usedNames.has(name)) name = baseName + ' ' + suffix++;
            usedNames.add(name);
            const creature = structuredClone(template);
            Object.assign(creature, {
                encounterCreatureName: name,
                encounterCreatureId: uuidv4(),
                knockedOut: false,
                dead: false,
                deathSaves: 3,
                currentHitPoints: creature.hitPoints,
            });
            creatureMap.set(creature.encounterCreatureId, creature);
        }
        encounter.creatureMap = this.mapToObj(creatureMap);
        this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
        this.markEncounterDirty();
        this.creatureTablePopulate();
        document.getElementById('close-new-creature-btn').click();
    }

    async updateCreatureButton() {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        if (creature) {
            creature.encounterCreatureName = document.getElementById('edit-creature-name').value;
            document.getElementById('close-edit-creature-btn').click();
            creatureMap.set(creature.encounterCreatureId, creature);
            this.dataStore.set(CREATURE_MAP_KEY, creatureMap);
            this.dataStore.get(SELECTED_ENCOUNTER_KEY).creatureMap = this.mapToObj(creatureMap);
            this.markEncounterDirty();
        this.creatureTablePopulate();
            document.getElementById('edit-creature-name').value = '';
            document.getElementById('edit-creature-objectName').value = '';
            document.getElementById('edit-creature-hp').value = '';
            document.getElementById('edit-creature-status').value = '';
            document.getElementById('edit-creature-size').value = '';
            document.getElementById('edit-creature-type').value = '';
        };
    }

    async deleteCreatureButton() {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        document.getElementById('close-edit-creature-btn').click();
        creatureMap.delete(creature.encounterCreatureId);
        this.dataStore.set(CREATURE_MAP_KEY, creatureMap);
        this.dataStore.get(SELECTED_ENCOUNTER_KEY).creatureMap = this.mapToObj(creatureMap);
        this.markEncounterDirty();
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

    lockElements() {
        var lock = document.getElementsByClassName('lockable');
                for(var i = 0; i < lock.length; i++) {
                    lock[i].disabled = true;
                }
    }

    unlockElements() {
        var lock = document.getElementsByClassName('lockable');
                for(var i = 0; i < lock.length; i++) {
                    lock[i].disabled = false;
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
