'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('LoginCtrl', function($scope,$state,$firebaseAuth, $rootScope) {
    var auth = $firebaseAuth();

    $scope.userLogado = undefined;

    $rootScope.$on('$stateChangeStart', function (event, toState) {      
      if (toState.name === 'landingpage') {              
        if (!isAuthenticated()) { // Check if user allowed to transition                  
             event.preventDefault();   // Prevent migration to default state                  
             $state.go('rateshare.login');           
         }
       }
    });

    
    $scope.logarNormal = function(){
          auth.$signInWithEmailAndPassword($scope.email,$scope.pass).then(function(firebaseUser) {
            console.log("Signed in as:", firebaseUser.uid);
            $state.go("rateshare.home");
          }).catch(function(error) {
            console.log("Authentication failed:", error);
          });
    };
    $scope.deslogar = function(){
      auth.$signOut().then(function(sucess) {
        // Sign-out successful.
        $state.go("rateshare.home");
      }).catch(function(error) {
        // An error happened.
        console.log("Authentication failed:", error);
      });
    };

    function init(){
      $scope.userLogado = auth.$getAuth();
    }
    function isAuthenticated(){
      if(!$scope.userLogado)
        return false;
      else
        return true;
    }
    init()
    console.log("login ctrl...")

});