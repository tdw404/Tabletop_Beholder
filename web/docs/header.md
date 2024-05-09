# Header

The `Header` class encapsulates the logic for building and displaying the website header, including the site title, user information, and login/logout buttons. It uses the `MusicPlaylistClient` class for authentication-related tasks. The `BindingClass` inheritance ensures that the class methods are properly bound to the class instance, avoiding issues with the `this` context in JavaScript.

[header.js](./header.js) defines a JavaScript module for a "Header" [component](https://dev.to/xavortm/what-are-components-in-the-front-end-and-why-do-we-need-them-2o2p) used in a website. The header component is responsible for displaying the website title, user information, and login/logout buttons. The component makes use of the `MusicPlaylistClient` class for handling authentication-related tasks and obtaining user information.

Let's break down the code:

## 1. Import Statements:
```javascript
import MusicPlaylistClient from '../api/musicPlaylistClient';
import BindingClass from "../util/bindingClass";
```
The module imports two dependencies:
- `MusicPlaylistClient`: This is a class or module that interacts with the MusicPlaylistService API, as we have seen in previous code snippets.
- `BindingClass`: This class provides utility methods for binding class methods to the class instance. It helps in handling `this` context issues in JavaScript. (Binding is an advanced Javascript concept, what we've documented here is all that you need to know for this project.)

## 2. Header Class:
```javascript
export default class Header extends BindingClass {
    // Class constructor
    constructor() {
        super();

        // Bind class methods
        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        // Create an instance of MusicPlaylistClient
        this.client = new MusicPlaylistClient();
    }

    // ...
}
```

## 3. Other Methods:
The class `Header` contains several methods for building and adding the header to the webpage:

- `addHeaderToPage()`: This method is called to add the header to the webpage. It [awaits](https://www.theodinproject.com/lessons/node-path-javascript-async-and-await) the result of calling `this.client.getIdentity()` to get the current user's information. Then, it creates the site title and user information elements and appends them to the header element with the ID 'header'.

- `createSiteTitle()`: This method creates the site title element, which includes a home button with a link to the 'index.html' page. The title is wrapped in a `div` with the class 'site-title'.

- `createUserInfoForHeader(currentUser)`: This method creates the user information section for the header. It takes the `currentUser` object (user information) as an argument and decides whether to show the login or logout button based on the user's authentication status. It calls `this.createLoginButton()` or `this.createLogoutButton(currentUser)` accordingly.

- `createLoginButton()`: This method creates the login button element with the text "Login" and sets a click event listener that calls `this.client.login()` when clicked.

- `createLogoutButton(currentUser)`: This method creates the logout button element with the text "Logout: { currentUser.name }". It uses the user's name from the `currentUser` object to display the current user's name on the logout button. It sets a click event listener that calls `this.client.logout()` when clicked.

- `createButton(text, clickHandler)`: This is a helper method used to create a generic button element with the provided text and click event handler. The `clickHandler` argument is a function that will be executed when the button is clicked.