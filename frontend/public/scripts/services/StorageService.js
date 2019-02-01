/**
 * Created by Robson on 17/11/2015.
 */
;(function() {

  'use strict';

  /**
   */
  angular.module('rateShareApp').service('StorageService', function (request) {
    return {
      getPostImg : function (keyPost, successCallback, errorCallback) {
        // retorna firebase storate by keyPost
        request.getPostFirebaseUrl(keyPost, successCallback, errorCallback);
      }

  };//fim return

  });//fim service

})();