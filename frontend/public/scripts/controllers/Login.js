'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('LoginCtrl', function($scope,$state,$firebaseAuth, $firebaseObject,principal) {
    var auth = $firebaseAuth();
    var ref = firebase.database().ref().child("users");
    // var obj = new $firebaseObject(ref);

    $scope.userLogado = undefined;

    
    $scope.logarNormal = function(){
        // here, we fake authenticating and give a fake user
       
        
      auth.$signInWithEmailAndPassword($scope.email,$scope.pass).then(function(firebaseUser) {
        console.log("Signed in as:", firebaseUser.uid);
        // console.log(obj);
        var userRef = ref.child(firebaseUser.uid);
        console.log(userRef);
        principal.authenticate({
          name: firebaseUser,
          roles: ['User']
        });
        definiRota();
      }).catch(function(error) {
        console.log("Authentication failed:", error);
      });


    };

    $scope.deslogar = function(){
      auth.$signOut().then(function(sucess) {
        principal.authenticate(null);
        $state.go("signin");
      }).catch(function(error) {
        console.log("Authentication failed:", error);
      });
    };

    function init(){
      $scope.userLogado = auth.$getAuth();
    }
    


    function getUserRelation(uid){

    }

    function definiRota(){
      if ($scope.returnToState) $state.go($scope.returnToState.name, $scope.returnToStateParams);
      else $state.go('home');
    }

    init()
    console.log("login ctrl...")

});