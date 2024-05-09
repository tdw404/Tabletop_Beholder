# DataStore

The `DataStore` class acts as a central hub to store and manage application data. It provides methods to read, update, and merge the [state](https://www.freecodecamp.org/news/state-in-javascript-explained-by-cooking-a-simple-meal-2baf10a787ee/#:~:text=State%20describes%20the%20status%20of,instantly%20react%20to%20that%20change.), and it automatically notifies all registered listeners whenever there is a change in the stored data. This pattern allows different parts of the application to observe and respond to data changes, leading to better coordination and synchronization among different components. It's a useful utility for building applications with complex data interactions and state management requirements. 

Due to the nature of this implementation of the Data Store pattern, where data is stored in JavaScript's memory, it lacks the ability to retain or cache data across page reloads.

[DataStore.js](DataStore.js) defines a JavaScript module for a `DataStore` class. This class acts as a centralized data store to manage and store data across the state of an application. It allows other components or modules to access, modify, and react to changes in the stored data by registering change listeners.

Let's break down the code:

# 1. Import Statement:
```javascript
import BindingClass from "./bindingClass";
```
The module imports the `BindingClass` that provides utility methods for binding class methods to the class instance. It helps in handling `this` context issues in JavaScript. (Binding is an advanced Javascript concept, what we've documented here is all that you need to know for this project.)

# 2. DataStore Class:
```javascript
export default class DataStore extends BindingClass {
    // Class constructor
    constructor(initialState = {}) {
        super();

        // Bind class methods
        this.bindClassMethods(['getState', 'get', 'setState', 'set', 'addChangeListener'], this);

        // Initialize the state and listeners
        this.state = initialState;
        this.listeners = [];
    }

    // ...
}
```

# 3. Other Methods:
The class `DataStore` provides several methods for managing the state and notifying listeners when the state changes:

- `getState()`: This method returns the entire current [state](https://www.freecodecamp.org/news/state-in-javascript-explained-by-cooking-a-simple-meal-2baf10a787ee/#:~:text=State%20describes%20the%20status%20of,instantly%20react%20to%20that%20change.) of the `DataStore`.

- `get(attribute)`: This method returns the current value of a specific attribute from the `DataStore` by providing the attribute name.

- `setState(newState)`: This method merges the current state of the `DataStore` with the new state provided as `newState`. If there are overlapping keys, the values from the `newState` will overwrite the corresponding values in the current state. After updating the state, all registered listeners are executed to notify them about the data updates.

- `set(attribute, value)`: This method sets or updates the value of a specific attribute in the `DataStore`. After updating the state, all registered listeners are executed to notify them about the data updates.

- `addChangeListener(listener)`: This method allows components or modules to register change listeners with the `DataStore`. When the state changes in any way, all registered listeners will be executed.