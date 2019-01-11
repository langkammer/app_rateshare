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

    $scope.deslogar = function(){
      console.log("deslog ctrl...")

    };

});