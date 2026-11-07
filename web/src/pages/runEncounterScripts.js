import AuthClient from "../api/authClient";
import SessionClient from "../api/sessionClient";
import EncounterClient from "../api/encounterClient";
import RunEncounterClient from "../api/runEncounterClient";
import NavbarProvider from"../components/navbarProvider";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";
import { v4 as uuidv4 } from 'uuid';

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const SESSION_LIST_KEY = 'session-list-key';
const SELECTED_SESSION_ID_KEY = 'selected-session-id-key';
const SELECTED_ENCOUNTER_ID_KEY = 'selected-encounter-id-key';
const ENCOUNTER_KEY = 'encounter-key';
const CREATURE_MAP_KEY = 'creature-map-key';
const SELECTED_CREATURE_ID_KEY = 'selected-creature-id-key';
const TARGET_CREATURE_ID_KEY = 'target-creature-id-key';
const SELECTED_EFFECT_ID_KEY = 'selected-effect-id-key';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
    [SESSION_LIST_KEY]: '',
    [SELECTED_SESSION_ID_KEY]: '0',
    [SELECTED_ENCOUNTER_ID_KEY]: '',
    [ENCOUNTER_KEY]: '',
    [CREATURE_MAP_KEY]: '',
    [SELECTED_CREATURE_ID_KEY]: '',
    [TARGET_CREATURE_ID_KEY]: '',
    [SELECTED_EFFECT_ID_KEY]: '',
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
                                'assignInitiative',
                                'goEncounter', 'populateAccordions',
                                'nextTurn', 'viewStats',
                                'modCalc', 'damageValue',
                                'applyDamage', 'healValue',
                                'applyHeal', 'knockOut',
                                'kill', 'populateSpells',
                                'sortSpellTable', 'populateActions',
                                'sortActionTable', 'populateEffects',
                                'sortEffectTable', 'viewSpell',
                                'viewAction', 'addEffect',
                                'addEffectFinish', 'viewEffect',
                                'removeEffect', 'showNotifications'
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
            const encounterId = new URLSearchParams(location.search).get('encounter');
            await this.populateEncounters(document.getElementById('session-list').value);
            if (encounterId) await this.launchEncounter(encounterId);
        }
    }

    attachEventListeners() {
    document.getElementById('launch-session-btn').addEventListener('click', () => this.launchEncounter());
    document.getElementById('initiative-btn').addEventListener('click', this.assignInitiative);
    document.getElementById('go-btn').addEventListener('click', this.goEncounter);
    document.getElementById('next-turn-btn').addEventListener('click', this.nextTurn);
    document.getElementById('apply-damage-btn').addEventListener('click', this.applyDamage);
    document.getElementById('apply-heal-btn').addEventListener('click', this.applyHeal);
    document.getElementById('apply-effect-btn').addEventListener('click', this.addEffectFinish);
    document.getElementById('remove-effect-btn').addEventListener('click', this.removeEffect);
    document.getElementById('notifications-btn').addEventListener('click', this.showNotifications);
    document.getElementById('session-list').addEventListener('change', (event) => {
                                                if (event.target.closest('select')) {this.populateEncounters(event.target.value)}});
    document.getElementById('encounter-list').addEventListener('change', event => this.previewEncounter(event.target.value));
    document.getElementById('mc-reassign').addEventListener('click', () => {
        if (window.confirm('Replace the current turn order and restart at round 1?')) this.openInitiative();
    });
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
        const picker = document.getElementById('encounter-list');
        const feedback = document.getElementById('mc-launch-roster');
        this.dataStore.set(SELECTED_SESSION_ID_KEY, sessionId);
        picker.options.length = 1;
        this.previewEncounter('0');
        feedback.textContent = 'Loading encounters…';
        const encounters = await this.runClient.getEncounterList(sessionId);
        if (this.dataStore.get(SELECTED_SESSION_ID_KEY) !== sessionId) return;
        for (const encounter of encounters || []) picker.add(new Option(encounter.objectName, encounter.objectId));
        feedback.textContent = encounters?.length ? 'Select an encounter to see its roster.'
            : encounters ? 'No encounters in this session.' : 'Could not load encounters.';
    }

    hasSavedOrder(encounter) {
        const roster = Object.keys(encounter.creatureMap || {});
        const order = encounter.turnQueue;
        return Boolean(roster.length && order?.length === roster.length &&
            new Set(order).size === roster.length && order.every(id => encounter.creatureMap[id]));
    }

    async previewEncounter(id) {
        const version = this.previewVersion = (this.previewVersion || 0) + 1;
        const button = document.getElementById('launch-session-btn');
        const preview = document.getElementById('mc-launch-roster');
        button.disabled = true;
        button.textContent = 'Set initiative';
        preview.textContent = '';
        if (id === '0') return;
        preview.textContent = 'Loading roster…';
        const encounter = await this.encounterClient.getSingleEncounter(id);
        if (version !== this.previewVersion) return;
        const names = Object.values(encounter?.creatureMap || {}).map(creature => creature.encounterCreatureName);
        preview.textContent = !encounter ? 'Could not load this encounter.'
            : names.length ? names.join(' · ') : 'Add creatures in the Encounter Library before launching.';
        if (!names.length) return;
        button.textContent = this.hasSavedOrder(encounter) ? 'Resume combat' : 'Set initiative';
        button.disabled = false;
    }

    async launchEncounter(encounterId = document.getElementById('encounter-list').value) {
        if (!encounterId || encounterId === '0' || this.loadingEncounter) return;
        this.loadingEncounter = true;
        this.hideElements();
        const feedback = document.getElementById('mc-combat-feedback');
        feedback.textContent = '';
        try {
            const encounter = await this.encounterClient.getSingleEncounter(encounterId);
            if (!encounter) {
                feedback.textContent = 'Could not load this encounter. Choose another encounter or try again.';
                return;
            }
            const roster = Object.entries(encounter.creatureMap || {});
            if (!roster.length) {
                feedback.textContent = 'Add creatures in the Encounter Library before launching.';
                return;
            }
            this.dataStore.set([ENCOUNTER_KEY], encounter);
            this.inspectId = null;
            document.getElementById('encounterNameBig').textContent = encounter.objectName;
            if (this.hasSavedOrder(encounter)) {
                this.rememberEncounter(encounter);
                this.populateAccordions();
            } else {
                this.openInitiative();
            }
        } finally {
            this.loadingEncounter = false;
            this.showElements();
            if (this.dataStore.get(ENCOUNTER_KEY)?.turnQueue?.length &&
                !document.getElementById('mc-turn-list').closest('[hidden]')) this.hideElementsPlay();
        }
    }

    rememberEncounter(encounter) {
        const url = new URL(location.href);
        url.searchParams.set('encounter', encounter.objectId);
        history.replaceState(null, '', url);
        try { localStorage.setItem('beholder:lastEncounter', encounter.objectId); } catch {}
    }

    openInitiative() {
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        const roster = Object.entries(encounter.creatureMap || {});
        this.initiative = roster.map(([id, creature]) => ({
            id,
            name: creature.encounterCreatureName,
            modifier: this.modCalc(creature.statMap?.dexterity),
            total: NaN,
        }));
        const body = document.getElementById('offcanvas-init-body');
        body.replaceChildren();
        for (const entry of this.initiative) {
            const row = document.createElement('div');
            row.className = 'mc-initiative-row';
            const name = document.createElement('strong');
            name.textContent = entry.name;
            const label = document.createElement('label');
            label.textContent = 'Total';
            const input = document.createElement('input');
            input.type = 'number';
            input.step = '1';
            input.className = 'form-control';
            input.setAttribute('aria-label', entry.name + ' initiative total');
            input.addEventListener('input', () => {
                entry.total = input.valueAsNumber;
                this.previewInitiative();
            });
            entry.input = input;
            label.append(input);
            const roll = document.createElement('button');
            roll.type = 'button';
            roll.className = 'btn btn-outline-dark';
            roll.textContent = 'Roll';
            roll.setAttribute('aria-label', 'Roll initiative for ' + entry.name);
            roll.addEventListener('click', () => {
                this.rollInitiative(entry);
                this.previewInitiative();
            });
            row.append(name, label, roll);
            body.append(row);
        }
        this.previewInitiative();
        bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('offcanvasInitiative')).show();
    }

    rollInitiative(entry) {
        entry.total = 1 + Math.floor(Math.random() * 20) + entry.modifier;
        entry.input.value = entry.total;
    }

    assignInitiative() {
        this.initiative.forEach(entry => this.rollInitiative(entry));
        this.previewInitiative();
    }

    previewInitiative(focusEntry) {
        this.initiative.sort((a, b) => (b.total || 0) - (a.total || 0));
        const ordered = this.initiative.filter(entry => Number.isSafeInteger(entry.total));
        const list = document.getElementById('mc-initiative-preview');
        list.replaceChildren();
        for (const [index, entry] of ordered.entries()) {
            const item = document.createElement('li');
            item.className = 'my-2';
            item.textContent = entry.name + ' · ' + entry.total + ' ';
            const previous = ordered[index - 1];
            if (previous?.total === entry.total) {
                const up = document.createElement('button');
                up.type = 'button';
                up.className = 'btn btn-sm btn-outline-dark';
                up.textContent = 'Move up in tie';
                up.setAttribute('aria-label', 'Move ' + entry.name + ' ahead of ' + previous.name);
                up.addEventListener('click', () => {
                    const a = this.initiative.indexOf(previous);
                    const b = this.initiative.indexOf(entry);
                    [this.initiative[a], this.initiative[b]] = [this.initiative[b], this.initiative[a]];
                    this.previewInitiative(entry);
                });
                item.append(up);
                if (focusEntry === entry) queueMicrotask(() => up.focus());
            } else if (focusEntry === entry) {
                queueMicrotask(() => entry.input.focus());
            }
            list.append(item);
        }
        this.initiativeOrder = ordered.map(entry => entry.id);
        const complete = ordered.length > 0 && ordered.length === this.initiative.length;
        document.getElementById('go-btn').disabled = !complete || this.combatPending;
        document.getElementById('mc-initiative-feedback').textContent = complete ? '' : 'Enter a whole number for each combatant.';
    }

    async goEncounter() {
        const button = document.getElementById('go-btn');
        if (button.disabled || this.combatPending) return;
        const encounterId = this.dataStore.get(ENCOUNTER_KEY).objectId;
        const saved = await this.saveCombat(
            () => this.runClient.setInitiative(encounterId, this.initiativeOrder),
            {followTurn:true, feedbackId:'mc-initiative-feedback'}
        );
        if (saved) {
            bootstrap.Offcanvas.getInstance(document.getElementById('offcanvasInitiative')).hide();
            this.rememberEncounter(this.dataStore.get(ENCOUNTER_KEY));
        } else {
            button.disabled = this.combatUncertain;
        }
    }

    async saveCombat(request, {followTurn = false, feedbackId = 'mc-combat-feedback'} = {}) {
        if (this.combatPending || this.combatUncertain) return false;
        const feedback = document.getElementById(feedbackId);
        const previous = this.dataStore.get(ENCOUNTER_KEY);
        this.combatPending = true;
        document.querySelectorAll('#next-turn-btn, #go-btn, #apply-damage-btn, #apply-heal-btn, #apply-effect-btn, #remove-effect-btn, .ko-btn, .kill-btn')
            .forEach(button => {button.disabled = true;});
        feedback.textContent = 'Saving…';
        try {
            const updated = await request();
            if (!updated) {
                const refreshed = await this.encounterClient.getSingleEncounter(previous.objectId);
                if (!refreshed) this.combatUncertain = true;
                if (refreshed?.turnQueue?.length && refreshed.turnQueue.every(id => refreshed.creatureMap?.[id])) {
                    this.dataStore.set([ENCOUNTER_KEY], refreshed);
                    this.populateAccordions();
                }
                feedback.textContent = refreshed
                    ? 'Save was not confirmed. The saved encounter was reloaded. Check it before retrying.'
                    : 'Could not confirm the save. Reload this page before trying again.';
                return false;
            }
            if (followTurn) {
                this.inspectId = null;
                this.dataStore.set([SELECTED_CREATURE_ID_KEY], '');
            }
            this.dataStore.set([ENCOUNTER_KEY], updated);
            this.populateAccordions();
            feedback.textContent = 'Saved';
            return true;
        } finally {
            this.combatPending = false;
            document.querySelectorAll('#next-turn-btn, #apply-damage-btn, #apply-heal-btn, #apply-effect-btn, #remove-effect-btn, .ko-btn, .kill-btn')
                .forEach(button => {button.disabled = Boolean(this.combatUncertain);});
        }
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
               <div class="accordion-item" data-combatant-panel="${encounterCreatureId}">
                   <h2 class="accordion-header" id="heading_${encounterCreatureId}">
                       <button id="acc_button_${encounterCreatureId}" class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapse_${encounterCreatureId}" aria-expanded="false" aria-controls="collapse_${encounterCreatureId}">
                           <span></span>
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
                            <div class = "row">
                                <div class="mb-3 col">
                                    <div class="table-responsive unlimited accordion-table"">
                                        <table id = "spell_table_${encounterCreatureId}"  class="table table-striped accordion-table">
                                            <thead>
                                            <tr>
                                                <th>Spell</th>
                                                <th>Level</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="mb-3 col">
                                    <div class="table-responsive unlimited accordion-table">
                                        <table id = "action_table_${encounterCreatureId}"  class="table table-striped accordion-table">
                                            <thead>
                                            <tr>
                                                <th>Action</th>
                                                <th>Type</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class = "row">
                                <div class="mb-3 col">
                                    <div class="table-responsive unlimited accordion-table">
                                        <table id = "effect_table_${encounterCreatureId}"  class="table table-striped accordion-table">
                                            <thead>
                                            <tr>
                                                <th>Effect</th>
                                                <th>From</th>
                                                <th>Duration</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="mb-3 col">
                                </div>
                            </div>
                       </div>
                   </div>
               </div>`
               accordion.insertAdjacentHTML('afterbegin', accordionItem);
               document.querySelector('#acc_button_' + CSS.escape(encounterCreatureId) + ' span').textContent = creatureName;
               const body = document.getElementById('collapse_' + encounterCreatureId).firstElementChild;
               const reminders = (encounter.messageQueue || []).filter(message => message.startsWith('[' + creature.encounterCreatureName + ']') || message.endsWith('for [' + creature.encounterCreatureName + '].'));
               for (const message of reminders) {
                   const notice = document.createElement('p');
                   notice.className = 'mc-feedback';
                   notice.textContent = message;
                   body.prepend(notice);
               }
               this.populateSpells(encounterCreatureId);
               this.populateActions(encounterCreatureId);
               this.populateEffects(encounterCreatureId);
        }
        const notifications = document.getElementById('notification-list');
        const messages = encounter.messageQueue || [];
        notifications.replaceChildren(...messages.map(message => {
            const item = document.createElement('li');
            item.className = 'list-group-item';
            item.textContent = message;
            return item;
        }));
        document.getElementById('notifications-btn').textContent = messages.length
            ? 'Notifications (' + messages.length + ')' : 'Notifications';
        document.getElementById('roundNumber').textContent = 'Round ' + encounter.encounterRound + ' · ';
        document.getElementById('mc-active').textContent = creatureMap.get(turnQueue[0]).encounterCreatureName;
        document.getElementById('mc-next').textContent = 'Up next · ' + creatureMap.get(turnQueue[1] || turnQueue[0]).encounterCreatureName;
        const list = document.getElementById('mc-turn-list');
        list.replaceChildren(...turnQueue.map((id, index) => {
            const creature = creatureMap.get(id);
            const button = document.createElement('button');
            button.type = 'button';
            button.className = 'btn btn-outline-dark';
            button.dataset.combatant = id;
            button.textContent = creature.encounterCreatureName + (index === 0 ? ' · Active' : index === 1 ? ' · Next' : '');
            if (index === 0) button.setAttribute('aria-current', 'step');
            button.addEventListener('click', () => this.inspectCombatant(id));
            return button;
        }));
        accordion.hidden = false;
        this.inspectCombatant(turnQueue.includes(this.inspectId) ? this.inspectId : turnQueue[0]);
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
        for(var btn of document.getElementsByClassName('ko-btn')) {
                    btn.addEventListener('click', (event) => {
                                            if (event.target.closest('button')) {this.knockOut(event.target.dataset.id)}});
                }
        for(var btn of document.getElementsByClassName('kill-btn')) {
                    btn.addEventListener('click', (event) => {
                                            if (event.target.closest('button')) {this.kill(event.target.dataset.id)}});
                }

        for(var btn of document.getElementsByClassName('effect-btn')) {
                            btn.addEventListener('click', (event) => {
                                                    if (event.target.closest('button')) {this.addEffect(event.target.dataset.id)}});
                        }
        this.hideElementsPlay();
    }

    populateSpells(encounterCreatureId) {
        var table = document.getElementById('spell_table_' + encounterCreatureId);
        var body = table.getElementsByTagName('tbody')[0];
        body.innerHTML = "";
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId);
        if (creature.spellMap) {
        var spellMap = new Map(Object.entries(creature.spellMap))
            for(var [key, value] of spellMap) {
                var row = body.insertRow(-1);
                row.setAttribute('id', value.objectId);
                row.setAttribute('data-id', value.objectId);
                row.setAttribute('data-crid', creature.encounterCreatureId);
                var cell0 = row.insertCell(0);
                var cell1 = row.insertCell(1);
                cell0.innerHTML = value.objectName;
                cell1.innerHTML = value.spellLevel;
            }
            this.sortSpellTable(table);
            document.getElementById('spell_table_' + encounterCreatureId).addEventListener('click', (event) => {
                                                                if (event.target.closest('tbody')) {this.viewSpell(event.target.parentNode.dataset.id, event.target.parentNode.dataset.crid)}});
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

    populateActions(encounterCreatureId) {
        var table = document.getElementById('action_table_' + encounterCreatureId);
        var body = table.getElementsByTagName('tbody')[0];
        body.innerHTML = "";
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId);
        if (creature.actionMap) {
            var actionMap = new Map(Object.entries(creature.actionMap))
            for(var [key, value] of actionMap) {
                var row = body.insertRow(-1);
                row.setAttribute('id', value.objectId);
                row.setAttribute('data-id', value.objectId);
                row.setAttribute('data-crid', creature.encounterCreatureId);
                var cell0 = row.insertCell(0);
                var cell1 = row.insertCell(1);
                cell0.innerHTML = value.objectName;
                cell1.innerHTML = value.actionType;
            }
            this.sortActionTable(table);
            document.getElementById('action_table_' + encounterCreatureId).addEventListener('click', (event) => {
                                                                if (event.target.closest('tbody')) {this.viewAction(event.target.parentNode.dataset.id, event.target.parentNode.dataset.crid)}});
        }
    }

    sortActionTable(table) {
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

    populateEffects(encounterCreatureId) {
        var table = document.getElementById('effect_table_' + encounterCreatureId);
        var body = table.getElementsByTagName('tbody')[0];
        body.innerHTML = "";
        var creature = this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId);
        if (creature.activeEffects) {
            var effectMap = new Map(Object.entries(creature.activeEffects))
            for(var [key, value] of effectMap) {
                var row = body.insertRow(-1);
                row.setAttribute('id', value.objectId);
                row.setAttribute('data-id', value.objectId);
                row.setAttribute('data-crid', creature.encounterCreatureId)
                var cell0 = row.insertCell(0);
                var cell1 = row.insertCell(1);
                var cell2 = row.insertCell(2);
                cell0.innerHTML = value.effectName;
                console.log(value)
                if (value.blameCreatureId == '0') {
                    cell1.innerHTML = '';
                } else {
                    cell1.innerHTML = this.dataStore.get(CREATURE_MAP_KEY).get(value.blameCreatureId).encounterCreatureName;
                }
                cell2.innerHTML = value.turnDuration;
            }
            this.sortEffectTable(table);
            document.getElementById('effect_table_' + encounterCreatureId).addEventListener('click', (event) => {
                                                                if (event.target.closest('tbody')) {this.viewEffect(event.target.parentNode.dataset.id, event.target.parentNode.dataset.crid)}});
        }
    }

    sortEffectTable(table) {
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

    inspectCombatant(id) {
        this.inspectId = id;
        document.querySelectorAll('[data-combatant-panel]').forEach(panel => {
            const selected = panel.dataset.combatantPanel === id;
            panel.hidden = !selected;
            panel.querySelector('.accordion-collapse').classList.toggle('show', selected);
            const heading = panel.querySelector('.accordion-button');
            heading.classList.remove('collapsed');
            heading.setAttribute('aria-expanded', 'true');
            heading.removeAttribute('data-bs-toggle');
            heading.disabled = true;
        });
        document.querySelectorAll('[data-combatant]').forEach(button => {
            button.setAttribute('aria-pressed', button.dataset.combatant === id);
        });
    }

    async nextTurn() {
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        await this.saveCombat(() => this.runClient.nextTurn(encounter.objectId), {followTurn:true});
    }

    async damageValue(targetID) {
        this.dataStore.set([SELECTED_CREATURE_ID_KEY], targetID);
        this.dataStore.set([TARGET_CREATURE_ID_KEY], targetID);
        var myOffcanvas = document.getElementById('offcanvasDamage');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async applyDamage() {
        const field = document.getElementById('damage-value');
        const amount = Number(field.value);
        if (!field.value || !Number.isSafeInteger(amount) || amount < 1 || amount > 2147483647) {
            field.setCustomValidity('Enter a positive whole number.');
            field.reportValidity();
            field.setCustomValidity('');
            return;
        }
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        const target = this.dataStore.get(TARGET_CREATURE_ID_KEY);
        const saved = await this.saveCombat(() => this.runClient.applyDamage(encounter.objectId, target, amount),
            {feedbackId:'mc-damage-feedback'});
        if (saved) {
            field.value = '';
            document.getElementById('offcanvas-damage-close').click();
        }
    }

    async healValue(targetID) {
        this.dataStore.set([SELECTED_CREATURE_ID_KEY], targetID);
        this.dataStore.set([TARGET_CREATURE_ID_KEY], targetID);
        var myOffcanvas = document.getElementById('offcanvasHeal');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async applyHeal() {
        const field = document.getElementById('heal-value');
        const amount = Number(field.value);
        if (!field.value || !Number.isSafeInteger(amount) || amount < 1 || amount > 2147483647) {
            field.setCustomValidity('Enter a positive whole number.');
            field.reportValidity();
            field.setCustomValidity('');
            return;
        }
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        const target = this.dataStore.get(TARGET_CREATURE_ID_KEY);
        const saved = await this.saveCombat(() => this.runClient.heal(encounter.objectId, target, amount),
            {feedbackId:'mc-heal-feedback'});
        if (saved) {
            field.value = '';
            document.getElementById('offcanvas-heal-close').click();
        }
    }

    async knockOut(targetID) {
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        await this.saveCombat(() => this.runClient.knockOut(encounter.objectId, targetID));
    }

    async kill(targetID) {
        const encounter = this.dataStore.get(ENCOUNTER_KEY);
        await this.saveCombat(() => this.runClient.kill(encounter.objectId, targetID));
    }

    viewSpell(spellId, encounterCreatureId) {
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = creatureMap.get(encounterCreatureId);
        var spell = new Map(Object.entries(creature.spellMap)).get(spellId);
        document.getElementById('spell-name').innerHTML = spell.objectName;
        document.getElementById('spellDescription').value = spell.spellDescription;
        document.getElementById('spellHigherLevel').value = spell.spellHigherLevel;
        document.getElementById('spellRange').value = spell.spellRange;
        document.getElementById('spellComponents').value = spell.spellComponents;
        document.getElementById('spellMaterial').value = spell.spellMaterial;
        document.getElementById('reaction').value = spell.reaction;
        document.getElementById('ritualCast').value = spell.ritualCast
                                                      ? "Yes"
                                                      : "No";
        switch(spell.castingTime) {
            case "0":
                document.getElementById('spellLevel').value = "Cantrip";
                break;
            case "1":
                document.getElementById('spellLevel').value = "1st";
                break;
            case "2":
                document.getElementById('spellLevel').value = "2nd";
                break;
            case "3":
                document.getElementById('spellLevel').value = "3rd";
                break;
            case "4":
                document.getElementById('spellLevel').value = "4th";
                break;
            case "5":
                document.getElementById('spellLevel').value = "5th";
                break;
            case "6":
                document.getElementById('spellLevel').value = "6th";
                break;
            case "7":
                document.getElementById('spellLevel').value = "7th";
                break;
            case "8":
                document.getElementById('spellLevel').value = "8th";
                break;
            case "9":
                document.getElementById('spellLevel').value = "9th";
                break;
            default:
                document.getElementById('spellLevel').value = ""
        }
        document.getElementById('castingTime').value = spell.castingTime;
        document.getElementById('spellSchool').value = spell.spellSchool;
        document.getElementById('innateCasts').value = spell.innateCasts;
        var myOffcanvas = document.getElementById('offcanvasSpell');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    viewEffect(effectId, encounterCreatureId) {
        this.dataStore.set(SELECTED_EFFECT_ID_KEY, effectId)
        this.dataStore.set(SELECTED_CREATURE_ID_KEY, encounterCreatureId);
        var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
        var creature = creatureMap.get(encounterCreatureId);
        var effect = new Map(Object.entries(creature.activeEffects)).get(effectId);
        document.getElementById('viewEffectName').value = effect.effectName;
        if(effect.blameCreatureId == '0') {
            document.getElementById('viewEffectBlameCreature').value = '';
         } else {
            document.getElementById('viewEffectBlameCreature').value = creatureMap.get(effect.blameCreatureId).encounterCreatureName;
        }
        document.getElementById('viewEffectDuration').value = effect.turnDuration;
        document.getElementById('viewEffectSaveType').value = effect.saveType;
        document.getElementById('viewEffectSaveDC').value = effect.saveDC;
        document.getElementById('viewEffectSaveOn').value = effect.saveOn;
        var myOffcanvas = document.getElementById('offcanvasViewEffect');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    viewAction(actionId, encounterCreatureId) {
            var creatureMap = this.dataStore.get(CREATURE_MAP_KEY);
            var creature = creatureMap.get(encounterCreatureId);
            var action = new Map(Object.entries(creature.actionMap)).get(actionId);
            document.getElementById('action-name').innerHTML = action.objectName;
            document.getElementById('actionDescription').value = action.actionDescription;
            document.getElementById('actionType').value = action.actionType;
            document.getElementById('actionUses').value = action.uses;
            document.getElementById('actionRecharge').value = action.rechargeOn;
            var myOffcanvas = document.getElementById('offcanvasAction');
            var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
            bsOffcanvas.show();
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

    addEffect(encounterCreatureId) {
        this.dataStore.set(SELECTED_CREATURE_ID_KEY, encounterCreatureId);
        var dropDown = document.getElementById('effectBlameCreatureId');
                for (var i = dropDown.options.length; i > 0; i--) {
                    dropDown.options.remove(i);
                }
                for (var [key, value] of this.dataStore.get(CREATURE_MAP_KEY)) {
                    var option = document.createElement('option');
                        option.value = key;
                        option.innerHTML = value.encounterCreatureName;
                        option.setAttribute('dataset-id', key);
                        dropDown.appendChild(option);
        };
        document.getElementById('effect-target-name').innerHTML = "Apply Effect to " +
            this.dataStore.get(CREATURE_MAP_KEY).get(encounterCreatureId).encounterCreatureName;
        this.dataStore.set(SELECTED_CREATURE_ID_KEY, encounterCreatureId);
        var myOffcanvas = document.getElementById('offcanvasEffect');
        var bsOffcanvas = new bootstrap.Offcanvas(myOffcanvas);
        bsOffcanvas.show();
    }

    async addEffectFinish() {
        var encounter = this.dataStore.get(ENCOUNTER_KEY);
        var encounterCreatureId = this.dataStore.get(SELECTED_CREATURE_ID_KEY);
        var effect = {};
        effect.objectId = uuidv4();
        effect.effectName = document.getElementById('effectName').value;
        effect.turnDuration = document.getElementById('effectTurnDuration').value || "1";
        effect.blameCreatureId = document.getElementById('effectBlameCreatureId').value;
        effect.saveType = document.getElementById('effectSaveType').value;
        effect.saveDC = document.getElementById('effectSaveDC').value || "0";
        effect.saveOn = [];
        for (var option of document.getElementById('effectSaveOn').options) {
            if(option.selected) {
                effect.saveOn.push(option.value);
            }
        }
        const saved = await this.saveCombat(() => this.runClient.addEffect(encounter.objectId, encounterCreatureId, effect),
            {feedbackId:'mc-effect-feedback'});
        if (saved) document.getElementById('offcanvas-effect-close').click();
    }
    
    async removeEffect() {
        var encounterCreatureId = this.dataStore.get(SELECTED_CREATURE_ID_KEY);
        var effectId = this.dataStore.get(SELECTED_EFFECT_ID_KEY);
        var encounter = this.dataStore.get(ENCOUNTER_KEY);
        const saved = await this.saveCombat(() => this.runClient.removeEffect(encounter.objectId, encounterCreatureId, effectId),
            {feedbackId:'mc-remove-effect-feedback'});
        if (saved) document.getElementById('offcanvas-view-effect-close').click();
    }

    showNotifications() {
        var myOffcanvas = document.getElementById('offcanvasNotification');
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
