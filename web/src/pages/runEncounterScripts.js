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
const CREATURE_MAP_KEY = 'creature-map-key';
const SELECTED_CREATURE_ID_KEY = 'selected-creature-id-key';
const TARGET_CREATURE_ID_KEY = 'taarget-creature-id-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SESSION_LIST_KEY]: '',
    [SELECTED_SESSION_ID_KEY]: '0',
    [ENCOUNTER_LIST_KEY]: '',
    [SELECTED_ENCOUNTER_ID_KEY]: '',
    [ENCOUNTER_KEY]: '',
    [CREATURE_MAP_KEY]: '',
    [SELECTED_CREATURE_ID_KEY]: '',
    [TARGET_CREATURE_ID_KEY]: '',
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
                                'hideElementsPlay', 'showElementsPlay',
                                'rollInitiative', 'assignInitiative',
                                'goEncounter', 'populateAccordions',
                                'nextTurn', 'viewStats',
                                'modCalc', 'damageValue',
                                'applyDamage', 'healValue',
                                'applyHeal'
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
    document.getElementById('initiative-btn').addEventListener('click', this.assignInitiative);
    document.getElementById('go-btn').addEventListener('click', this.goEncounter);
    document.getElementById('next-turn-btn').addEventListener('click', this.nextTurn);
    document.getElementById('apply-damage-btn').addEventListener('click', this.applyDamage);
    document.getElementById('apply-heal-btn').addEventListener('click', this.applyHeal);
    document.getElementById('session-list').addEventListener('change', (event) => {
                                                if (event.target.closest('select')) {this.populateEncounters(event.target.value)}});
    document.getElementById('offcanvas-init-body').addEventListener('click', (event) => {
                                                    if (event.target.closest('button')) {this.rollInitiative(event.target.dataset.id)}});
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
                if(!encounter.turnQueue && encounter.creatureMap) {
                    this.dataStore.set([CREATURE_MAP_KEY], new Map(Object.entries(encounter.creatureMap)));
                    this.showElements();
                    var myOffcanvas = document.getElementById('offcanvasInitiative');
                    var initBody = document.getElementById('offcanvas-init-body');
                    initBody.innerHTML = '';
                    for(var [key, value] of new Map(Object.entries(encounter.creatureMap))) {
                        var pcStatus = '';
                        if (value.isPC) { pcStatus = 'PC'}
                        const initRow = `
                        <div class = "row">
                                        <div class="col-sm-4 mb-3">
                                            <label for="name_${value.encounterCreatureId}" class="form-label">Name</label>
                                            <input class="form-control" id="name_${value.encounterCreatureId}" readonly value = "${value.objectName}">
                                        </div>
                                        <div class="col-sm-2 mb-3">
                                            <label for="roll_${value.encounterCreatureId}" class="form-label">Roll</label>
                                            <input class="form-control initiative-roll" id="roll_${value.encounterCreatureId}" data-id = "${value.encounterCreatureId}" type="number" data-bind="value:replyNumber">
                                        </div>
                                        <div class="col-sm-2 mb-3">
                                            <label for="init_${value.encounterCreatureId}" class="form-label">Initiative</label>
                                            <input class="form-control initiative-rank" id="init_${value.encounterCreatureId}" data-id = "${value.encounterCreatureId}" type="number" min="1" data-bind="value:replyNumber">
                                        </div>
                                        <div class="col-sm-2 mb-3">
                                            <label for="pc_${value.encounterCreatureId}" class="form-label">PC</label>
                                            <div class = "row">
                                                <div class = "col">
                                                    <input class="form-control" id="pc_${value.encounterCreatureId}" readonly value = "${pcStatus}">
                                                </div>
                                                <div class = "col">
                                                    <button type="button" class="btn btn-outline-dark align-bottom" data-id = "${value.encounterCreatureId}" id="roll-btn-_${value.encounterCreatureId}">Roll</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                        `
                        initBody.insertAdjacentHTML('afterbegin', initRow);
                    }
                    var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                    bsOffcanvas.show();
                } else if(encounter.turnQueue) {
                    this.populateAccordions();
                } else {
                    this.showElements();
                    document.getElementById('offcanvas-warn-body').innerText = "This encounter has no creatures. Please add some before trying to launch it.";
                   var myOffcanvas = document.getElementById('offcanvasWarn');
                   var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                   bsOffcanvas.show();
                }

            }
        }
    }

    rollInitiative(encounterCreatureId) {
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId);
        var target = 'roll_' + encounterCreatureId;
        document.getElementById(target).value = Math.floor(Math.random() * (20) + 1) + this.modCalc(creature?.statMap?.dexterity);
    }

    assignInitiative() {
        var initList = [];
        for(var result of document.getElementsByClassName('initiative-roll')) {
            var entry = {};
            entry.roll = result.value;
            entry.objectId = result.dataset.id;
            initList.push(entry);
        }
        initList = initList.sort(function(a,b) {
            return b.roll - a.roll
        });
        var rollMap = new Map();
        var i = 1;
        for(var entry of initList) {
            rollMap.set(entry.objectId, i);
            i ++;
        }
        for(var [key, value] of rollMap) {
            document.getElementById('init_' + key).value = value;
        }
    }

    async goEncounter() {
        var initList = [];
        for(var result of document.getElementsByClassName('initiative-rank')) {
            var entry = {};
            entry.rank = result.value;
            entry.objectId = result.dataset.id;
            initList.push(entry);
        }
        initList = initList.sort(function(a,b) {
                    return a.rank - b.rank
                });
        console.log(initList)
        var queueList = [];
        for(var element of initList) {
            queueList.push(element.objectId);
        }
        var warned = false;
        var encounter = await this.runClient.setInitiative(this.dataStore.get(ENCOUNTER_KEY).objectId,
                                                     queueList, (error) => {
                           document.getElementById('offcanvas-warn-body').innerText = error.message;
                           var myOffcanvas = document.getElementById('offcanvasWarn');
                           var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
                           bsOffcanvas.show();
                           this.showElements();
                           warned = true;
        });
        this.dataStore.set([ENCOUNTER_KEY], encounter);
        this.hideElements();
        document.getElementById('offcanvas-init-close').click();
        this.populateAccordions();
    }

    populateAccordions() {
        var encounter = this.dataStore.get([ENCOUNTER_KEY]);
        var creatureMap = new Map(Object.entries(encounter.creatureMap));
        this.dataStore.set([CREATURE_MAP_KEY], creatureMap);
        var accordion = document.getElementById('creatureAccordion');
        accordion.innerHTML = '';
        var encounter = this.dataStore.get(ENCOUNTER_KEY);
        var turnQueue = encounter.turnQueue;
        for(var i = turnQueue.length; i > 0; i --) {
            var encounterCreatureId = turnQueue[i - 1];
            var creature = creatureMap.get(encounterCreatureId);
            var creatureName = creature.encounterCreatureName;
            if (creature.isPC) {
                 creatureName = "(PC)      " + creatureName
            }
            if (creature.encounterCreatureId === encounter.topOfOrder) {
                 creatureName = creatureName + "     (Top of Order)"
            }
            var status = "Okay";
            if (creature.knockedOut) {
                status = "Unconscious";
            };
            if (creature.dead) {
                status = "Dead";
            };
            var accordionItem = `
               <div class="accordion-item">
                   <h2 class="accordion-header" id="heading_${encounterCreatureId}">
                       <button id="acc_button_${encounterCreatureId}" class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_${encounterCreatureId}" aria-expanded="false" aria-controls="collapse_${encounterCreatureId}">
                           ${creatureName}
                       </button>
                   </h2>
                   <div id="collapse_${encounterCreatureId}" class="accordion-collapse collapse" aria-labelledby="heading_${encounterCreatureId}" data-bs-parent="#creatureAccordion">
                       <div class="accordion-body">
                            <div class = "row">
                                <div class="mb-3 col">
                                        <button type="button" class="stats-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="stats_${encounterCreatureId}">View Details</button>
                                </div>
                                <div class="mb-3 col">

                                        <button type="button" class="damage-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="damage_${encounterCreatureId}">Damage</button>
                                        <button type="button" class="heal-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="heal_${encounterCreatureId}">Heal</button>
                                        <button type="button" class="effect-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="effect_${encounterCreatureId}">Add Effect</button>
                                        <button type="button" class="ko-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="ko_${encounterCreatureId}">Knock Out</button>
                                        <button type="button" class="kill-btn btn btn-outline-dark" data-id = "${encounterCreatureId}" id="kill_${encounterCreatureId}">Kill</button>
                                </div>
                            </div>
                            <div class = "row">
                                <div class="mb-3 col">
                                    <label for="hp_${encounterCreatureId}" class="form-label">HP</label>
                                    <input class="form-control" id="hp_${encounterCreatureId}" readonly value = "${creature.currentHitPoints} / ${creature.hitPoints}">
                                </div>
                                <div class="mb-3 col">
                                    <label for="status_${encounterCreatureId}" class="form-label">Status</label>
                                    <input class="form-control" id="status_${encounterCreatureId}" readonly value = "${status}">
                                </div>
                                <div class="mb-3 col">
                                    <label for="ac_${encounterCreatureId}" class="form-label">AC</label>
                                    <input class="form-control" id="ac_${encounterCreatureId}" readonly value = "${creature.armorClass}">
                                </div>
                                <div class="mb-3 col">
                                </div>
                            </div>
                       </div>
                   </div>
               </div>`
               accordion.insertAdjacentHTML('afterbegin', accordionItem);
        }
        accordion.hidden = false;
        if(this.dataStore.get(SELECTED_CREATURE_ID_KEY)) {
            document.getElementById('collapse_' + this.dataStore.get(SELECTED_CREATURE_ID_KEY)).classList.add('show');
            document.getElementById('acc_button_' + this.dataStore.get(SELECTED_CREATURE_ID_KEY)).classList.remove('collapsed');
            this.dataStore.set([SELECTED_CREATURE_ID_KEY], '');
        } else {
            document.getElementById('collapse_' + turnQueue[0]).classList.add('show');
            document.getElementById('acc_button_' + turnQueue[0]).classList.remove('collapsed');
        }
        document.getElementById('roundNumber').innerHTML = `Round: ${encounter.encounterRound}`
        for(var btn of document.getElementsByClassName('stats-btn')) {
            btn.addEventListener('click', (event) => {
                                    if (event.target.closest('button')) {this.viewStats(event.target.dataset.id)}});
        }
        for(var btn of document.getElementsByClassName('damage-btn')) {
                    btn.addEventListener('click', (event) => {
                                            if (event.target.closest('button')) {this.damageValue(event.target.dataset.id)}});
                }
        for(var btn of document.getElementsByClassName('heal-btn')) {
            btn.addEventListener('click', (event) => {
                                    if (event.target.closest('button')) {this.healValue(event.target.dataset.id)}});
        }
        this.hideElementsPlay();
    }

    async nextTurn() {
        document.getElementById('spinner').hidden = false;
        document.getElementById('spinner-label').hidden = false;
        document.getElementById('creatureAccordion').hidden = true;
        var encounter = this.dataStore.get(ENCOUNTER_KEY);
        encounter = await this.runClient.nextTurn(encounter.objectId);
        this.dataStore.set([ENCOUNTER_KEY], encounter);
        document.getElementById('spinner').hidden = true;
        document.getElementById('spinner-label').hidden = true;
        document.getElementById('creatureAccordion').hidden = false;
        this.populateAccordions();
    }

    async damageValue(targetID) {
        this.dataStore.set([SELECTED_CREATURE_ID_KEY], targetID);
        this.dataStore.set([TARGET_CREATURE_ID_KEY], targetID);
        var myOffcanvas = document.getElementById('offcanvasDamage');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async applyDamage() {
        document.getElementById('spinner').hidden = false;
        document.getElementById('spinner-label').hidden = false;
        document.getElementById('creatureAccordion').hidden = true;
        var encounter = this.dataStore.get(ENCOUNTER_KEY);
        var damageValue = document.getElementById('damage-value').value;
        if (damageValue == '' || damageValue < 0) {
            damageValue = 0;
        }
        document.getElementById('offcanvas-damage-close').click();
        encounter = await this.runClient.applyDamage(
                encounter.objectId, this.dataStore.get(TARGET_CREATURE_ID_KEY), damageValue);
        this.dataStore.set([ENCOUNTER_KEY], encounter);
        document.getElementById('spinner').hidden = true;
        document.getElementById('spinner-label').hidden = true;
        document.getElementById('creatureAccordion').hidden = false;
        this.populateAccordions();
    }

        async healValue(targetID) {
            this.dataStore.set([SELECTED_CREATURE_ID_KEY], targetID);
            this.dataStore.set([TARGET_CREATURE_ID_KEY], targetID);
            var myOffcanvas = document.getElementById('offcanvasHeal');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
        }

        async applyHeal() {
            document.getElementById('spinner').hidden = false;
            document.getElementById('spinner-label').hidden = false;
            document.getElementById('creatureAccordion').hidden = true;
            var encounter = this.dataStore.get(ENCOUNTER_KEY);
            var damageValue = document.getElementById('heal-value').value;
            if (damageValue == '' || damageValue < 0) {
                damageValue = 0;
            }
            document.getElementById('offcanvas-heal-close').click();
            encounter = await this.runClient.heal(
                    encounter.objectId, this.dataStore.get(TARGET_CREATURE_ID_KEY), damageValue);
            this.dataStore.set([ENCOUNTER_KEY], encounter);
            document.getElementById('spinner').hidden = true;
            document.getElementById('spinner-label').hidden = true;
            document.getElementById('creatureAccordion').hidden = false;
            this.populateAccordions();
        }

    viewStats(encounterCreatureId) {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = creatureMap.get(encounterCreatureId);
        document.getElementById('stats-name').innerText = creature.encounterCreatureName;
        document.getElementById('creatureName').value = creature.objectName;
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
            document.getElementById('ss1').innerHTML = spellSlotMap.get(1) || '';;
            document.getElementById('ss2').innerHTML = spellSlotMap.get(2) || '';;
            document.getElementById('ss3').innerHTML = spellSlotMap.get(3) || '';;
            document.getElementById('ss4').innerHTML = spellSlotMap.get(4) || '';;
            document.getElementById('ss5').innerHTML = spellSlotMap.get(5) || '';;
            document.getElementById('ss6').innerHTML = spellSlotMap.get(6) || '';;
            document.getElementById('ss7').innerHTML = spellSlotMap.get(7) || '';;
            document.getElementById('ss8').innerHTML = spellSlotMap.get(8) || '';;
            document.getElementById('ss9').innerHTML = spellSlotMap.get(9) || '';;
        }
        document.getElementById('walkSpeed').innerHTML = creature.speedMap?.walk || '';
        document.getElementById('flySpeed').innerHTML = creature.speedMap?.fly || '';
        document.getElementById('swimSpeed').innerHTML = creature.speedMap?.swim || '';
        document.getElementById('burrowSpeed').innerHTML = creature.speedMap?.burrow || '';
        document.getElementById('climbSpeed').innerHTML = creature.speedMap?.climb || '';
        document.getElementById('hoverSpeed').innerHTML = creature.speedMap?.hover || '';
        document.getElementById('strStat').innerHTML = this.modCalc(creature.statMap?.strength);
        document.getElementById('dexStat').innerHTML = this.modCalc(creature.statMap?.dexterity);
        document.getElementById('conStat').innerHTML = this.modCalc(creature.statMap?.constitution);
        document.getElementById('intStat').innerHTML = this.modCalc(creature.statMap?.intelligence);
        document.getElementById('wisStat').innerHTML = this.modCalc(creature.statMap?.wisdom);
        document.getElementById('chaStat').innerHTML = this.modCalc(creature.statMap?.charisma);
        document.getElementById('strSave').innerHTML = creature.saveMap?.strength_save || '';
        document.getElementById('dexSave').innerHTML = creature.saveMap?.dexterity_save || '';
        document.getElementById('conSave').innerHTML = creature.saveMap?.constitution_save || '';
        document.getElementById('intSave').innerHTML = creature.saveMap?.intelligence_save || '';
        document.getElementById('wisSave').innerHTML = creature.saveMap?.wisdom_save || '';
        document.getElementById('chaSave').innerHTML = creature.saveMap?.charisma_save || '';
        document.getElementById('acrobatics').innerHTML = creature.skillsMap?.acrobatics || '';
        document.getElementById('animalHandling').innerHTML = creature.skillsMap?.animalHandling || '';
        document.getElementById('arcana').innerHTML = creature.skillsMap?.arcana || '';
        document.getElementById('athletics').innerHTML = creature.skillsMap?.athletics || '';
        document.getElementById('deception').innerHTML = creature.skillsMap?.deception || '';
        document.getElementById('history').innerHTML = creature.skillsMap?.history || '';
        document.getElementById('insight').innerHTML = creature.skillsMap?.insight || '';
        document.getElementById('intimidation').innerHTML = creature.skillsMap?.intimidation || '';
        document.getElementById('investigation').innerHTML = creature.skillsMap?.investigation || '';
        document.getElementById('medicine').innerHTML = creature.skillsMap?.medicine || '';
        document.getElementById('nature').innerHTML = creature.skillsMap?.nature || '';
        document.getElementById('perception').innerHTML = creature.skillsMap?.perception || '';
        document.getElementById('performance').innerHTML = creature.skillsMap?.performance || '';
        document.getElementById('persuasion').innerHTML = creature.skillsMap?.persuasion || '';
        document.getElementById('religion').innerHTML = creature.skillsMap?.religion || '';
        document.getElementById('sleight').innerHTML = creature.skillsMap?.sleight || '';
        document.getElementById('stealth').innerHTML = creature.skillsMap?.stealth || '';
        document.getElementById('survival').innerHTML = creature.skillsMap?.survival || '';
        var myOffcanvas = document.getElementById('offcanvasStats');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    modCalc(stat) {
        var modifier = 0;
        if (stat) {
            modifier = Math.floor((stat - 10)/2)
        }
        return modifier;
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
        var show = document.getElementsByClassName('show-while-playing');
                                for(var i = 0; i < show.length; i++) {
                                    show[i].hidden = false;
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
