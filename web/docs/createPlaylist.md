# Create Playlist

In summary, this JavaScript module sets up the "create playlist" page with event listeners and API interactions. It uses classes and utility modules to handle data management, API calls, and page rendering.

[createPlaylist.js](./createPlaylist.js) is a [JavaScript module](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Modules) that handles the logic for the "create playlist" page of a website. It uses various classes and utility modules to interact with the website's API, manage data, and handle user interactions. Let's break down the code step by step:

## 1. Import Statements:
```javascript
import MusicPlaylistClient from '../api/musicPlaylistClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';
```
The code begins by [importing](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Modules#importing_features_into_your_script) four modules:

- `MusicPlaylistClient`: It is an API client used to interact with the backend playlist API.
- `Header`: Is a [component](https://dev.to/xavortm/what-are-components-in-the-front-end-and-why-do-we-need-them-2o2p) that adds a header to the webpage.
- `BindingClass`: This class provides utility methods for binding class methods to the class instance. It helps in handling `this` context issues in JavaScript. (Binding of `this` is an advanced Javascript concept)
- `DataStore`: This is a utility module that stores data from our API and serves as a intermediary between the API and what is rendered to the page.

## 2. CreatePlaylist Class:
```javascript
class CreatePlaylist extends BindingClass {
    // Constructor
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'redirectToViewPlaylist'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToViewPlaylist);
        this.header = new Header(this.dataStore);
    }

    // Mount method to initialize the page
    mount() {
        document.getElementById('create').addEventListener('click', this.submit);
        this.header.addHeaderToPage();
        this.client = new MusicPlaylistClient();
    }

    // Method to handle submit button click
    async submit(evt) {
        // ...
    }

    // Method to redirect to the playlist view page
    redirectToViewPlaylist() {
        // ...
    }
}
```
The `CreatePlaylist` class is defined, extending `BindingClass` to utilize its method binding functionality. It has three methods:

- `constructor`: Initializes the class, binds relevant methods, and creates instances of `DataStore` and `Header`. The `addChangeListener` method ensures that whenever the playlist is updated in the data store, the `redirectToViewPlaylist` method will be called to redirect the user to the view playlist page.

- `mount`: This method is called when the page loads and sets up event listeners. Specifically, it adds an event listener to the element with ID `'create'` button to trigger the `submit` method when clicked. It also adds the header to the page and creates an instance of the `MusicPlaylistClient`.

- `submit`: This is an [asynchronous](https://www.freecodecamp.org/news/asynchronous-programming-in-javascript#conclusion) method that is called when the "create playlist" submit button is pressed. It handles form data (playlist name and tags), displays loading indicators, and uses the `MusicPlaylistClient` to create the playlist. If there is an error, it displays the error message.

- `redirectToViewPlaylist`: This method is called when the playlist is updated in the data store. It retrieves the playlist's ID and redirects the user to the view playlist page by changing the window's location. It is also passing data to using `?id=${playlist.id}` 

    Passing data from one site to another using [query parameters](https://www.abstractapi.com/api-glossary/query-parameters#what-are-query-parameters) is a common technique used in web development to transfer information between pages or websites. Query parameters are key-value pairs appended to the end of a URL after a question mark `?`. Each parameter is separated by an ampersand `&`.
    For example:

    ```
    https://www.example.com/page?name=John&age=30
    ```

    In this example, the URL contains two query parameters: `name=John` and `age=30`.

    Here's how data can be passed from one site to another using query parameters:

    ### a. Generating the URL with Query Parameters:
    The source website (Site A) constructs a URL that includes the data to be passed. This URL can be created dynamically by appending the query parameters to the base URL. For instance, if you have a form where a user enters their name and age, the URL could be generated like this:

    ```javascript
    const name = document.getElementById('nameInput').value;
    const age = document.getElementById('ageInput').value;
    const url = `https://www.example.com/page?name=${encodeURIComponent(name)}&age=${encodeURIComponent(age)}`;
    ```

    ### b. Redirecting to the Destination Site:
    To send the user with the data to the destination website (Site B), the source website can trigger a redirect by setting `window.location.href` to the constructed URL. This will take the user to the new site along with the data in the query parameters.

    ```javascript
    window.location.href = url;
    ```

    ### c. Retrieving the Data on the Destination Site:
    On the destination website (Site B), the data from the query parameters can be accessed using JavaScript. The `URLSearchParams` API can help extract the parameters:

    ```javascript
    const urlParams = new URLSearchParams(window.location.search);
    const name = urlParams.get('name');
    const age = urlParams.get('age');
    ```

    Now, the `name` and `age` variables on Site B will contain the values passed from Site A.


## 3. Main Function:
```javascript
const main = async () => {
    const createPlaylist = new CreatePlaylist();
    createPlaylist.mount();
};

window.addEventListener('DOMContentLoaded', main);
```
This code defines a main function  (think of this as the javascript equivalent of the [main function in java](https://www.geeksforgeeks.org/java-main-method-public-static-void-main-string-args/)) that creates an instance of the `CreatePlaylist` class and calls its `mount` method when the DOM content has loaded (i.e., the webpage has finished loading).