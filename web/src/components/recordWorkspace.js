export function recordWorkspace(render, selected, reload) {
    const editor = document.getElementById('mc-record-editor');
    const summary = document.getElementById('mc-record-summary');
    const edit = document.getElementById('mc-edit-record');
    const save = document.getElementById('update-btn');
    const cancel = document.getElementById('mc-cancel-edit');
    const status = document.getElementById('mc-draft-status');
    let baseline;
    let saving = false;

    function snapshot() {
        return JSON.stringify([selected(), [...editor.querySelectorAll('input, select, textarea')]
            .map(field => [field.value, field.checked])]);
    }

    function finishEdit() {
        editor.hidden = true;
        summary.hidden = false;
        edit.hidden = !selected();
        baseline = undefined;
    }

    function canSwitch() {
        return !saving && (!baseline || baseline === snapshot() || window.confirm('Discard unsaved changes?'));
    }

    edit.addEventListener('click', () => {
        summary.hidden = true;
        edit.hidden = true;
        editor.hidden = false;
        baseline = snapshot();
        status.textContent = 'Editing';
        (editor.querySelector('.tab-pane.active input') || editor.querySelector('input'))?.focus();
    });
    editor.addEventListener('submit', event => event.preventDefault());
    editor.addEventListener('input', () => {status.textContent = 'Unsaved changes';});
    cancel.addEventListener('click', async () => {
        if (!canSwitch()) return;
        finishEdit();
        await reload(selected().objectId);
        edit.focus();
    });
    window.addEventListener('beforeunload', event => {
        if (baseline && baseline !== snapshot()) {
            event.preventDefault();
            event.returnValue = '';
        }
    });
    return {
        canSwitch,
        finishEdit,
        show(record) {
            finishEdit();
            render(record);
            document.getElementById('mc-record-feedback').textContent = '';
        },
        beginSave() {
            if (saving || editor.hidden || !selected()) return false;
            const name = editor.querySelector('#objectName');
            if (!name.value.trim()) {
                const pane = name.closest('.tab-pane');
                if (pane) {
                    const tab = editor.querySelector('[data-bs-target="#' + pane.id + '"]');
                    if (tab) bootstrap.Tab.getOrCreateInstance(tab).show();
                }
                name.setCustomValidity('Enter a name.');
                name.reportValidity();
                name.setCustomValidity('');
                status.textContent = 'Enter a name before saving.';
                return false;
            }
            saving = true;
            save.disabled = cancel.disabled = true;
            status.textContent = 'Saving…';
            return true;
        },
        endSave() {
            saving = false;
            save.disabled = cancel.disabled = false;
        },
        saveFailed() {
            status.textContent = 'Save failed. Your edits are still here.';
        },
    };
}

function paragraph(text, className = '') {
    const element = document.createElement('p');
    element.className = className;
    element.textContent = text;
    return element;
}

function metrics(entries) {
    const grid = document.createElement('div');
    grid.className = 'mc-stat-grid';
    for (const [label, value] of entries) {
        const cell = document.createElement('div');
        const number = document.createElement('strong');
        number.textContent = value ?? '—';
        cell.append(paragraph(label, 'mc-label'), number);
        grid.append(cell);
    }
    return grid;
}

function block(title, value) {
    const section = document.createElement('section');
    section.className = 'mc-reading mt-3';
    const heading = document.createElement('h3');
    heading.className = 'mc-label';
    heading.textContent = title;
    section.append(heading, paragraph(value || '—', 'mc-prose'));
    return section;
}

