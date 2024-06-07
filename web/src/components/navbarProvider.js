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
                                            <a href="encounterLibrary.html" class="btn btn-flat align-items-center rounded preload disabled">
                                                Encounter Library
                                            </a>
                                        </li>
                                        <li class="mb-1">
                                            <a href="creatureLibrary.html" class="btn btn-flat align-items-center rounded preload disabled">
                                                Creature Library
                                            </a>
                                        </li>
                                        <li class="mb-1">
                                            <a href="spellLibrary.html" class="btn btn-flat align-items-center rounded preload disabled">
                                                Spell Library
                                            </a>
                                        </li>

                                        <li class="border-top my-3"></li>
                                        <li class="mb-1">
                                            <a class="btn btn-flat align-items-center rounded preload disabled">
                                                Run Encounter
                                            </a>
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
            inputs[i].classList.remove("disabled");
        }
    }
  };