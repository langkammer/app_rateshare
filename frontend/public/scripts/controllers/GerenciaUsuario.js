'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('GerenciaUsersCtrl', function($scope,User) {
    $scope.users = User;

    console.log("usuarios gerenciar ctrl...")

    $scope.aprovar = function(d){
      d.ativo = true;
      $scope.users.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }

    $scope.reprovar = function(d){
      d.ativo = false;
      $scope.users.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }

    $scope.ativaAdmin = function(d){
      d.adm = true;
      $scope.users.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }

    $scope.desativaAdmin = function(d){
      d.adm = false;
      $scope.users.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }

    adm: 
    false
    ativo: 

    $scope.status = function(d){
      var retorno = "";
      if(d.ativo == true){
        retorno = 'btn-success'
      }
      if(d.ativo == false){
        retorno = 'btn-danger'
      }
    
      return retorno;
    }
});