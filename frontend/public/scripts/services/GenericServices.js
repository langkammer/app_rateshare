/**
 * Created by Robson on 17/11/2015.
 */
;(function() {

  'use strict';

  /**
   */
  angular.module('rateShareApp').service('GenericService', function (request) {
    return {
      getByKey : function (key,servicePosts, successCallback, errorCallback) {
        // retorna firebase storate by keyPost
        request.getByKey(key,servicePosts, successCallback, errorCallback);
      }
 

  };//fim return

  });//fim service

})();