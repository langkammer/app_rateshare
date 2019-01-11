'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sbAdminApp
 */
angular.module('rateShareApp').factory('authorization', ['$rootScope', '$state', 'principal',
function($rootScope, $state, principal) {
  return {
    authorize: function() {
      return principal.identity()
        .then(function() {
          var isAuthenticated = principal.isAuthenticated();
          if($rootScope.toState){
            if ($rootScope.toState.data.roles && $rootScope.toState.data.roles.length > 0 && !principal.isInAnyRole($rootScope.toState.data.roles)) {
              if (isAuthenticated) $state.go('rateshare.accessdenied'); // user is signed in but not authorized for desired state
              else {
                // user is not authenticated. stow the state they wanted before you
                // send them to the signin state, so you can return them when you're done
                $rootScope.returnToState = $rootScope.toState;
                $rootScope.returnToStateParams = $rootScope.toStateParams;
  
                // now, send them to the signin state so they can log in
                $state.go('rateshare.signin');
              }
            }
          }
          
        });
    }
  };
}
])