'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp')
  .controller('GerenciaCategoriasCtrl', function($scope,$state,Categorias,Notification) {
    $scope.categorias = Categorias;

    console.log("categorias ctrl...")

    $scope.deletar = function(d){
      $scope.categorias.$remove(d).then(function() {
        Notification.success('Sucesso ao deletar categoria');
      }).catch(function(error) {
        Notification.error('Erro ao deletar categoria');
      });
    }


    $scope.editInclui = function(d){
      if(d != null){
        $scope.categoria = $scope.categorias.$getRecord(d);
      }
      $('#modalCategoria').modal('show');
    }

    $scope.salvar = function(){
      if($scope.categoria.$id != null){
        $scope.categorias.$save($scope.categoria).then(function(sucesso) {
          console.log(sucesso);
          $('#modalCategoria').modal('hide');
          Notification.success('Sucesso ao incluir categoria');
  
        },function(error){
          console.log(error);
          Notification.error('Erro ao incluir categoria');
  
        });
      }
      else{
        $scope.categoria.data = new Date().toString();
        $scope.categorias.$add($scope.categoria).then(function(sucesso) {
          console.log(sucesso);
          $('#modalCategoria').modal('hide');
          Notification.success('Sucesso ao incluir categoria');
  
        },function(error){
          console.log(error);
          Notification.error('Erro ao incluir categoria');
  
        });
      }
      
    }
   

});
