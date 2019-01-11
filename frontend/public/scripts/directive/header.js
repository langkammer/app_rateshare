'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('rateShareApp')
	.directive('header',function(){
		return {
        templateUrl:'views/nav-bar/navbar.html',
        restrict: 'E',
        replace: true,
        controller: 'LoginCtrl'
    	}
});

