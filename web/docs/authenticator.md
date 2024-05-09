# Authenticator
The `Authenticator` class acts as an abstraction layer for [AWS Amplify's](https://github.com/aws-amplify/amplify-js#aws-amplify-is-a-javascript-library-for-frontend-and-mobile-developers-building-cloud-enabled-applications) authentication functionality, making it easier for the application to interact with AWS Cognito for user authentication. It provides methods for login, logout, user information retrieval, and token management, making it a convenient and reusable module for handling authentication in the application.

[authenticator.js](./authenticator.js) is a [JavaScript module](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Modules) that defines a class named `Authenticator`. This class [encapsulates](https://www.techtarget.com/searchnetworking/definition/encapsulation) authentication-related functionalities using [AWS Amplify](https://github.com/aws-amplify/amplify-js#aws-amplify-is-a-javascript-library-for-frontend-and-mobile-developers-building-cloud-enabled-applications), a library that simplifies working with AWS services.

Let's break down the code:

## 1. Import Statements:
```javascript
import BindingClass from "../util/bindingClass";
import { Auth } from 'aws-amplify';
```
The module imports two dependencies:
- `BindingClass`: This class provides utility methods for binding class methods to the class instance. It helps in handling `this` context issues in JavaScript. (Binding is an advanced Javascript concept, what we've documented here is all that you need to know for this project.)
- `Auth`: An object representing the authentication module of the AWS Amplify library.

## 2. Authenticator Class:
```javascript
export default class Authenticator extends BindingClass {
    // Class constructor
    constructor() {
        super();

        // Bind class methods
        const methodsToBind = ['getCurrentUserInfo'];
        this.bindClassMethods(methodsToBind, this);

        // Configure Cognito (AWS Cognito for authentication)
        this.configureCognito();
    }

    // ...
}
```

## 3. Other Methods:
The class `Authenticator` contains several methods to handle various aspects of authentication:

- `getCurrentUserInfo()`: This method retrieves the current user's information (email and name) after successful authentication. It uses AWS Amplify's `Auth.currentAuthenticatedUser()` to get the currently authenticated user and extract the relevant data.

- `isUserLoggedIn()`: This method checks whether the user is currently logged in. It attempts to call `Auth.currentAuthenticatedUser()` and returns `true` if successful (user is logged in) or `false` if there's an error (user is not logged in).

- `getUserToken()`: This method retrieves the user's authentication token (known as a [JWT](https://supertokens.com/blog/what-is-jwt#what-is-a-jwt)) after successful authentication. It uses AWS Amplify's `Auth.currentSession()` to get the current session and then extracts the token using `getIdToken().getJwtToken()`.

- `login()`: This method initiates the login process by calling `Auth.federatedSignIn()`. It is using Cognito for the login experience.

- `logout()`: This method initiates the logout process by calling `Auth.signOut()`. It logs the user out and clears any stored authentication tokens.

- `configureCognito()`: This method configures AWS Cognito using Amplify's `Auth.configure()` method. It sets up the necessary configurations, including the AWS Cognito User Pool ID, Client ID, OAuth settings, and other relevant parameters.