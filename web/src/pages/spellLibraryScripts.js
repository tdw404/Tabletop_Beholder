import AuthClient from "../api/authClient";
import SpellClient from "../api/spellClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SPELLLIST_KEY = 'spell-list';
const SELECTED_SPELL_KEY = 'selected-spell-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SPELLLIST_KEY]: [],
    [SELECTED_SPELL_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class SpellLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.spellClient = new SpellClient();
        this.bindClassMethods(['mount', 'startupActivities', 'populateTable', 'deleteButton', 'filterResetButton', 'updateButton'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };

     mount() {
        this.navbarProvider.addNavbarToPage();
        this.startupActivities();
     };

    async startupActivities() {
        if (await this.client.verifyLogin()) {
            const spells = await this.spellClient.getMultipleSpells();
            this.dataStore.set([SPELLLIST_KEY], await this.spellClient.getMultipleSpells());
            await this.populateTable();
            document.getElementById('spinner').hidden = true;
            document.getElementById('spell-table').hidden = false;
            document.getElementById('search-fields').hidden = false;
            document.getElementById('filter-btn').hidden = false;
            document.getElementById('clear-btn').hidden = false;
            document.getElementById('delete-btn').addEventListener('click', await this.deleteButton);
            document.getElementById('filter-btn').addEventListener('click', await this.populateTable);
            document.getElementById('clear-btn').addEventListener('click', await this.filterResetButton);
            document.getElementById('update-btn').addEventListener('click', await this.updateButton);
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
            document.getElementById('spinner').hidden = false;
            document.getElementById('spell-table').hidden = true;
            document.getElementById('search-fields').hidden = true;
            document.getElementById('filter-btn').hidden = true;
                        document.getElementById('clear-btn').hidden = true;
            await this.spellClient.deleteSpell(objectId);
            location.reload();
        }
    }

    async updateButton() {
        document.getElementById('spinner').hidden = false;
        document.getElementById('spell-table').hidden = true;
        document.getElementById('search-fields').hidden = true;
        document.getElementById('filter-btn').hidden = true;
        document.getElementById('clear-btn').hidden = true;
        const spell = await this.spellClient.getSingleSpell(this.dataStore.get(SELECTED_SPELL_KEY));
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
            document.getElementById('spinner').hidden = true;
            document.getElementById('spell-table').hidden = false;
            document.getElementById('search-fields').hidden = false;
            document.getElementById('filter-btn').hidden = false;
            document.getElementById('clear-btn').hidden = false;
            alert("You already have a spell with the name " + document.getElementById('objectName').value + " in your library.");
        }

    }

    filterResetButton() {
        document.getElementById('nameSearch').value = '';
        document.getElementById('levelSearch').value = '';
        document.getElementById('schoolSearch').value = '';
        this.populateTable();
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
