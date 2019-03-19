'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('LoginCtrl', function($scope,$state,$firebaseAuth, $firebaseObject,principal,Notification,User,GenericService) {
    var auth = $firebaseAuth();
    var ref = firebase.database().ref().child("users");
    $scope.usuario = {};
    $scope.users = User;

    // var provider = new firebase.auth.FacebookAuthProvider();
    // provider.addScope("email", "public_profile");

    var provider = new firebase.auth.FacebookAuthProvider();
    provider.addScope('email');
    provider.addScope('public_profile');


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
        if(error.code == "auth/wrong-password")
          Notification.error('Usuario ou senha errado');        
      });


    };

    $scope.logarFacebook = function(){
      auth.$signInWithPopup(provider).then(function(firebaseUser) {
        console.log("Signed in as:", firebaseUser.user);
        getUserRelation(firebaseUser.user.uid);
        
        principal.authenticate({
          name: firebaseUser,
          roles: ['User']
        });
        definiRota();
      }).catch(function(error) {
        Notification.error('Erro ao Logar :' + error);        
      });
    }

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
      if(auth.$getAuth()==undefined){
        principal.authenticate(null);
      }else{
        getUserRelation($scope.userLogado.uid)
        console.log($scope.userLogado);
      }
    }
    


    function getUserRelation(uid){
      $scope.definicaoUser = "";
      GenericService.getUid(uid,"users/",
        function(data) {
          if(data != undefined){
            $scope.usuario = data;
          }
        },
        function(err) {
          Notification.error("Erro ao buscar Img");
        }
      );
    }

    function definiRota(){
      if ($scope.returnToState) $state.go($scope.returnToState.name, $scope.returnToStateParams);
      else $state.go('home');
    }

    init()
    console.log("login ctrl...")

});