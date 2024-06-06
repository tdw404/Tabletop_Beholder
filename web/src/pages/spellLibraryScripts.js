import AuthClient from "../api/authClient";
import SpellClient from "../api/spellClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SPELL_LIST_KEY = 'spell-list';
const SPELL_MAP_KEY = 'spell-map';
const SELECTED_SPELL_KEY = 'selected-spell-key';
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
 class SpellLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.spellClient = new SpellClient();
        this.bindClassMethods(['mount', 'startupActivities',
                                'populateTable', 'deleteButton',
                                'filterResetButton', 'updateButton',
                                'hideElements', 'showElements',
                                'createButton', 'createFinishButton',
                                'importButton', 'importFinishButton',
                                'searchButton', 'spellRowClick',
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
            this.dataStore.set([SPELL_LIST_KEY], await this.spellClient.getMultipleSpells());
            var spellMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([SPELL_MAP_KEY], spellMap);
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
        document.getElementById('spell-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.spellRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('template-table').addEventListener('click', (event) => {
                                            if (event.target.closest('tbody')) {this.templateRowClick(event.target.parentNode.dataset.id)}});
    }

    async populateTable() {
            var table = document.getElementById("spell-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var spellList = this.dataStore.get(SPELL_LIST_KEY);
            spellList.sort((a, b) => a.objectName.localeCompare(b.objectName));
            var nameSearch = document.getElementById('nameSearch').value;
            var levelSearch = document.getElementById('levelSearch').value;
            var schoolSearch = document.getElementById('schoolSearch').value;
            for(var spell of spellList) {
                if (
                    (nameSearch == '' || spell.objectName.toLowerCase().includes(nameSearch.toLowerCase())) &&
                    (levelSearch == '' || spell.spellLevel == levelSearch) &&
                    (schoolSearch == '' || spell.spellSchool == schoolSearch)
                ) {

                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', spell.objectId);
                    row.setAttribute('data-id', spell.objectId);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    cell1.innerHTML = spell.objectName;
                    cell2.innerHTML = spell.spellLevel;
                    cell3.innerHTML = spell.ritualCast
                                        ? "Yes"
                                        : "No";
                    cell4.innerHTML = spell.spellSchool;
                }
            }
            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async templateRowClick(templateId) {
        this.dataStore.set([SELECTED_TEMPLATE_KEY], templateId);
        var table = document.getElementById('template-table');
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(templateId).setAttribute('class','selectedRow');
    }

    async spellRowClick(spellId) {
        var spell = this.dataStore.get(SPELL_MAP_KEY).get(spellId);
        this.dataStore.set([SELECTED_SPELL_KEY], spell);
        var table = document.getElementById('spell-table');
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(spellId).setAttribute('class','selectedRow');
        document.getElementById('spellNameBig').innerText = spell.objectName;
        document.getElementById('objectName').value = spell.objectName;
        document.getElementById('spellDescription').value = spell.spellDescription;
        document.getElementById('spellHigherLevel').value = spell.spellHigherLevel;
        document.getElementById('spellRange').value = spell.spellRange;
        document.getElementById('spellComponents').value = spell.spellComponents;
        document.getElementById('spellMaterial').value = spell.spellMaterial;
        document.getElementById('reaction').value = spell.reaction;
        document.getElementById('ritualCast').value = spell.ritualCast
                                                      ? "yes"
                                                      : "no";
        document.getElementById('castingTime').value = spell.castingTime;
        document.getElementById('castingTurns').value = spell.castingTurns;
        document.getElementById('spellLevel').value = spell.spellLevel;
        document.getElementById('spellSchool').value = spell.spellSchool;
        document.getElementById('innateCasts').value = spell.innateCasts;
    }

    async deleteButton() {
        var objectId = this.dataStore.get(SELECTED_SPELL_KEY).objectId;
        if(objectId != '') {
            this.hideElements();
            await this.spellClient.deleteSpell(objectId);
            location.reload();
        }
    }

    async updateButton() {
        this.hideElements();
        var spell = this.dataStore.get(SELECTED_SPELL_KEY);
        spell.objectName = document.getElementById('objectName').value;
        spell.spellDescription = document.getElementById('spellDescription').value;
        spell.spellHigherLevel = document.getElementById('spellHigherLevel').value;
        spell.spellRange = document.getElementById('spellRange').value;
        spell.spellComponents = document.getElementById('spellComponents').value;
        spell.spellMaterial = document.getElementById('spellMaterial').value;
        spell.reaction = document.getElementById('reaction').value;
        if (document.getElementById('ritualCast').value.equals == "yes") {
            spell.ritualCast = true;
        } else if (document.getElementById('ritualCast').value.equals == "no") {
            spell.ritualCast = false;
        } else {
            spell.ritualCast = '';
        }
        spell.castingTime = document.getElementById('castingTime').value;
        spell.castingTurns = document.getElementById('castingTurns').value;
        spell.spellLevel = document.getElementById('spellLevel').value;
        spell.spellSchool = document.getElementById('spellSchool').value;
        spell.innateCasts = document.getElementById('innateCasts').value;

        try {
            await this.spellClient.updateSpell(spell);
            location.reload();
        } catch (error) {
            this.showElements();
            document.getElementById('offcanvas-warn-body').innerText = "You already have a spell with the name " + document.getElementById('objectName').value + " in your library."
            var myOffcanvas = document.getElementById('offcanvasWarn');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }

    }

    filterResetButton() {
        document.getElementById('nameSearch').value = '';
        document.getElementById('levelSearch').value = '';
        document.getElementById('schoolSearch').value = '';
        this.populateTable();
    }

    createButton() {
        var myOffcanvas = document.getElementById('offcanvasCreate');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async createFinishButton() {
        if(document.getElementById('newName').value != '') {
            var spell = {};
            spell.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
            spell.objectName = document.getElementById('newName').value;
            spell.spellDescription = document.getElementById('newDesc').value;
            spell.spellLevel = document.getElementById('newLevel').value;
            spell.spellSchool = document.getElementById('newSchool').value;

            try {
                this.hideElements();
                document.getElementById('close-btn').click()
                var newSpell = await this.spellClient.createSpell(spell);
                document.getElementById('newName').value = '';
                document.getElementById('newDesc').value = '';
                document.getElementById('newLevel').value = '';
                document.getElementById('newSchool').value = '';
                this.dataStore.set([SPELL_LIST_KEY], await this.spellClient.getMultipleSpells());
                var spellMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
                this.dataStore.set([SPELL_MAP_KEY], spellMap);
                await this.populateTable();
                this.showElements();
                this.spellRowClick(newSpell.objectId);
            } catch (error) {
                this.showElements();
                document.getElementById('offcanvas-warn-body').innerText = "You already have a spell with the name " + document.getElementById('objectName').value + " in your library."
                var myOffcanvas = document.getElementById('offcanvasWarn');
                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                bsOffcanvas.show();
            }
        }
    }

    importButton() {
        var myOffcanvas = document.getElementById('offcanvasImport');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async importFinishButton() {
        var slug = this.dataStore.get(SELECTED_TEMPLATE_KEY);
        if(!slug=='') {
            try {
                this.hideElements();
                document.getElementById('close-import-btn').click()
                var newSpell = await this.spellClient.createTemplate(slug);
                this.dataStore.set([SPELL_LIST_KEY], await this.spellClient.getMultipleSpells());
                var spellMap = new Map(this.dataStore.get([SPELL_LIST_KEY]).map((obj) => [obj.objectId, obj]));
                this.dataStore.set([SPELL_MAP_KEY], spellMap);
                this.populateTable();
                this.showElements();
                this.spellRowClick(newSpell.objectId);
            } catch (error) {
                this.showElements();
                document.getElementById('offcanvas-warn-body').innerText = "You already have a spell with the name " + document.getElementById('objectName').value + " in your library."
                var myOffcanvas = document.getElementById('offcanvasWarn');
                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                bsOffcanvas.show();
            }
        }
    }

    async searchButton() {
        this.dataStore.set([SELECTED_TEMPLATE_KEY], '');
        document.getElementById('template-table').hidden = true;
        document.getElementById('spinner-side').hidden = false;
        var templates = await this.spellClient.searchTemplate(document.getElementById('templateSearch').value, document.getElementById('limit').value)
        document.getElementById('template-table').hidden = false;
        document.getElementById('spinner-side').hidden = true;
        var table = document.getElementById("template-table");
        var oldTableBody = table.getElementsByTagName('tbody')[0];
        var newTableBody = document.createElement('tbody');
        var spellList = templates;
        spellList.sort((a, b) => a.name.localeCompare(b.name));
        for(var templateSpell of spellList) {
            if (
                !templateSpell.resourceExists
            ) {
                var row = newTableBody.insertRow(-1);
                row.setAttribute('id', templateSpell.slug);
                row.setAttribute('data-id', templateSpell.slug);
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                cell1.innerHTML = templateSpell.name;
                cell2.innerHTML = templateSpell.level;
                cell3.innerHTML = templateSpell.document__title
            }
        }
        oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
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
     const spellLibraryScripts = new SpellLibraryScripts();
     spellLibraryScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
