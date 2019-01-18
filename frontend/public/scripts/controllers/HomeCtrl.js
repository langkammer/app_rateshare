'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('HomeCtrl', function($scope,ServicesGtrak) {
    console.log("home ctrl...")
    var username  = 'starcar@starcar'
    var password  = '8b1d2fe303b9';
    function ini(){
      ServicesGtrak.testar(
        { 
          'grant_type' : 'password',
          'username' :  username , 
          'password' : password
        }
        ,function (sucesso) {
           console.log(sucesso)
         },
         function (erro) {
            console.log(erro)
        }
      );
    }

  

    // ini();
});