'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */

angular.module('rateShareApp')
  .controller('AdminCtrl', function($scope,$state,$firebaseAuth,Postagens,Notification) {
    console.log("ini ctrl...")

    $scope.posts = Postagens;

    
    $scope.aprovar = function(d){
      d.aprovado = true;
      $scope.posts.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }

    $scope.reprovar = function(d){
      d.aprovado = false;
      $scope.posts.$save(d).then(function() {
        Notification.success('Sucesso ao mudar de status');
      }).catch(function(error) {
        Notification.error('Erro ao mudar de status');
      });
    }


    $scope.remover = function(d){
      d.aprovado = false;
      $scope.posts.$remove(d).then(function() {
        Notification.success('Sucesso ao deletar post');
      }).catch(function(error) {
        Notification.error('Erro ao deletar post');
      });
    }

    $scope.status = function(bolean){
      if(bolean == true){
        return 'btn-success'
      }
      else{
        return 'btn-danger'
      }
    }

    $scope.deslogar = function(){
      console.log("deslog ctrl...")

    };

    $scope.verPostagem = function(p){
      console.log(p)
      $state.go('ver-post', {key :  p.$id});
    };
});