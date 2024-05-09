# package.json

`package.json` is a configuration file used in Node.js projects. It contains metadata about the project, as well as the list of dependencies and scripts required for building, running, and managing the project. Let's break down the different parts of this `package.json` file:

1. Package Metadata:
```json
{
  "name": "u5-project-template-static-website",
  "description": "Website for the NSS Unit 5 Project",
  "version": "1.0.0",
  "license": "UNLICENSED",
  "main": "js/index.js"
}
```
- `"name"`: The name of the project is "u5-project-template-static-website".
- `"description"`: A brief description of the project, which is "Website for the NSS Unit 5 Project".
- `"version"`: The version of the project is "1.0.0".
- `"license"`: The license information for the project is specified as "UNLICENSED". This typically means that the project is not open-source or publicly available under a standard license.
- `"main"`: The entry point of the project is set to "js/index.js". This is the main file that will be loaded when the project is used as a dependency in other projects.

2. Scripts:
```json
"scripts": {
  "prepublishOnly": "npm run build",
  "build": "webpack --mode=production --devtool=source-map",
  "build-dev": "webpack --mode=development --devtool=source-map",
  "run-local": "API_LOCATION=local webpack-dev-server --mode=development --hot",
  "run-remote": "API_LOCATION=remote webpack-dev-server --mode=development --hot"
}
```
The "scripts" section defines various commands that can be executed using [npm](https://www.freecodecamp.org/news/what-is-npm-a-node-package-manager-tutorial-for-beginners/) or [yarn](https://engineering.fb.com/2016/10/11/web/yarn-a-new-package-manager-for-javascript/). For example:
- `"prepublishOnly"`: This script runs the "build" script before publishing the project.
- `"build"`: This script runs Webpack in production mode, generating a production build of the project with source maps for debugging.
- `"build-dev"`: This script runs Webpack in development mode, generating a development build with source maps for debugging.
- `"run-local"` and `"run-remote"`: These scripts start the Webpack Dev Server in development mode with [hot module replacement](https://blog.bitsrc.io/webpacks-hot-module-replacement-feature-explained-43c13b169986) (HMR) enabled. The `API_LOCATION` environment variable is set to either "local" or "remote" based on the script used.

3. Dependencies:
```json
"dependencies": {
  "aws-amplify": "^5.0.7",
  "axios": "^1.1.2",
  "webpack": "^5.74.0",
  "webpack-cli": "^4.10.0",
  "webpack-dev-server": "^4.11.1"
}
```
- `"aws-amplify": "^5.0.7"`: The project depends on version 5.0.7 of the `aws-amplify` library, which is a popular library for building cloud-powered applications using Amazon Web Services (AWS).
- `"axios": "^1.1.2"`: The project depends on version 1.1.2 of the `axios` library, which is a popular HTTP client for making API requests.
- `"webpack": "^5.74.0"` and `"webpack-cli": "^4.10.0"`: These dependencies are for Webpack, which is a module bundler used to bundle JavaScript and other assets for the web.
- `"webpack-dev-server": "^4.11.1"`: This dependency is for the Webpack Dev Server, which provides a development server with live reloading and hot module replacement during development.

4. Dev Dependencies:
```json
"devDependencies": {
  "copy-webpack-plugin": "^11.0.0",
  "dotenv-webpack": "^8.0.1"
}
```
- `"copy-webpack-plugin": "^11.0.0"`: This dependency is for the Copy Webpack Plugin, which allows developers to copy files or directories as part of the Webpack build process.
- `"dotenv-webpack": "^8.0.1"`: This dependency is for the Dotenv Webpack Plugin, which loads environment variables from a .env file into process.env during the Webpack build process.

The `package.json` file plays a crucial role in managing the project's dependencies and scripts, making it easier to build, test, and run the application. Developers can use the defined scripts to run common tasks such as building the project, starting a development server, or running other custom tasks specific to the project's needs.