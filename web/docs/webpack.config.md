# webpack.config.js

This Webpack configuration file defines how the project's code will be bundled and served during development. It includes plugins for copying assets and loading environment variables, entry points for each page, output configuration for the bundled files, and settings for the development server.

webpack.config.js is a configuration file for [Webpack](https://www.freecodecamp.org/news/an-intro-to-webpack-what-it-is-and-how-to-use-it-8304ecdc3c60/), a popular module bundler used in modern web development. This Webpack configuration file defines how the project's source code will be transformed, bundled, and output. Let's break down the different parts of this Webpack configuration. Webpack has extensive configuration and will only focus on the parts you will likely need to tweak.

1. Webpack Configuration Object:
```javascript
module.exports = {
  entry: {
    // Entry points for the application
  },
  output: {
    // Output configuration for the bundled files
  }
};
```

2. Entry Points:
```javascript
entry: {
  createPlaylist: path.resolve(__dirname, 'src', 'pages', 'createPlaylist.js'),
  viewPlaylist: path.resolve(__dirname, 'src', 'pages', 'viewPlaylist.js'),
  searchPlaylists: path.resolve(__dirname, 'src', 'pages', 'searchPlaylists.js'),
},
```
These are the entry points for the application. Each entry point represents a separate JavaScript file that serves as the starting point for creating the bundles. When you want to link a new js file in html 
this section shall be appended with info pointing to the new js file.


3. Output Configuration:
```javascript
output: {
  path: path.resolve(__dirname, 'build', 'assets'),
  filename: '[name].js',
  publicPath: '/assets/'
},
```
- `path`: The directory where the bundled files will be output. The path is resolved to the `build/assets` folder.
- `filename`: The naming convention for the output bundle files. The `[name]` placeholder will be replaced with the entry point name (e.g., `createPlaylist.js`, `viewPlaylist.js`, or `searchPlaylists.js`).
- `publicPath`: The public URL path where the assets will be served by the development server. The assets will be available under `/assets/` in the URL.