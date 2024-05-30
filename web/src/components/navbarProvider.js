import AuthClient from '../api/authClient';
import BindingClass from "../util/bindingClass";

/**
 * Builds a standard navbar everywhere on the site.
 */

 export default class Header extends BindingClass {
    constructor() {
        super();
        const methodsToBind = ['addNavbarToPage', 'createLoginButton', 'createLogoutButton', 'createButton'];
        this.bindClassMethods(methodsToBind, this);
        this.client = new AuthClient();
    };

    async addNavbarToPage() {
        const currentUser = await this.client.getIdentity();
        const navbar = `<div class="flex-shrink-0 p-3 bg-white" style="width: 280px;">
                                    <a href="/" class="d-flex align-items-center pb-3 mb-3 link-dark text-decoration-none border-bottom">
                                        <svg class="bi me-2" width="30" height="24"><use xlink:href="#bootstrap"/></svg>
                                        <span class="fs-5 fw-semibold px-2">Beholder</span>
                                        <img src="images/d20a.png" width="30" height="30">
                                    </a>
                                    <ul class="list-unstyled ps-0" id = "list">
                                        <li class="mb-1">
                                            <button class="btn btn-toggle align-items-center rounded collapsed preload" data-bs-toggle="collapse" data-bs-target="#session-collapse" aria-expanded="false" disabled>
                                                Sessions
                                            </button>
                                            <div class="collapse" id="session-collapse">
                                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                                                    <li><a href="#" class="link-dark rounded">Session Library</a></li>
                                                    <li><a href="#" class="link-dark rounded">New Session</a></li>
                                                </ul>
                                            </div>
                                        </li>
                                        <li class="mb-1">
                                            <button class="btn btn-toggle align-items-center rounded collapsed preload" data-bs-toggle="collapse" data-bs-target="#encounter-collapse" aria-expanded="false" disabled>
                                                Encounters
                                            </button>
                                            <div class="collapse" id="encounter-collapse">
                                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                                                    <li><a href="#" class="link-dark rounded">Encounter Library</a></li>
                                                    <li><a href="#" class="link-dark rounded">New Encounter</a></li>
                                                </ul>
                                            </div>
                                        </li>
                                        <li class="mb-1">
                                            <button class="btn btn-toggle align-items-center rounded collapsed preload" data-bs-toggle="collapse" data-bs-target="#creature-collapse" aria-expanded="false" disabled>
                                                Creatures
                                            </button>
                                            <div class="collapse" id="creature-collapse">
                                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                                                    <li><a href="#" class="link-dark rounded">Creature Library</a></li>
                                                    <li><a href="#" class="link-dark rounded">New Creature</a></li>
                                                </ul>
                                            </div>
                                        </li>
                                        <li class="mb-1">
                                            <button class="btn btn-toggle align-items-center rounded collapsed preload" data-bs-toggle="collapse" data-bs-target="#spell-collapse" aria-expanded="false" disabled>
                                                Spells
                                            </button>
                                            <div class="collapse" id="spell-collapse">
                                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                                                    <li><a href="spellLibrary.html" class="link-dark rounded">Spell Library</a></li>
                                                    <li><a href="#" class="link-dark rounded">New Spell</a></li>
                                                </ul>
                                            </div>
                                        </li>

                                        <li class="border-top my-3"></li>
                                        <li class="mb-1">
                                            <button class="btn btn-flat align-items-center rounded preload" disabled>
                                                Run Encounter
                                            </button>
                                        <li class="border-top my-3"></li>
                                    </ul>
                                </div>`;
                                document.getElementById('navbar').insertAdjacentHTML('afterbegin', navbar);
                                const button = currentUser
                                   ? this.createLogoutButton(currentUser)
                                   : this.createLoginButton();
                                var ul = document.getElementById("list");
                                var li = document.createElement("li");
                                li.appendChild(button);
                                li.classList.add('mb-1');
                                ul.appendChild(li);
                                if (currentUser) {
                                    this.enabler();
                                }
    };


    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('btn');
        button.classList.add('btn-flat');
        button.classList.add('align-items-center');
        button.classList.add('rounded');
        button.href = '#';
        button.innerText = text;
        button.addEventListener('click', async () => {
                    await clickHandler();
        });
        return button;
    };

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    };

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    };

    enabler() {
        var inputs = document.getElementsByClassName('preload');
        for(var i = 0; i < inputs.length; i++) {
            inputs[i].disabled = false;
        }
    }
  };