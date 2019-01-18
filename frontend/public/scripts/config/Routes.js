angular.module('rateShareApp').config(['$stateProvider','$urlRouterProvider','NotificationProvider',
    function ($stateProvider,$urlRouterProvider,NotificationProvider) {
         // Initialize the Firebase SDK
       
        console.log("app ini ...");

        NotificationProvider.setOptions({
            delay: 10000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 20,
            horizontalSpacing: 20,
            positionX: 'center',
            positionY: 'top'
        });
    
        $urlRouterProvider.otherwise('/home');

        $stateProvider
        // .state('rateshare', {
        //   'abstract': true,
        //   templateUrl:'views/main/main.html',
        //   controller: 'IniCtrl'
        // })
        .state('rateshare', {
          'abstract': true,
          templateUrl: 'views/main/main.html',
          controller: 'IniCtrl',
          resolve: {
            authorize: ['authorization',
              function(authorization) {
                return authorization.authorize();
              }
            ]
          }
        })
        .state('home',{
          parent: 'rateshare',
          url:'/home',
          data: {
            roles: []
          },
          templateUrl: 'views/home/home.html',
          controller: 'HomeCtrl'
        }) 
        .state('signin', {
          parent: 'rateshare',
          url: '/login',
          data: {
            roles: []
          },
          templateUrl: 'views/login/login.html',
          controller: 'LoginCtrl'
         
        })
        .state('restricted', {
          parent: 'rateshare',
          url: '/restricted',
          data: {
            roles: ['User']
          },
          templateUrl: 'views/login/restrict.html',
          controller: 'AdminCtrl'
        })
        .state('accessdenied', {
          parent: 'rateshare',
          url: '/denied',
          data: {
            roles: []
          },
          templateUrl: 'views/login/denied.html'
        });
    }]).run(['$rootScope', '$state', '$stateParams', 'authorization', 'principal',
    function($rootScope, $state, $stateParams, authorization, principal) {
      $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;

        if (principal.isIdentityResolved()) authorization.authorize();
      });
    }
  ]);