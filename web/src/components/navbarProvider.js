import AuthClient from '../api/authClient';
import BindingClass from "../util/bindingClass";

/**
 * Builds a standard navbar everywhere on the site.
 */

 export default class Header extends BindingClass {
    constructor() {
        super();
        const methodsToBind = ['provideBars'];
        this.bindClassMethods(methodsToBind, this);
        this.client = new AuthClient();
    };

    async provideBars() {
        const currentUser = await this.client.getIdentity();
        const header = document.createElement('header');
        header.id = 'header-buttons';
        header.className = 'd-flex flex-wrap align-items-center py-2 px-2';
        const nav = document.createElement('nav');
        nav.className = 'd-flex flex-wrap gap-1';
        nav.setAttribute('aria-label', 'Main navigation');
        const current = location.pathname.split('/').pop() || 'index.html';
        const destinations = [
            ['Beholder', 'index.html'],
            ['Encounters', 'encounterLibrary.html'],
            ['Creatures', 'creatureLibrary.html'],
            ['Spells', 'spellLibrary.html'],
            ['Run Encounter', 'runEncounter.html'],
        ];
        for (const [label, href] of destinations) {
            if (!currentUser && href !== 'index.html') continue;
            const link = document.createElement('a');
            link.href = href;
            link.className = 'btn btn-flat';
            if (href === 'index.html') link.classList.add('mc-brand');
            if (href === current) link.setAttribute('aria-current', 'page');
            link.textContent = label;
            nav.append(link);
        }
        const account = currentUser ? this.createLogoutButton(currentUser) : this.createLoginButton();
        account.classList.add('mc-account');
        header.append(nav, account);
        document.getElementById('header-bar').replaceChildren(header);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('button');
        button.classList.add('btn');
        button.classList.add('btn-flat');
        button.classList.add('align-items-center');
        button.classList.add('rounded');
        button.type = 'button';
        button.innerText = text;
        button.addEventListener('click', async () => {
                    await clickHandler();
        });
        return button;
    };

    createLoginButton() {
        return this.createButton('Sign in', this.client.login);
    };

    createLogoutButton(currentUser) {
        return this.createButton(`Sign out · ${currentUser.name}`, this.client.logout);
    };

}
