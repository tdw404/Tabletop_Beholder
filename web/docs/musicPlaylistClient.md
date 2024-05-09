# music playlist client

The `MusicPlaylistClient` class serves as a central client for making API calls to the MusicPlaylistService and abstracts away the details of handling authentication, making HTTP requests, and error handling. It provides a cleaner and more organized way to interact with the backend service in other parts of the application.

[musicPlaylistClient.js](./musicPlaylistClinet.js) defines a class named `MusicPlaylistClient`. This class acts as a client for calling the MusicPlaylistService API and provides methods to interact with the service. It makes use of the [Axios](https://codebots.com/docs/what-is-axios) [library](https://codeinstitute.net/global/blog/what-is-a-javascript-library/) for handling [HTTP requests](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview) to the [API](https://www.ibm.com/topics/api).

Here's a breakdown of the code:

## 1. Import Statements:
```javascript
import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";
```
The module imports three dependencies:
- `axios`: A popular JavaScript library used to make HTTP requests.
- `BindingClass`: This class provides utility methods for binding class methods to the class instance. It helps in handling `this` context issues in JavaScript. (Binding is an advanced Javascript concept, what we've documented here is all that you need to know for this project.)
- `Authenticator`: This is another class that handles authentication-related tasks, such as user login, token retrieval, etc.

## 2. MusicPlaylistClient Class:
```javascript
export default class MusicPlaylistClient extends BindingClass {
    // Class constructor
    constructor(props = {}) {
        super();

        // Bind class methods
        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getPlaylist', 'getPlaylistSongs', 'createPlaylist'];
        this.bindClassMethods(methodsToBind, this);

        // Initialize properties
        this.authenticator = new Authenticator();
        this.props = props;

        // Set base URL for axios requests
        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;

        // Call the clientLoaded method (probably used for some setup or initialization)
        this.clientLoaded();
    }

    // ...
}
```

## 3. Other Methods:
The class `MusicPlaylistClient` has several methods that interact with the MusicPlaylistService API. Here's a summary of each method's purpose:

- `clientLoaded()`: This method is called during the constructor and can be used to perform any tasks that need to be executed once the client has successfully loaded.

- `getIdentity(errorCallback)`: This method retrieves the identity (user information) of the current user. It uses the `Authenticator` class to check if the user is logged in and returns the user information if available.

- `login()`: This method initiates the login process using the `Authenticator` class.

- `logout()`: This method initiates the logout process using the `Authenticator` class.

- `getTokenOrThrow(unauthenticatedErrorMessage)`: This method retrieves the user token or throws an error with the provided message if the user is not authenticated.

- `getPlaylist(id, errorCallback)`: This method retrieves the metadata of a playlist with the given `id` using an HTTP GET request.

- `getPlaylistSongs(id, errorCallback)`: This method retrieves the list of songs in a playlist with the given `id` using an HTTP GET request.

- `createPlaylist(name, tags, errorCallback)`: This method creates a new playlist owned by the current user. It uses an HTTP POST request to the API.

- `addSongToPlaylist(id, asin, trackNumber, errorCallback)`: This method adds a song to a playlist with the specified `id` using an HTTP POST request.

- `search(criteria, errorCallback)`: This method performs a search for playlists based on the given `criteria` using an HTTP GET request.

- `handleError(error, errorCallback)`: This is a helper method used to log errors and run any provided error callback functions. It also extracts error messages from the API response if available.