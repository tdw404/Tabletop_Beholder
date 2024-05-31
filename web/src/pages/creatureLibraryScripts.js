import AuthClient from "../api/authClient";
import SpellClient from "../api/spellClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SPELLLIST_KEY = 'spell-list';
const SELECTED_SPELL_KEY = 'selected-spell-key';
const SELECTED_TEMPLATE_KEY = 'selected-template-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SPELLLIST_KEY]: [],
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
                                'searchButton'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.addNavbarToPage();
        this.startupActivities();
     };

    async startupActivities() {
        if (await this.client.verifyLogin()) {
            const{email, name} = await this.client.getIdentity().then(result => result);
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            this.dataStore.set([SPELLLIST_KEY], await this.spellClient.getMultipleSpells());
            await this.populateTable();
            this.showElements();
            document.getElementById('delete-btn').addEventListener('click', await this.deleteButton);
            document.getElementById('filter-btn').addEventListener('click', await this.populateTable);
            document.getElementById('clear-btn').addEventListener('click', await this.filterResetButton);
            document.getElementById('update-btn').addEventListener('click', await this.updateButton);
            document.getElementById('create-btn').addEventListener('click', await this.createButton);
            document.getElementById('create-finish-btn').addEventListener('click', await this.createFinishButton);
            document.getElementById('import-btn').addEventListener('click', await this.importButton);
            document.getElementById('search-btn').addEventListener('click', await this.searchButton);
            document.getElementById('import-finish-btn').addEventListener('click', await this.importFinishButton);
        }
    }

    async populateTable() {
            var table = document.getElementById("spell-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var spellList = this.dataStore.get(SPELLLIST_KEY);
            spellList.sort((a, b) => a.objectName.localeCompare(b.objectName));
            const nameSearch = document.getElementById('nameSearch').value;
            const levelSearch = document.getElementById('levelSearch').value;
            const schoolSearch = document.getElementById('schoolSearch').value;
            for(const spell of spellList) {
                if (
                    (nameSearch == '' || spell.objectName.toLowerCase().includes(nameSearch.toLowerCase())) &&
                    (levelSearch == '' || spell.spellLevel == levelSearch) &&
                    (schoolSearch == '' || spell.spellSchool == schoolSearch)
                ) {

                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', spell.objectId);
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
                    var createClickHandler = function(row, dataStore) {
                        return function() {
                            for (var i = 0; i < table.rows.length; i++){
                                table.rows[i].removeAttribute('class');
                            }
                            row.setAttribute('class','selectedRow')
                            document.getElementById('spellNameBig').innerText = spell.objectName;
                            document.getElementById('objectName').setAttribute('value', spell.objectName);
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
                            dataStore.set([SELECTED_SPELL_KEY], spell.objectId);

                        };
                    };
                    row.onclick = createClickHandler(row, this.dataStore);
                }
            }
            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async deleteButton() {
        const objectId = this.dataStore.get(SELECTED_SPELL_KEY);
        if(objectId != '') {
            this.hideElements();
            await this.spellClient.deleteSpell(objectId);
            location.reload();
        }
    }

    async updateButton() {
        this.hideElements();
        const spell = {};
        spell.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
        spell.objectId = this.dataStore.get(SELECTED_SPELL_KEY);
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
            //this.spellClient = this.dataStore.get(SPELL_CLIENT_KEY);
            const spell = {};
            spell.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
            spell.objectName = document.getElementById('newName').value;
            spell.spellDescription = document.getElementById('newDesc').value;
            spell.spellLevel = document.getElementById('newLevel').value;
            spell.spellSchool = document.getElementById('newSchool').value;

            try {
                this.hideElements();
                document.getElementById('close-btn').click()
                const newSpell = await this.spellClient.createSpell(spell);
                document.getElementById('newName').value = '';
                document.getElementById('newDesc').value = '';
                document.getElementById('newLevel').value = '';
                document.getElementById('newSchool').value = '';
                this.dataStore.set([SPELLLIST_KEY], await this.spellClient.getMultipleSpells());
                await this.populateTable();
                this.showElements();
                document.getElementById(newSpell.objectId).click();
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
        const slug = this.dataStore.get(SELECTED_TEMPLATE_KEY);
        if(!slug=='') {
            try {
                this.hideElements();
                document.getElementById('close-import-btn').click()
                const newSpell = await this.spellClient.createTemplate(slug);
                this.dataStore.set([SPELLLIST_KEY], await this.spellClient.getMultipleSpells());
                await this.populateTable();
                this.showElements();
                document.getElementById(newSpell.objectId).click();
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
        const templates = await this.spellClient.searchTemplate(document.getElementById('templateSearch').value, document.getElementById('limit').value)
        document.getElementById('template-table').hidden = false;
        document.getElementById('spinner-side').hidden = true;
        var table = document.getElementById("template-table");
                    var oldTableBody = table.getElementsByTagName('tbody')[0];
                    var newTableBody = document.createElement('tbody');
                    var spellList = templates;
                    spellList.sort((a, b) => a.name.localeCompare(b.name));
                    for(const templateSpell of spellList) {
                        if (
                            !templateSpell.resourceExists
                        ) {

                            var row = newTableBody.insertRow(-1);
                            row.setAttribute('id', templateSpell.slug);
                            var cell1 = row.insertCell(0);
                            var cell2 = row.insertCell(1);
                            var cell3 = row.insertCell(2);
                            cell1.innerHTML = templateSpell.name;
                            cell2.innerHTML = templateSpell.level;
                            cell3.innerHTML = templateSpell.document__title
                            var createClickHandler = function(row, dataStore) {
                                return function() {
                                    for (var i = 0; i < table.rows.length; i++){
                                        table.rows[i].removeAttribute('class');
                                    }
                                    row.setAttribute('class','selectedRow')
                                    dataStore.set([SELECTED_TEMPLATE_KEY], templateSpell.slug);

                                };
                            };
                            row.onclick = createClickHandler(row, this.dataStore);
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
