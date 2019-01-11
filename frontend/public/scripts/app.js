'use strict';
/**
 * @ngdoc overview
 * @name sbAdminApp
 * @description
 * # sbAdminApp
 *
 * Main module of the application.
 */
var configFirebase = {
    apiKey: "AIzaSyDp3fyEuS66eQzQI5cEyPEucoYW55_ZVVM",
    authDomain: "rateshareteste.firebaseapp.com",
    databaseURL: "https://rateshareteste.firebaseio.com",
    projectId: "rateshareteste",
    storageBucket: "rateshareteste.appspot.com",
    messagingSenderId: "191955991258"
    };
firebase.initializeApp(configFirebase);
angular.module('rateShareApp', [
    'ui.router',
    'ui.bootstrap',
	  'ui-notification',
    'checklist-model',
    'firebase'
  ]);