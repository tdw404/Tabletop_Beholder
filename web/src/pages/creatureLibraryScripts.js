import AuthClient from "../api/authClient";
import CreatureClient from "../api/creatureClient"
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";
import { v4 as uuidv4 } from 'uuid';

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const CREATURELIST_KEY = 'creature-list';
const CREATUREMAP_KEY = 'creature-map';
const SELECTED_CREATURE_KEY = 'selected-creature-key';
const SELECTED_TEMPLATE_KEY = 'selected-template-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [CREATURELIST_KEY]: [],
    [CREATUREMAP_KEY]: '',
    [SELECTED_CREATURE_KEY]: '',
    [SELECTED_TEMPLATE_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class CreatureLibraryScripts extends BindingClass {
    constructor() {
        super();
        this.client = new AuthClient();
        this.creatureClient = new CreatureClient();
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
        //alert(uuidv4());
            const{email, name} = await this.client.getIdentity().then(result => result);
            this.dataStore.set([COGNITO_EMAIL_KEY], email);
            this.dataStore.set([COGNITO_NAME_KEY], name);
            this.dataStore.set([CREATURELIST_KEY], await this.creatureClient.getMultipleCreatures());
            const creatureMap = new Map(this.dataStore.get([CREATURELIST_KEY]).map((obj) => [obj.objectId, obj]));
            this.dataStore.set([CREATUREMAP_KEY], creatureMap);
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
        } else {
            window.location.href = "index.html";
        }
    }

    async populateTable() {
            var table = document.getElementById("creature-table");
            var oldTableBody = table.getElementsByTagName('tbody')[0];
            var newTableBody = document.createElement('tbody');
            var creatureList = this.dataStore.get(CREATURELIST_KEY);
            creatureList.sort((a, b) => a.objectName.localeCompare(b.objectName));
//            const nameSearch = document.getElementById('nameSearch').value;
//            const levelSearch = document.getElementById('levelSearch').value;
//            const schoolSearch = document.getElementById('schoolSearch').value;
            for(const creature of creatureList) {
                if (
                    1 == 1
                ) {

                    var row = newTableBody.insertRow(-1);
                    row.setAttribute('id', creature.objectId);
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
                    var createClickHandler = function(row, dataStore) {
                        return function() {
                            for (var i = 0; i < table.rows.length; i++){
                                table.rows[i].removeAttribute('class');
                            }
                            row.setAttribute('class','selectedRow')
                            if (creature.isPC) {
                                var show = document.getElementsByClassName('pc-tab');
                                                for(var i = 0; i < show.length; i++) {
                                                    show[i].hidden = false;
                                                }
                                var hide = document.getElementsByClassName('npc-tab');
                                                for(var i = 0; i < hide.length; i++) {
                                                    hide[i].hidden = true;
                                                }
                            } else {
                                var show = document.getElementsByClassName('npc-tab');
                                                for(var i = 0; i < show.length; i++) {
                                                    show[i].hidden = false;
                                                }
                                var hide = document.getElementsByClassName('pc-tab');
                                                for(var i = 0; i < hide.length; i++) {
                                                    hide[i].hidden = true;
                                                }
                            }
                            document.getElementById('creatureNameBig').innerText = creature.objectName;
                            document.getElementById('objectName').value = creature.objectName;
                            document.getElementById('sourceBook').value = creature.sourceBook;
                            document.getElementById('creatureDescription').value = creature.creatureDescription;
                            document.getElementById('size').value = creature.size;
                            document.getElementById('type').value = creature.type;
                            document.getElementById('subType').value = creature.subType;
                            document.getElementById('group').value = creature.group;
                            document.getElementById('alignment').value = creature.alignment;
                            document.getElementById('armorClass').value = creature.armorClass;
                            document.getElementById('armorType').value = creature.armorType;
                            document.getElementById('hitPoints').value = creature.hitPoints;
                            document.getElementById('hitDice').value = creature.hitDice;
                            document.getElementById('walkSpeed').value = creature.speedMap.walk;
                            document.getElementById('flySpeed').value = creature.speedMap.fly;
                            document.getElementById('swimSpeed').value = creature.speedMap.swim;
                            document.getElementById('burrowSpeed').value = creature.speedMap.burrow;
                            document.getElementById('climbSpeed').value = creature.speedMap.climb;
                            document.getElementById('hoverSpeed').value = creature.speedMap.hover;
                            document.getElementById('strStat').value = creature.statMap.strength;
                            document.getElementById('dexStat').value = creature.statMap.dexterity;
                            document.getElementById('conStat').value = creature.statMap.constitution;
                            document.getElementById('intStat').value = creature.statMap.intelligence;
                            document.getElementById('wisStat').value = creature.statMap.wisdom;
                            document.getElementById('chaStat').value = creature.statMap.charisma;
                            document.getElementById('strSave').value = creature.saveMap.strength_save;
                            document.getElementById('dexSave').value = creature.saveMap.dexterity_save;
                            document.getElementById('conSave').value = creature.saveMap.constitution_save;
                            document.getElementById('intSave').value = creature.saveMap.intelligence_save;
                            document.getElementById('wisSave').value = creature.saveMap.wisdom_save;
                            document.getElementById('chaSave').value = creature.saveMap.charisma_save;
                            document.getElementById('acrobatics').value = creature.skillsMap.acrobatics;
                            document.getElementById('animalHandling').value = creature.skillsMap.animalHandling;
                            document.getElementById('arcana').value = creature.skillsMap.arcana;
                            document.getElementById('athletics').value = creature.skillsMap.athletics;
                            document.getElementById('deception').value = creature.skillsMap.deception;
                            document.getElementById('history').value = creature.skillsMap.history;
                            document.getElementById('insight').value = creature.skillsMap.insight;
                            document.getElementById('intimidation').value = creature.skillsMap.intimidation;
                            document.getElementById('investigation').value = creature.skillsMap.investigation;
                            document.getElementById('medicine').value = creature.skillsMap.medicine;
                            document.getElementById('nature').value = creature.skillsMap.nature;
                            document.getElementById('perception').value = creature.skillsMap.perception;
                            document.getElementById('performance').value = creature.skillsMap.performance;
                            document.getElementById('persuasion').value = creature.skillsMap.persuasion;
                            document.getElementById('religion').value = creature.skillsMap.religion;
                            document.getElementById('sleight').value = creature.skillsMap.sleight;
                            document.getElementById('stealth').value = creature.skillsMap.stealth;
                            document.getElementById('survival').value = creature.skillsMap.survival;
                            dataStore.set([SELECTED_CREATURE_KEY], creature.objectId);

                        };
                    };
                    row.onclick = createClickHandler(row, this.dataStore);
                }
            }
            oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
    }

    async deleteButton() {
        const objectId = this.dataStore.get(SELECTED_CREATURE_KEY);
        if(objectId != '') {
            this.hideElements();
            await this.creatureClient.deleteCreature(objectId);
            location.reload();
        }
    }

    async updateButton() {
        this.hideElements();
//        const creature = (this.dataStore.get(CREATUREMAP_KEY)).get(this.dataStore.get(SELECTED_CREATURE_KEY));;
//        creature.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
//        creature.objectId = this.dataStore.get(SELECTED_CREATURE_KEY);
//        creature.objectName = document.getElementById('objectName').value;
//        creature.creatureDescription = document.getElementById('creatureDescription').value;
//        creature.creatureHigherLevel = document.getElementById('creatureHigherLevel').value;
//        creature.creatureRange = document.getElementById('creatureRange').value;
//        creature.creatureComponents = document.getElementById('creatureComponents').value;
//        creature.creatureMaterial = document.getElementById('creatureMaterial').value;
//        creature.reaction = document.getElementById('reaction').value;
//        if (document.getElementById('ritualCast').value.equals == "yes") {
//            creature.ritualCast = true;
//        } else if (document.getElementById('ritualCast').value.equals == "no") {
//            creature.ritualCast = false;
//        } else {
//            creature.ritualCast = '';
//        }
//        creature.castingTime = document.getElementById('castingTime').value;
//        creature.castingTurns = document.getElementById('castingTurns').value;
//        creature.creatureLevel = document.getElementById('creatureLevel').value;
//        creature.creatureSchool = document.getElementById('creatureSchool').value;
//        creature.innateCasts = document.getElementById('innateCasts').value;
//
//        try {
//            await this.creatureClient.updateCreature(creature);
//            location.reload();
//        } catch (error) {
//            this.showElements();
//            document.getElementById('offcanvas-warn-body').innerText = "You already have a creature with the name " + document.getElementById('objectName').value + " in your library."
//            var myOffcanvas = document.getElementById('offcanvasWarn');
//            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//            bsOffcanvas.show();
//        }

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
//        var myOffcanvas = document.getElementById('offcanvasCreate');
//        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
//        bsOffcanvas.show();
    }

    async createFinishButton() {
//        if(document.getElementById('newName').value != '') {
//            //this.creatureClient = this.dataStore.get(CREATURE_CLIENT_KEY);
//            const creature = {};
//            creature.userEmail = this.dataStore.get(COGNITO_EMAIL_KEY);
//            creature.objectName = document.getElementById('newName').value;
//            creature.creatureDescription = document.getElementById('newDesc').value;
//            creature.creatureLevel = document.getElementById('newLevel').value;
//            creature.creatureSchool = document.getElementById('newSchool').value;
//
//            try {
//                this.hideElements();
//                document.getElementById('close-btn').click()
//                const newCreature = await this.creatureClient.createCreature(creature);
//                document.getElementById('newName').value = '';
//                document.getElementById('newDesc').value = '';
//                document.getElementById('newLevel').value = '';
//                document.getElementById('newSchool').value = '';
//                this.dataStore.set([CREATURELIST_KEY], await this.creatureClient.getMultipleCreatures());
//                await this.populateTable();
//                this.showElements();
//                document.getElementById(newCreature.objectId).click();
//            } catch (error) {
//                this.showElements();
//                document.getElementById('offcanvas-warn-body').innerText = "You already have a creature with the name " + document.getElementById('objectName').value + " in your library."
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
//        const slug = this.dataStore.get(SELECTED_TEMPLATE_KEY);
//        if(!slug=='') {
//            try {
//                this.hideElements();
//                document.getElementById('close-import-btn').click()
//                const newCreature = await this.creatureClient.createTemplate(slug);
//                this.dataStore.set([CREATURELIST_KEY], await this.creatureClient.getMultipleCreatures());
//                await this.populateTable();
//                this.showElements();
//                document.getElementById(newCreature.objectId).click();
//            } catch (error) {
//                this.showElements();
//                document.getElementById('offcanvas-warn-body').innerText = "You already have a creature with the name " + document.getElementById('objectName').value + " in your library."
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
//        const templates = await this.creatureClient.searchTemplate(document.getElementById('templateSearch').value, document.getElementById('limit').value)
//        document.getElementById('template-table').hidden = false;
//        document.getElementById('spinner-side').hidden = true;
//        var table = document.getElementById("template-table");
//                    var oldTableBody = table.getElementsByTagName('tbody')[0];
//                    var newTableBody = document.createElement('tbody');
//                    var creatureList = templates;
//                    creatureList.sort((a, b) => a.name.localeCompare(b.name));
//                    for(const templateCreature of creatureList) {
//                        if (
//                            !templateCreature.resourceExists
//                        ) {
//
//                            var row = newTableBody.insertRow(-1);
//                            row.setAttribute('id', templateCreature.slug);
//                            var cell1 = row.insertCell(0);
//                            var cell2 = row.insertCell(1);
//                            var cell3 = row.insertCell(2);
//                            cell1.innerHTML = templateCreature.name;
//                            cell2.innerHTML = templateCreature.level;
//                            cell3.innerHTML = templateCreature.document__title
//                            var createClickHandler = function(row, dataStore) {
//                                return function() {
//                                    for (var i = 0; i < table.rows.length; i++){
//                                        table.rows[i].removeAttribute('class');
//                                    }
//                                    row.setAttribute('class','selectedRow')
//                                    dataStore.set([SELECTED_TEMPLATE_KEY], templateCreature.slug);
//
//                                };
//                            };
//                            row.onclick = createClickHandler(row, this.dataStore);
//                        }
//                    }
//                    oldTableBody.parentNode.replaceChild(newTableBody, oldTableBody);
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
     const creatureLibraryScripts = new CreatureLibraryScripts();
     creatureLibraryScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
