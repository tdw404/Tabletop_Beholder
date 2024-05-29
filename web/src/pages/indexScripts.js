import NavbarProvider from"../components/navbarProvider.js";
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

const COGNITO_NAME_KEY = 'cognito-name';
const COGNITO_EMAIL_KEY = 'cognito-name-results';
const EMPTY_DATASTORE_STATE = {
    [COGNITO_NAME_KEY]: '',
    [COGNITO_EMAIL_KEY]: '',
};
/**
 * Adds functionality to the landing page.
 */
 class IndexScripts extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount'], this);
        this.dataStore = new DataStore(EMPTY_DATASTORE_STATE);
        this.navbarProvider = new NavbarProvider();
    };


 mount() {
    document.getElementById('welcomeText').hidden = false;
    this.navbarProvider.addNavbarToPage();
 };
};

 /**
  * Main method to run when the page contents have loaded.
  */
 const main = async () => {
     const indexScripts = new IndexScripts();
     indexScripts.mount();
 };

 window.addEventListener('DOMContentLoaded', main);
