'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('IniCtrl', function($scope,$state,$firebaseAuth) {
    console.log("ini ctrl...")
    var auth = $firebaseAuth();

    $scope.deslogar = function(){
      console.log("deslog ctrl...")
      principal.authenticate(null);
      $state.go('rateshare.home');
    };

    $scope.logar = function(){
      auth.$signInWithEmailAndPassword($scope.email,$scope.pass).then(function(firebaseUser) {
        console.log("Signed in as:", firebaseUser.uid);

      }).catch(function(error) {
        console.log("Authentication failed:", error);
      });
    }

});