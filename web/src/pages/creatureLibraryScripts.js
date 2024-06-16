import AuthClient from "../api/authClient";
import CreatureClient from "../api/creatureClient"
import SpellClient from "../api/spellClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";
import { v4 as uuidv4 } from 'uuid';

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const CREATURE_LIST_KEY = 'creature-list';
const CREATURE_MAP_KEY = 'creature-map';
const SELECTED_CREATURE_KEY = 'selected-creature-key';
const SELECTED_TEMPLATE_KEY = 'selected-template-key';
const ACTION_MAP_KEY = 'action-map-key';
const SELECTED_ACTION_KEY = 'selected-action-key';
const SPELL_MAP_KEY = 'spell-map-key';
const SELECTED_SPELL_KEY = 'selected-spell-key';
const SELECTED_NEW_SPELL_KEY = 'selected-new-spell-key';
const NEW_SPELL_MAP_KEY = 'new-spell-map-key';
const ERROR_DIALOGUE_KEY = 'error-dialogue-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [CREATURE_LIST_KEY]: [],
    [CREATURE_MAP_KEY]: '',
    [SELECTED_CREATURE_KEY]: '',
    [SELECTED_TEMPLATE_KEY]: '',
    [ACTION_MAP_KEY]: '',
    [SELECTED_ACTION_KEY]: '',
    [SPELL_MAP_KEY]: '',
    [SELECTED_SPELL_KEY]: '',
    [SELECTED_NEW_SPELL_KEY]: '',
    [NEW_SPELL_MAP_KEY]: '',
    [ERROR_DIALOGUE_KEY]: '',

};
/**
 * Adds functionality to the landing page.
 */
 class CreatureLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.creatureClient = new CreatureClient();
        this.spellClient = new SpellClient();
        this.bindClassMethods(['mount', 'startupActivities',
                                'populateTable', 'deleteButton',
                                'filterResetButton', 'updateButton',
                                'hideElements', 'showElements',
                                'createButton', 'createFinishButton',
                                'importButton', 'importFinishButton',
                                'searchButton', 'creatureRowClick',
                                'actionRowClick', 'removeActionButton',
                                'updateActionButton', 'actionTablePopulate',
                                'mapToObj', 'addActionButton',
                                'addActionFinishButton', 'attachEventListeners',
                                'spellTablePopulate', 'sortSpellTable',
                                'spellRowClick', 'removeSpellButton',
                                'addSpellButton', 'addSpellRowClick',
                                'addSpellFinishButton', 'cloneButton',
                                'pcSwitch', 'rebaseSpells',
                                'rebaseSpellsContinue', 'templateRowClick'], this);
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
            this.dataStore.set([CREATURE_LIST_KEY], await this.creatureClient.getMultipleCreatures());
            var creatureMap = new Map(this.dataStore.get([CREATURE_LIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
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
        document.getElementById('creature-table').addEventListener('click', (event) => {
                                        if (event.target.closest('tbody')) {this.creatureRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('actions').addEventListener('click', (event) => {
                                        if (event.target.closest('tbody')) {this.actionRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('spells-table').addEventListener('click', (event) => {
                                                if (event.target.closest('tbody')) {this.spellRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('add-spells-table').addEventListener('click', (event) => {
                                                if (event.target.closest('tbody')) {this.addSpellRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('add-spells-table').addEventListener('click', (event) => {
                                                        if (event.target.closest('tbody')) {this.addSpellRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('template-table').addEventListener('click', (event) => {
                                                    if (event.target.closest('tbody')) {this.templateRowClick(event.target.parentNode.dataset.id)}});
        document.getElementById('remove-action-btn').addEventListener('click', await this.removeActionButton);
        document.getElementById('update-action-btn').addEventListener('click', await this.updateActionButton);
        document.getElementById('new-action-btn').addEventListener('click', await this.addActionButton);
        document.getElementById('add-action-btn').addEventListener('click', await this.addActionFinishButton);
        document.getElementById('remove-spell-btn').addEventListener('click', await this.removeSpellButton);
        document.getElementById('new-spell-btn').addEventListener('click', await this.addSpellButton);
        document.getElementById('add-spell-btn').addEventListener('click', await this.addSpellFinishButton);
        document.getElementById('clone-btn').addEventListener('click', await this.cloneButton);
        document.getElementById('pcSwitch').addEventListener('change', await this.pcSwitch);
        document.getElementById('rebase-spells-btn').addEventListener('click', await this.rebaseSpells);
        document.getElementById('rebase-continue-btn').addEventListener('click', await this.rebaseSpellsContinue);
    }

    async populateTable() {
            var table = document.getElementById("creature-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var creatureList = this.dataStore.get(CREATURE_LIST_KEY);
            creatureList?.sort((a, b) => a.objectName.localeCompare(b.objectName));
            var nameSearch = document.getElementById('nameSearch').value;
            var pcSearch = document.getElementById('pcSearch').value;
            var crAboveSearch = document.getElementById('crAboveSearch').value;
            var crBelowSearch = document.getElementById('crBelowSearch').value;
            var sizeSearch = document.getElementById('sizeSearch').value;
            for(var creature of creatureList) {
                if (
                    (nameSearch == '' || creature.objectName.toLowerCase().includes(nameSearch.toLowerCase())) &&
                    (pcSearch == 'Both' || creature.isPC == true && pcSearch == 'PC' || creature.isPC == false && pcSearch == 'NPC') &&
                    (crAboveSearch == '' || creature.challengeRating >= crAboveSearch) &&
                    (crBelowSearch == '' || creature.challengeRating <= crBelowSearch) &&
                    (sizeSearch == '' || creature.size == sizeSearch)
                ) {
                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', creature.objectId);
                    row.setAttribute('data-id', creature.objectId);
                    var cell1 = row.insertCell(0);
                    var cell2 = row.insertCell(1);
                    var cell3 = row.insertCell(2);
                    var cell4 = row.insertCell(3);
                    var cell5 = row.insertCell(4);
                    var cell6 = row.insertCell(5);
                    cell1.innerHTML = creature.objectName;
                    cell2.innerHTML = creature.isPC
                                        ?"Yes"
                                        :"";
                    cell3.innerHTML = creature.challengeRating;
                    cell4.innerHTML = creature.size;
                    cell5.innerHTML = creature.type;
                    cell6.innerHTML = creature.alignment;
                }
            }
            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async creatureRowClick(creatureId) {
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(creatureId);
        this.dataStore.set([SELECTED_CREATURE_KEY], creature);
        var table = document.getElementById('creature-table');
        for (var i = 0; i < table.rows.length; i++){
          table.rows[i].removeAttribute('class');
        }
        document.getElementById(creatureId).setAttribute('class','selectedRow');
        if (creature.isPC) {
            var show = document.getElementsByClassName('pc-tab');
                            for(var i = 0; i < show.length; i++) {
                                show[i].hidden = false;
                            }
            var hide = document.getElementsByClassName('npc-tab');
                            for(var i = 0; i < hide.length; i++) {
                                hide[i].hidden = true;
                            }
            document.getElementById('pc-tab').click()
        } else {
            var show = document.getElementsByClassName('npc-tab');
                            for(var i = 0; i < show.length; i++) {
                                show[i].hidden = false;
                            }
            var hide = document.getElementsByClassName('pc-tab');
                            for(var i = 0; i < hide.length; i++) {
                                hide[i].hidden = true;
                            }
            document.getElementById('details-tab').click()
        }
        document.getElementById('creatureNameBig').innerText = creature.objectName;
        document.getElementById('objectName').value = creature.objectName;
        document.getElementById('pcName').value = creature.objectName;
        document.getElementById('pcLevel').value = creature.pcLevel;
        document.getElementById('sourceBook').value = creature.sourceBook;
        document.getElementById('creatureDescription').value = creature.creatureDescription;
        document.getElementById('size').value = creature.size;
        document.getElementById('type').value = creature.type;
        document.getElementById('subType').value = creature.subType;
        document.getElementById('group').value = creature.group;
        document.getElementById('alignment').value = creature.alignment;
        document.getElementById('armorClass').value = creature.armorClass;
        document.getElementById('armorType').value = creature.armorType;
        document.getElementById('vulnerabilities').value = creature.vulnerabilities;
        document.getElementById('resistances').value = creature.resistances;
        document.getElementById('immunities').value = creature.immunities;
        document.getElementById('conditionImmunities').value = creature.conditionImmunities;
        document.getElementById('hitDice').value = creature.hitDice;
        document.getElementById('hitPoints').value = creature.hitPoints;
        document.getElementById('senses').value = creature.senses;
        document.getElementById('languages').value = creature.languages;
        document.getElementById('challengeRating').value = creature.challengeRating;
        document.getElementById('legendaryDesc').value = creature.legendaryDesc;
        document.getElementById('spellcastingAbility').value = creature.spellcastingAbility;
        document.getElementById('spellSaveDC').value = creature.spellSaveDC;
        document.getElementById('spellAttackModifier').value = creature.spellAttackModifier;
        if (creature.spellSlots !== null) {
            var spellSlotMap = new Map(Object.entries(creature.spellSlots));
            document.getElementById('ss1').value = spellSlotMap.get(1);
            document.getElementById('ss2').value = spellSlotMap.get(2);
            document.getElementById('ss3').value = spellSlotMap.get(3);
            document.getElementById('ss4').value = spellSlotMap.get(4);
            document.getElementById('ss5').value = spellSlotMap.get(5);
            document.getElementById('ss6').value = spellSlotMap.get(6);
            document.getElementById('ss7').value = spellSlotMap.get(7);
            document.getElementById('ss8').value = spellSlotMap.get(8);
            document.getElementById('ss9').value = spellSlotMap.get(9);
        }
        document.getElementById('walkSpeed').value = creature.speedMap?.walk;
        document.getElementById('flySpeed').value = creature.speedMap?.fly;
        document.getElementById('swimSpeed').value = creature.speedMap?.swim;
        document.getElementById('burrowSpeed').value = creature.speedMap?.burrow;
        document.getElementById('climbSpeed').value = creature.speedMap?.climb;
        document.getElementById('hoverSpeed').value = creature.speedMap?.hover;
        document.getElementById('strStat').value = creature.statMap?.strength;
        document.getElementById('dexStat').value = creature.statMap?.dexterity;
        document.getElementById('conStat').value = creature.statMap?.constitution;
        document.getElementById('intStat').value = creature.statMap?.intelligence;
        document.getElementById('wisStat').value = creature.statMap?.wisdom;
        document.getElementById('chaStat').value = creature.statMap?.charisma;
        document.getElementById('strSave').value = creature.saveMap?.strength_save;
        document.getElementById('dexSave').value = creature.saveMap?.dexterity_save;
        document.getElementById('conSave').value = creature.saveMap?.constitution_save;
        document.getElementById('intSave').value = creature.saveMap?.intelligence_save;
        document.getElementById('wisSave').value = creature.saveMap?.wisdom_save;
        document.getElementById('chaSave').value = creature.saveMap?.charisma_save;
        document.getElementById('acrobatics').value = creature.skillsMap?.acrobatics;
        document.getElementById('animalHandling').value = creature.skillsMap?.animalHandling;
        document.getElementById('arcana').value = creature.skillsMap?.arcana;
        document.getElementById('athletics').value = creature.skillsMap?.athletics;
        document.getElementById('deception').value = creature.skillsMap?.deception;
        document.getElementById('history').value = creature.skillsMap?.history;
        document.getElementById('insight').value = creature.skillsMap?.insight;
        document.getElementById('intimidation').value = creature.skillsMap?.intimidation;
        document.getElementById('investigation').value = creature.skillsMap?.investigation;
        document.getElementById('medicine').value = creature.skillsMap?.medicine;
        document.getElementById('nature').value = creature.skillsMap?.nature;
        document.getElementById('perception').value = creature.skillsMap?.perception;
        document.getElementById('performance').value = creature.skillsMap?.performance;
        document.getElementById('persuasion').value = creature.skillsMap?.persuasion;
        document.getElementById('religion').value = creature.skillsMap?.religion;
        document.getElementById('sleight').value = creature.skillsMap?.sleight;
        document.getElementById('stealth').value = creature.skillsMap?.stealth;
        document.getElementById('survival').value = creature.skillsMap?.survival;
        this.actionTablePopulate();
        this.spellTablePopulate();

    }

    async spellTablePopulate() {
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        if (creature.spellMap !== null) {
            var spellMap = new Map(Object.entries(creature.spellMap));
            var spellTable = document.getElementById(('spells-table'));
            var spellBody = spellTable.getElementsByTagName('tbody')[0];
            spellBody.innerHTML = '';
            for (var [key, value] of spellMap) {
                var spellId = value.objectId;
                var spellRow = spellBody.insertRow(-1);
                spellRow.setAttribute('id', spellId);
                spellRow.setAttribute('data-id', spellId);
                var spellCell0 = spellRow.insertCell(0);
                var spellCell1 = spellRow.insertCell(1);
                var spellCell2 = spellRow.insertCell(2);
                var spellCell3 = spellRow.insertCell(3);
                spellCell0.innerHTML = value.objectName;
                spellCell1.innerHTML = value.spellLevel;
                spellCell2.innerHTML = value.ritualCast
                                        ? "Yes"
                                        : "No";
                spellCell3.innerHTML = value.spellSchool;
            }
            this.dataStore.set([SPELL_MAP_KEY], spellMap);
            this.sortSpellTable(spellTable);
        }
    }

    sortSpellTable(table) {
      var switching = true;
      while (switching) {
        switching = false;
        var rows = table.rows;
        for (var i = 1; i < (rows.length - 1); i++) {
          var shouldSwitch = false;
          var x = rows[i];
          var y = rows[i + 1];
          if (x.getElementsByTagName("td")[1].innerHTML > y.getElementsByTagName("td")[1].innerHTML ||
                (x.getElementsByTagName("td")[1].innerHTML == y.getElementsByTagName("td")[1].innerHTML &&
                 x.getElementsByTagName("td")[0].innerHTML > y.getElementsByTagName("td")[0].innerHTML)) {
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

    async templateRowClick(templateId) {
        this.dataStore.set([SELECTED_TEMPLATE_KEY], templateId);
        var table = document.getElementById('template-table');
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(templateId).setAttribute('class','selectedRow');
    }

    async spellRowClick(spellId) {
            if (this.dataStore.get(SELECTED_CREATURE_KEY) != '') {
                var spell = this.dataStore.get(SPELL_MAP_KEY).get(spellId);
                this.dataStore.set([SELECTED_SPELL_KEY], spell);
                var spellTable = document.getElementById('spells-table');
                    for (var i = 0; i < spellTable.rows.length; i++){
                                spellTable.rows[i].removeAttribute('class');
                            }
                document.getElementById(spellId).setAttribute('class','selectedRow');
                document.getElementById('spellName').value = spell.objectName;
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
                var myOffcanvas = document.getElementById('offcanvasViewSpell');
                var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                bsOffcanvas.show();
            }
        }

    addSpellRowClick(spellId) {
        var newSpell = this.dataStore.get(NEW_SPELL_MAP_KEY).get(spellId);
        this.dataStore.set([SELECTED_NEW_SPELL_KEY], newSpell);
        var table = document.getElementById('add-spells-table')
        for (var i = 0; i < table.rows.length; i++){
            table.rows[i].removeAttribute('class');
        }
        document.getElementById(spellId).setAttribute('class','selectedRow');
    }

    async actionTablePopulate() {
        var tabs = document.getElementsByClassName('actions-table');
                for(var i = 0; i < tabs.length; i++) {
                   tabs[i].getElementsByTagName('tbody')[0].innerHTML = '';
                };
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        if (creature.actionMap !== null) {
        var actionMap = new Map(Object.entries(creature.actionMap));
        for (var [key, value] of actionMap) {
            var actionType = value.actionType;
            var actionId = value.objectId;
            var actionTable = document.getElementById((actionType + '-table'));
            var actionBody = actionTable.getElementsByTagName('tbody')[0];
            var actionRow = actionBody.insertRow(-1);
            actionRow.setAttribute('id', actionId);
            actionRow.setAttribute('data-id', actionId);
            var actionCell = actionRow.insertCell(0);
            actionCell.innerHTML = value.objectName;
        }
        this.dataStore.set([ACTION_MAP_KEY], actionMap);
        }
    }

    async actionRowClick(actionId) {
        if (this.dataStore.get(SELECTED_CREATURE_KEY) != '') {
            var action = this.dataStore.get(ACTION_MAP_KEY).get(actionId);
            this.dataStore.set([SELECTED_ACTION_KEY], action);
            var actionTables = document.getElementsByClassName('actions-table');
            for (var h = 0; h < actionTables.length; h++) {
                for (var i = 0; i < actionTables[h].rows.length; i++){
                            actionTables[h].rows[i].removeAttribute('class');
                        }
            }
            document.getElementById(actionId).setAttribute('class','selectedRow');

            document.getElementById('nameActionEdit').value = action.objectName;
            document.getElementById('descriptionActionEdit').value = action.actionDescription;
            document.getElementById('typeEditAction').value = action.actionType;
            document.getElementById('usesEditAction').value = action.uses;
            document.getElementById('rechargeEditAction').value = action.rechargeOn;
            var myOffcanvas = document.getElementById('offcanvasEditAction');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }
    }

    async deleteButton() {
        if(this.dataStore.get(SELECTED_CREATURE_KEY) != '') {
            this.hideElements();
            var warned = false;
            await this.creatureClient.deleteCreature(this.dataStore.get(SELECTED_CREATURE_KEY).objectId, (error) => {
               document.getElementById('offcanvas-warn-body').innerText = error.message;
               var myOffcanvas = document.getElementById('offcanvasWarn');
               var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
               bsOffcanvas.show();
               this.showElements();
               warned = true;
           });
            if (!warned) { location.reload(); };
        }
    }

    async removeSpellButton() {
            var spellMap = this.dataStore.get(SPELL_MAP_KEY);
            var spell = this.dataStore.get(SELECTED_SPELL_KEY);
            spellMap.delete(spell.objectId);
            document.getElementById(spell.objectId).remove();
            this.dataStore.get(SELECTED_CREATURE_KEY).spellMap = this.mapToObj(spellMap);
            document.getElementById('spellName').value = '';
            document.getElementById('spellDescription').value = '';
            document.getElementById('spellHigherLevel').value = '';
            document.getElementById('spellRange').value = '';
            document.getElementById('spellComponents').value = '';
            document.getElementById('spellMaterial').value = '';
            document.getElementById('reaction').value = '';
            document.getElementById('ritualCast').value = '';
            document.getElementById('castingTime').value = '';
            document.getElementById('castingTurns').value = '';
            document.getElementById('spellLevel').value = '';
            document.getElementById('spellSchool').value = '';
            document.getElementById('innateCasts').value = '';
            document.getElementById('close-edit-action').click();
        }

    async removeActionButton() {
        var actionMap = this.dataStore.get(ACTION_MAP_KEY);
        var action = this.dataStore.get(SELECTED_ACTION_KEY);
        actionMap.delete(action.objectId);
        document.getElementById(action.objectId).remove();
        this.dataStore.get(SELECTED_CREATURE_KEY).actionMap = this.mapToObj(actionMap);
        document.getElementById('nameActionEdit').value = '';
        document.getElementById('descriptionActionEdit').value = '';
        document.getElementById('typeEditAction').value = 'standard';
        document.getElementById('usesEditAction').value = '';
        document.getElementById('rechargeEditAction').value = '';
        document.getElementById('close-edit-action').click();
    }

    async updateActionButton() {
        var actionMap = this.dataStore.get(ACTION_MAP_KEY);
        var action = this.dataStore.get(SELECTED_ACTION_KEY);
        action.objectName = document.getElementById('nameActionEdit').value;
        action.actionDescription = document.getElementById('descriptionActionEdit').value;
        action.actionType = document.getElementById('typeEditAction').value;
        action.uses = document.getElementById('usesEditAction').value;
        action.rechargeOn = document.getElementById('rechargeEditAction').value;
        document.getElementById('close-edit-action').click();
        actionMap.set(action.objectId, action);
        this.dataStore.get(SELECTED_CREATURE_KEY).actionMap = this.mapToObj(actionMap);
        this.actionTablePopulate();
        document.getElementById('nameActionEdit').value = '';
        document.getElementById('descriptionActionEdit').value = '';
        document.getElementById('typeEditAction').value = 'standard';
        document.getElementById('usesEditAction').value = '';
        document.getElementById('usesEditAction').value = '';
        document.getElementById('close-edit-action').click();
    }

    async addSpellButton() {
        if (this.dataStore.get(SELECTED_CREATURE_KEY) != '') {
            document.getElementById('spinner-new-spell').hidden = false;
            document.getElementById('add-spells-table').hidden = true;
            document.getElementById('add-spell-btn').hidden = true;
            var myOffcanvas = document.getElementById('offcanvasAddSpell');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
            var newSpellList = await this.spellClient.getMultipleSpells();
            var spellMap = this.dataStore.get(SPELL_MAP_KEY);
            var newSpellMap = new Map(newSpellList.map((obj) => [obj.objectId, obj]));
            this.dataStore.set([NEW_SPELL_MAP_KEY], newSpellMap);
            var spellTable = document.getElementById(('add-spells-table'));
            var spellBody = spellTable.getElementsByTagName('tbody')[0];
            spellBody.innerHTML = '';
            for(var spell of newSpellList) {
                            if (
                                !spellMap.has(spell.objectId)
                            ) {

                                var row = spellBody.insertRow(-1);
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
                    this.sortSpellTable(spellTable);
                    document.getElementById('spinner-new-spell').hidden = true;
                    document.getElementById('add-spells-table').hidden = false;
                    document.getElementById('add-spell-btn').hidden = false;
                }
        }

    addActionButton() {
        if (this.dataStore.get(SELECTED_CREATURE_KEY) != '') {
            var myOffcanvas = document.getElementById('offcanvasNewAction');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }
    }

    addActionFinishButton() {
        var action = {};
        action.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
        action.objectId = uuidv4();
        action.objectName = document.getElementById('nameActionNew').value;
        action.actionDescription = document.getElementById('descriptionActionNew').value;
        action.actionType = document.getElementById('typeEditAction').value;
        action.uses = document.getElementById('usesEditAction').value;
        action.rechargeOn = document.getElementById('usesEditAction').value;
        var actionMap = this.dataStore.get(ACTION_MAP_KEY)
        actionMap.set(action.objectId, action);
        this.dataStore.get(SELECTED_CREATURE_KEY).actionMap = this.mapToObj(actionMap);
        this.actionTablePopulate();
        document.getElementById('close-edit-action').click();
        document.getElementById('nameActionEdit').value = '';
        document.getElementById('descriptionActionEdit').value = '';
        document.getElementById('typeEditAction').value = 'standard';
        document.getElementById('usesEditAction').value = '';
        document.getElementById('usesEditAction').value = '';
        document.getElementById('close-edit-action').click();
    }

    addSpellFinishButton() {
        var spellMap = this.dataStore.get(SPELL_MAP_KEY);
        var spell = this.dataStore.get(SELECTED_NEW_SPELL_KEY);
        spellMap.set(spell.objectId, spell);
        this.dataStore.get(SELECTED_CREATURE_KEY).spellMap = this.mapToObj(spellMap);
        this.spellTablePopulate();
        document.getElementById('close-add-spell').click();
    }

    async updateButton() {
        this.hideElements();
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        if (creature.isPC) {
            creature.objectName = document.getElementById('pcName').value;
        } else {
            creature.objectName = document.getElementById('objectName').value;
        }
        creature.pcLevel = document.getElementById('pcLevel').value;
        creature.sourceBook = document.getElementById('sourceBook').value;
        creature.creatureDescription = document.getElementById('creatureDescription').value;
        creature.size = document.getElementById('size').value;
        creature.type = document.getElementById('type').value;
        creature.subType = document.getElementById('subType').value;
        creature.group = document.getElementById('group').value;
        creature.alignment = document.getElementById('alignment').value;
        creature.armorClass = document.getElementById('armorClass').value;
        creature.armorType = document.getElementById('armorType').value;
        creature.vulnerabilities = document.getElementById('vulnerabilities').value;
        creature.resistances = document.getElementById('resistances').value;
        creature.immunities = document.getElementById('immunities').value;
        creature.conditionImmunities = document.getElementById('conditionImmunities').value;
        creature.hitDice = document.getElementById('hitDice').value;
        creature.hitPoints = document.getElementById('hitPoints').value;
        creature.senses = document.getElementById('senses').value;
        creature.languages = document.getElementById('languages').value;
        creature.challengeRating = document.getElementById('challengeRating').value;
        creature.legendaryDesc = document.getElementById('legendaryDesc').value;
        creature.spellcastingAbility = document.getElementById('spellcastingAbility').value;
        creature.spellSaveDC = document.getElementById('spellSaveDC').value;
        creature.spellAttackModifier = document.getElementById('spellAttackModifier').value;
        var speedMap = new Map;
        speedMap.set('walk', document.getElementById('walkSpeed').value);
        speedMap.set('fly', document.getElementById('flySpeed').value);
        speedMap.set('swim', document.getElementById('burrowSpeed').value);
        speedMap.set('burrow', document.getElementById('walkSpeed').value);
        speedMap.set('climb', document.getElementById('climbSpeed').value);
        speedMap.set('hover', document.getElementById('hoverSpeed').value);
        creature.speedMap = this.mapToObj(speedMap);
        var statMap = new Map;
        statMap.set('strength', document.getElementById('strStat').value);
        statMap.set('dexterity', document.getElementById('dexStat').value);
        statMap.set('constitution', document.getElementById('conStat').value);
        statMap.set('intelligence', document.getElementById('intStat').value);
        statMap.set('wisdom', document.getElementById('wisStat').value);
        statMap.set('charisma', document.getElementById('chaStat').value);
        creature.statMap = this.mapToObj(statMap);
        var saveMap = new Map;
        saveMap.set('strength_save', document.getElementById('strSave').value);
        saveMap.set('dexterity_save', document.getElementById('dexSave').value);
        saveMap.set('constitution_save', document.getElementById('conSave').value);
        saveMap.set('intelligence_save', document.getElementById('intSave').value);
        saveMap.set('wisdom_save', document.getElementById('wisSave').value);
        saveMap.set('charisma_save', document.getElementById('chaSave').value);
        creature.saveMap = this.mapToObj(saveMap);
        var skillsMap = new Map;
        skillsMap.set('acrobatics', document.getElementById('acrobatics').value);
        skillsMap.set('animalHandling', document.getElementById('animalHandling').value);
        skillsMap.set('arcana', document.getElementById('arcana').value);
        skillsMap.set('athletics', document.getElementById('athletics').value);
        skillsMap.set('deception', document.getElementById('deception').value);
        skillsMap.set('history', document.getElementById('history').value);
        skillsMap.set('insight', document.getElementById('insight').value);
        skillsMap.set('intimidation', document.getElementById('intimidation').value);
        skillsMap.set('investigation', document.getElementById('investigation').value);
        skillsMap.set('medicine', document.getElementById('medicine').value);
        skillsMap.set('nature', document.getElementById('nature').value);
        skillsMap.set('perception', document.getElementById('perception').value);
        skillsMap.set('performance', document.getElementById('performance').value);
        skillsMap.set('persuasion', document.getElementById('persuasion').value);
        skillsMap.set('religion', document.getElementById('religion').value);
        skillsMap.set('sleight', document.getElementById('sleight').value);
        skillsMap.set('stealth', document.getElementById('stealth').value);
        skillsMap.set('survival', document.getElementById('survival').value);
        creature.skillsMap = this.mapToObj(skillsMap);
        var spellSlotMap = new Map;
        spellSlotMap.set(1, document.getElementById('ss1').value);
        spellSlotMap.set(2, document.getElementById('ss2').value);
        spellSlotMap.set(3, document.getElementById('ss3').value);
        spellSlotMap.set(4, document.getElementById('ss4').value);
        spellSlotMap.set(5, document.getElementById('ss5').value);
        spellSlotMap.set(6, document.getElementById('ss6').value);
        spellSlotMap.set(7, document.getElementById('ss7').value);
        spellSlotMap.set(8, document.getElementById('ss8').value);
        spellSlotMap.set(9, document.getElementById('ss9').value);
        creature.spellSlots = this.mapToObj(spellSlotMap);
        var warned = false;
        await this.creatureClient.updateCreature(creature, (error) => {
            document.getElementById('offcanvas-warn-body').innerText = error.message;
            var myOffcanvas = document.getElementById('offcanvasWarn');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
            this.showElements();
            warned = true;
        });
        if (!warned) { location.reload(); };
    }


    async cloneButton() {
        var creature = this.dataStore.get(SELECTED_CREATURE_KEY);
        creature.objectName = (creature.objectName + " Clone");
        this.hideElements();
        try {
            await this.creatureClient.createCreature(creature);
            location.reload();
        } catch (error) {
            this.cloneButton();
        }
    }

    filterResetButton() {
        document.getElementById('nameSearch').value = '';
        document.getElementById('pcSearch').value = 'Both';
        document.getElementById('crAboveSearch').value = '';
        document.getElementById('crBelowSearch').value = '';
        document.getElementById('sizeSearch').value = '';
        this.populateTable();
    }

    createButton() {
        var myOffcanvas = document.getElementById('offcanvasCreate');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async createFinishButton() {
        if(document.getElementById('newName').value != '') {
            var creature = {};
            creature.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
            creature.objectName = document.getElementById('newName').value;
            creature.isPC = document.getElementById('pcSwitch').checked;
            if (!document.getElementById('pcSwitch').checked) {
                creature.creatureDescription = document.getElementById('newDesc').value;
                creature.size = document.getElementById('sizeCreate').value;
            }
            this.hideElements();
            document.getElementById('close-btn').click()
            var warned = false;
            var newCreature = await this.creatureClient.createCreature(creature, (error) => {
               document.getElementById('offcanvas-warn-body').innerText = error.message;
               var myOffcanvas = document.getElementById('offcanvasWarn');
               var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                bsOffcanvas.show();
                this.showElements();
                warned = true;
            });
            if(!warned) {
                document.getElementById('newName').value = '';
                document.getElementById('newDesc').value = '';
                document.getElementById('sizeCreate').value = '';
                document.getElementById('pcSwitch').checked = false;
                this.dataStore.set([CREATURE_LIST_KEY], await this.creatureClient.getMultipleCreatures());
                var creatureMap = new Map(this.dataStore.get([CREATURE_LIST_KEY]).map((obj) => [obj.objectId, obj]));
                this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
                await this.populateTable();
                this.showElements();
                this.creatureRowClick(newCreature.objectId);
            };
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
                this.hideElements();
                document.getElementById('close-import-btn').click()
                var warned = false;
                var newCreature = await this.creatureClient.createTemplate(slug, (error) => {
                    document.getElementById('offcanvas-warn-body').innerText = error.message;
                    var myOffcanvas = document.getElementById('offcanvasWarn');
                    var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                    bsOffcanvas.show();
                    this.showElements();
                    warned = true;
                });
                if (!warned) {
                    this.dataStore.set([CREATURE_LIST_KEY], await this.creatureClient.getMultipleCreatures());
                    var creatureMap = new Map(this.dataStore.get([CREATURE_LIST_KEY]).map((obj) => [obj.objectId, obj]));
                    this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
                    await this.populateTable();
                    this.showElements();
                    this.creatureRowClick(newCreature.objectId);
                };
        }
    }

    async searchButton() {
        this.dataStore.set([SELECTED_TEMPLATE_KEY], '');
        document.getElementById('template-table').hidden = true;
        document.getElementById('spinner-side').hidden = false;
        var templates = await this.creatureClient.searchTemplate(document.getElementById('templateSearch').value, document.getElementById('limit').value)
        document.getElementById('template-table').hidden = false;
        document.getElementById('spinner-side').hidden = true;
        var table = document.getElementById("template-table");
                    var oldTableBody = table.getElementsByTagName('tbody')[0];
                    var newTableBody = document.createElement('tbody');
                    var creatureList = templates;
                    if(creatureList) {
                        creatureList.sort((a, b) => a.name.localeCompare(b.name));
                        for(var templateCreature of creatureList) {
                            if (
                                !templateCreature.resourceExists
                            ) {

                                var row = newTableBody.insertRow(-1);
                                row.setAttribute('id', templateCreature.slug);
                                row.setAttribute('data-id', templateCreature.slug);
                                var cell1 = row.insertCell(0);
                                var cell2 = row.insertCell(1);
                                var cell3 = row.insertCell(2);
                                var cell4 = row.insertCell(3);
                                var cell5 = row.insertCell(4);
                                cell1.innerHTML = templateCreature.name;
                                cell2.innerHTML = templateCreature.cr;
                                cell3.innerHTML = templateCreature.size;
                                cell4.innerHTML = templateCreature.type;
                                cell5.innerHTML = templateCreature.document__title;
                            }
                        }
                    }
                    oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    pcSwitch() {
        document.getElementById('pcSwitch').checked
                                        ?  (document.getElementById('newDesc').disabled = true,
                                            document.getElementById('sizeCreate').disabled = true)
                                        : (document.getElementById('newDesc').disabled = false,
                                          document.getElementById('sizeCreate').disabled = false)
    }

    rebaseSpells() {
        var myOffcanvas = document.getElementById('offcanvasRebaseWarn');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async rebaseSpellsContinue() {
        var spellMap = this.dataStore.get(SPELL_MAP_KEY);
        this.hideElements();
        for (var [key, value] of spellMap) {
            try {
                var rebaseSpell = await this.spellClient.getSingleSpell(key);
            } catch (error) {
                spellMap.delete(key);
            }
            spellMap.set(key, rebaseSpell);
        }
        this.dataStore.get(SELECTED_CREATURE_KEY).spellMap = this.mapToObj(spellMap);
        this.updateButton();
        document.getElementById('rebase-warn-close').click()
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
     const creatureLibraryScripts = new CreatureLibraryScripts();
     creatureLibraryScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
