import UserRoleClient from '../api/userRoleClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new UserRoleClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const userInfo = this.createUserInfoForHeader(currentUser);
        const navToProjectsList = this.createNavButton("Projects", 'projectsList.html?orgId=' + new URLSearchParams(window.location.search).get('orgId'));
        const navToMaterialManagement = this.createNavButton("Inventory", 'materialManagement.html?orgId=' + new URLSearchParams(window.location.search).get('orgId'));
        const navToUserManagement = this.createNavButton("User Management", 'userManagement.html?orgId=' + new URLSearchParams(window.location.search).get('orgId'));
        const navToAssignedTaskList = this.createNavButton("Assigned Task", 'assignedTaskList.html?orgId=' + new URLSearchParams(window.location.search).get('orgId'));
        //const navToNewRole = this.createNavButton("New Role", 'newRole.html');
        
        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        if(window.location.pathname != "/" & window.location.pathname != "/index.html" & window.location.pathname != "/newRole.html") {
            var loggedRole = await this.client.getUserRole(currentUser.email, new URLSearchParams(window.location.search).get('orgId'))
            loggedRole = loggedRole.jobRole;
            if (loggedRole == 'Manager') { header.appendChild(navToProjectsList); }
            header.appendChild(navToAssignedTaskList);
            //header.appendChild(navToNewRole);
            if (loggedRole == 'Manager') { header.appendChild(navToMaterialManagement); }
            if (loggedRole == 'Manager') { header.appendChild(navToUserManagement); }
        }
        header.appendChild(userInfo);
    }

    createNavButton(text, htmlTarget) {
        const button = document.createElement('a');
        button.classList.add('navButton');
        button.href = htmlTarget;
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }



    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';
        homeButton.innerText = 'Project Binford';

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}
