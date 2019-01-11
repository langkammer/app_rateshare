/**
 * Created by Robson on 17/11/2015.
 */
;(function() {

  'use strict';

  /**
   * @ngdoc function
   * @name gmrsModule.service:AcaoService
   * @description
   * # AcaoService
   * Service que faz o controlle de areas inicialmente utilizado na tela de residuos solidos
   */
  angular.module('rateShareApp').service('ServicesGtrak', function (request) {
    return {
    testar: function (data, successCallback, errorCallback) {
      request.postFormEncoded('https://api.getrak.com/newkoauth/oauth/token', data, successCallback, errorCallback);
    } 
    /*
    ,
    deletarAgenda: function (data, successCallback, errorCallback) {
      request.postFormEncoded('agenda/deletarAgenda', data, successCallback, errorCallback);
    },
    listarAgenda : function (mesAno,successCallback, errorCallback){
    request.get('agenda/'+mesAno+'/listarAgenda',successCallback, errorCallback)
	  }
    */

    };//fim return

  });//fim service

})();