export function showRecord(kind, record) {
    const panel = document.getElementById('mc-record-summary');
    if (kind === 'creature') {
        const actions = Object.values(record.actionMap || {}).map(action =>
            [action.actionName || action.objectName || action.name, action.actionDescription || action.description].filter(Boolean).join(' · '));
        const defenses = [
            ['Senses', record.senses], ['Resistances', record.resistances], ['Immunities', record.immunities],
            ['Languages', record.languages], ['Notes', record.creatureDescription],
        ].filter(([, value]) => value).map(([label, value]) => label + ' · ' + value);
        panel.replaceChildren(paragraph(record.isPC ? 'Player character · Level ' + (record.pcLevel ?? '—')
            : [record.size, record.type, record.challengeRating != null ? 'CR ' + record.challengeRating : null, record.sourceBook].filter(Boolean).join(' · '), 'mc-muted'),
            metrics([['Hit points', record.hitPoints], ['Armor class', record.armorClass], ['Walk', record.speedMap?.walk == null ? null : record.speedMap.walk + ' ft.']]),
            metrics(['strength', 'dexterity', 'constitution', 'intelligence', 'wisdom', 'charisma']
                .map(key => [key.slice(0, 3), record.statMap?.[key]])),
            block('Actions', actions.join('\n') || 'No actions assigned.'));
        if (defenses.length) {
            const details = document.createElement('details');
            details.className = 'mc-reading mt-3';
            const title = document.createElement('summary');
            title.textContent = 'Senses, defenses, and notes';
            details.append(title, paragraph(defenses.join('\n'), 'mc-prose'));
            panel.append(details);
        }
    } else {
        const level = Number(record.spellLevel) === 0 ? 'Cantrip' : 'Level ' + record.spellLevel;
        panel.replaceChildren(paragraph([level, record.spellSchool].filter(Boolean).join(' · '), 'mc-muted'),
            metrics([['Casting time', record.castingTime], ['Range', record.spellRange], ['Components', record.spellComponents], ['Duration', record.spellDuration ?? record.duration]]),
            block('Description', record.spellDescription));
        const labels = [record.ritualCast && 'Ritual', record.requiresConcentration === true && 'Concentration'].filter(Boolean);
        if (labels.length) {
            const tags = paragraph('', 'd-flex flex-wrap gap-2 mt-2');
            for (const label of labels) {
                const tag = document.createElement('span');
                tag.className = 'mc-status-tag';
                tag.textContent = label;
                tags.append(tag);
            }
            panel.children[0].after(tags);
        }
        if (record.spellHigherLevel) {
            const details = document.createElement('details');
            details.className = 'mc-reading mt-3';
            const title = document.createElement('summary');
            title.textContent = Number(record.spellLevel) === 0 ? 'Cantrip scaling' : 'At higher levels';
            details.append(title, paragraph(record.spellHigherLevel, 'mc-prose'));
            panel.append(details);
        }
    }
}

let previewVersion = 0;
export async function previewTemplate(slug, fetchTemplate, kind, getRecords) {
    const version = ++previewVersion;
    const preview = document.getElementById('mc-import-preview');
    const submit = document.getElementById('import-finish-btn');
    submit.disabled = true;
    preview.textContent = slug ? 'Loading preview…' : 'Select a result to preview it.';
    if (!slug) return;
    const template = await fetchTemplate(slug);
    if (version !== previewVersion) return;
    if (!template) {
        preview.textContent = 'Preview unavailable. Select this result to retry.';
        return;
    }
    const duplicate = getRecords().find(record => record.objectName?.trim().toLowerCase() === template.name?.trim().toLowerCase());
    preview.replaceChildren(block(template.name, template.document__title || 'Open5e'));
    if (kind === 'creature') {
        preview.append(paragraph([template.size, template.type, template.hit_points != null ? template.hit_points + ' HP' : null,
            template.armor_class != null ? template.armor_class + ' AC' : null].filter(Boolean).join(' · ')));
        if (Array.isArray(template.actions)) preview.append(block('Actions', template.actions.map(action => action.name).filter(Boolean).join(', ')));
        if (template.spell_list?.length) preview.append(block('Spells to review', [].concat(template.spell_list).join(', ')));
    } else {
        preview.append(paragraph([template.level, template.school, template.casting_time, template.range, template.duration].filter(Boolean).join(' · ')),
            paragraph(template.desc || '', 'mc-prose'));
    }
    if (template.resourceExists) {
        preview.append(paragraph('This source entry is already in your library.', 'mc-feedback'));
    } else if (duplicate) {
        preview.append(paragraph('Possible duplicate in your library · ' + duplicate.objectName, 'mc-feedback'));
    }
    submit.disabled = Boolean(template.resourceExists);
}
