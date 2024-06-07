import AuthClient from "../api/authClient";
import EncounterClient from "../api/encounterClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SPELL_LIST_KEY = 'encounter-list';
const SPELL_MAP_KEY = 'encounter-map';
const SELECTED_SPELL_KEY = 'selected-encounter-key';
const SELECTED_TEMPLATE_KEY = 'selected-template-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SPELL_LIST_KEY]: [],
    [SPELL_MAP_KEY]: '',
    [SELECTED_SPELL_KEY]: '',
    [SELECTED_TEMPLATE_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class EncounterLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.encounterClient = new EncounterClient();
        this.bindClassMethods(['mount', 'startupActivities',
                                'populateTable', 'deleteButton',
                                'filterResetButton', 'updateButton',
                                'hideElements', 'showElements',
                                'createButton', 'createFinishButton',
                                'importButton', 'importFinishButton',
                                'searchButton', 'encounterRowClick',
                                'templateRowClick', 'attachEventListeners'], this);
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
            this.dataStore.set([SPELL_LIST_KEY], await this.encounterClient.getMultipleEncounters());
            var encounterMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([SPELL_MAP_KEY], encounterMap);
            await this.populateTable();
            this.attachEventListeners();
            this.showElements();
        } else {
            window.location.href = "index.html";
        }
    }

    async attachEventListeners() {
        document.getElementById('delete-btn').addEventListener('click', await this.deleteButton);
        document.getElementById('filter-btn').addEventListener('click', await this.populateTable);
        document.getElementById('clear-btn').addEventListener('click', await this.filterResetButton);
        document.getElementById('update-btn').addEventListener('click', await this.updateButton);
        document.getElementById('create-btn').addEventListener('click', await this.createButton);
        document.getElementById('create-finish-btn').addEventListener('click', await this.createFinishButton);
        document.getElementById('import-btn').addEventListener('click', await this.importButton);
        document.getElementById('search-btn').addEventListener('click', await this.searchButton);
        document.getElementById('import-finish-btn').addEventListener('click', await this.importFinishButton);
        document.getElementById('encounter-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.encounterRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('template-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.templateRowClick(event.target.parentNode.dataset.id)}});
    }

    async populateTable() {
//            var table = document.getElementById("encounter-table");
//            var oldTableBody = table.getElementsByTagName('tbody')[0];
//            var newTableBody = document.createElement('tbody');
//            var encounterList = this.dataStore.get(SPELL_LIST_KEY);
//            encounterList.sort((a, b) => a.objectName.localeCompare(b.objectName));
//            var nameSearch = document.getElementById('nameSearch').value;
//            var levelSearch = document.getElementById('levelSearch').value;
//            var schoolSearch = document.getElementById('schoolSearch').value;
//            for(var encounter of encounterList) {
//                if (
//                    (nameSearch == '' || encounter.objectName.toLowerCase().includes(nameSearch.toLowerCase())) &&
//                    (levelSearch == '' || encounter.encounterLevel == levelSearch) &&
//                    (schoolSearch == '' || encounter.encounterSchool == schoolSearch)
//                ) {
//
//                    var row = newTableBody.insertRow(-1);
//                    row.setAttribute('id', encounter.objectId);
//                    row.setAttribute('data-id', encounter.objectId);
//                    var cell1 = row.insertCell(0);
//                    var cell2 = row.insertCell(1);
//                    var cell3 = row.insertCell(2);
//                    var cell4 = row.insertCell(3);
//                    cell1.innerHTML = encounter.objectName;
//                    cell2.innerHTML = encounter.encounterLevel;
//                    cell3.innerHTML = encounter.ritualCast
//                                        ? "Yes"
//                                        : "No";
//                    cell4.innerHTML = encounter.encounterSchool;
//                }
//            }
//            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async templateRowClick(templateId) {
//        this.dataStore.set([SELECTED_TEMPLATE_KEY], templateId);
//        var table = document.getElementById('template-table');
//        for (var i = 0; i < table.rows.length; i++){
//            table.rows[i].removeAttribute('class');
//        }
//        document.getElementById(templateId).setAttribute('class','selectedRow');
    }

    async encounterRowClick(encounterId) {
//        var encounter = this.dataStore.get(SPELL_MAP_KEY).get(encounterId);
//        this.dataStore.set([SELECTED_SPELL_KEY], encounter);
//        var table = document.getElementById('encounter-table');
//        for (var i = 0; i < table.rows.length; i++){
//            table.rows[i].removeAttribute('class');
//        }
//        document.getElementById(encounterId).setAttribute('class','selectedRow');
//        document.getElementById('encounterNameBig').innerText = encounter.objectName;
//        document.getElementById('objectName').value = encounter.objectName;
//        document.getElementById('encounterDescription').value = encounter.encounterDescription;
//        document.getElementById('encounterHigherLevel').value = encounter.encounterHigherLevel;
//        document.getElementById('encounterRange').value = encounter.encounterRange;
//        document.getElementById('encounterComponents').value = encounter.encounterComponents;
//        document.getElementById('encounterMaterial').value = encounter.encounterMaterial;
//        document.getElementById('reaction').value = encounter.reaction;
//        document.getElementById('ritualCast').value = encounter.ritualCast
//                                                      ? "yes"
//                                                      : "no";
//        document.getElementById('castingTime').value = encounter.castingTime;
//        document.getElementById('castingTurns').value = encounter.castingTurns;
//        document.getElementById('encounterLevel').value = encounter.encounterLevel;
//        document.getElementById('encounterSchool').value = encounter.encounterSchool;
//        document.getElementById('innateCasts').value = encounter.innateCasts;
    }

    async deleteButton() {
//        var objectId = this.dataStore.get(SELECTED_SPELL_KEY).objectId;
//        if(objectId != '') {
//            this.hideElements();
//            await this.encounterClient.deleteEncounter(objectId);
//            location.reload();
//        }
    }

    async updateButton() {
//        this.hideElements();
//        var encounter = this.dataStore.get(SELECTED_SPELL_KEY);
//        encounter.objectName = document.getElementById('objectName').value;
//        encounter.encounterDescription = document.getElementById('encounterDescription').value;
//        encounter.encounterHigherLevel = document.getElementById('encounterHigherLevel').value;
//        encounter.encounterRange = document.getElementById('encounterRange').value;
//        encounter.encounterComponents = document.getElementById('encounterComponents').value;
//        encounter.encounterMaterial = document.getElementById('encounterMaterial').value;
//        encounter.reaction = document.getElementById('reaction').value;
//        if (document.getElementById('ritualCast').value.equals == "yes") {
//            encounter.ritualCast = true;
//        } else if (document.getElementById('ritualCast').value.equals == "no") {
//            encounter.ritualCast = false;
//        } else {
//            encounter.ritualCast = '';
//        }
//        encounter.castingTime = document.getElementById('castingTime').value;
//        encounter.castingTurns = document.getElementById('castingTurns').value;
//        encounter.encounterLevel = document.getElementById('encounterLevel').value;
//        encounter.encounterSchool = document.getElementById('encounterSchool').value;
//        encounter.innateCasts = document.getElementById('innateCasts').value;
//
//        try {
//            await this.encounterClient.updateEncounter(encounter);
//            location.reload();
//        } catch (error) {
//            this.showElements();
//            document.getElementById('offcanvas-warn-body').innerText = "You already have a encounter with the name " + document.getElementById('objectName').value + " in your library."
//            var myOffcanvas = document.getElementById('offcanvasWarn');
//            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//            bsOffcanvas.show();
//        }

    }

    filterResetButton() {
//        document.getElementById('nameSearch').value = '';
//        document.getElementById('levelSearch').value = '';
//        document.getElementById('schoolSearch').value = '';
//        this.populateTable();
    }

    createButton() {
//        var myOffcanvas = document.getElementById('offcanvasCreate');
//        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//        bsOffcanvas.show();
    }

    async createFinishButton() {
//        if(document.getElementById('newName').value != '') {
//            var encounter = {};
//            encounter.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
//            encounter.objectName = document.getElementById('newName').value;
//            encounter.encounterDescription = document.getElementById('newDesc').value;
//            encounter.encounterLevel = document.getElementById('newLevel').value;
//            encounter.encounterSchool = document.getElementById('newSchool').value;
//
//            try {
//                this.hideElements();
//                document.getElementById('close-btn').click()
//                var newEncounter = await this.encounterClient.createEncounter(encounter);
//                document.getElementById('newName').value = '';
//                document.getElementById('newDesc').value = '';
//                document.getElementById('newLevel').value = '';
//                document.getElementById('newSchool').value = '';
//                this.dataStore.set([SPELL_LIST_KEY], await this.encounterClient.getMultipleEncounters());
//                var encounterMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
//                this.dataStore.set([SPELL_MAP_KEY], encounterMap);
//                await this.populateTable();
//                this.showElements();
//                this.encounterRowClick(newEncounter.objectId);
//            } catch (error) {
//                this.showElements();
//                document.getElementById('offcanvas-warn-body').innerText = "You already have a encounter with the name " + document.getElementById('objectName').value + " in your library."
//                var myOffcanvas = document.getElementById('offcanvasWarn');
//                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//                bsOffcanvas.show();
//            }
//        }
    }

    importButton() {
//        var myOffcanvas = document.getElementById('offcanvasImport');
//        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//        bsOffcanvas.show();
    }

    async importFinishButton() {
//        var slug = this.dataStore.get(SELECTED_TEMPLATE_KEY);
//        if(!slug=='') {
//            try {
//                this.hideElements();
//                document.getElementById('close-import-btn').click()
//                var newEncounter = await this.encounterClient.createTemplate(slug);
//                this.dataStore.set([SPELL_LIST_KEY], await this.encounterClient.getMultipleEncounters());
//                var encounterMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
//                this.dataStore.set([SPELL_MAP_KEY], encounterMap);
//                this.populateTable();
//                this.showElements();
//                this.encounterRowClick(newEncounter.objectId);
//            } catch (error) {
//                this.showElements();
//                document.getElementById('offcanvas-warn-body').innerText = "You already have a encounter with the name " + document.getElementById('objectName').value + " in your library."
//                var myOffcanvas = document.getElementById('offcanvasWarn');
//                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//                bsOffcanvas.show();
//            }
//        }
    }

    async searchButton() {
//        this.dataStore.set([SELECTED_TEMPLATE_KEY], '');
//        document.getElementById('template-table').hidden = true;
//        document.getElementById('spinner-side').hidden = false;
//        var templates = await this.encounterClient.searchTemplate(document.getElementById('templateSearch').value, document.getElementById('limit').value)
//        document.getElementById('template-table').hidden = false;
//        document.getElementById('spinner-side').hidden = true;
//        var table = document.getElementById("template-table");
//        var oldTableBody = table.getElementsByTagName('tbody')[0];
//        var newTableBody = document.createElement('tbody');
//        var encounterList = templates;
//        encounterList.sort((a, b) => a.name.localeCompare(b.name));
//        for(var templateEncounter of encounterList) {
//            if (
//                !templateEncounter.resourceExists
//            ) {
//                var row = newTableBody.insertRow(-1);
//                row.setAttribute('id', templateEncounter.slug);
//                row.setAttribute('data-id', templateEncounter.slug);
//                var cell1 = row.insertCell(0);
//                var cell2 = row.insertCell(1);
//                var cell3 = row.insertCell(2);
//                cell1.innerHTML = templateEncounter.name;
//                cell2.innerHTML = templateEncounter.level;
//                cell3.innerHTML = templateEncounter.document__title
//            }
//        }
//        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
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

};

 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const encounterLibraryScripts = new EncounterLibraryScripts();
     encounterLibraryScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
