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
    
        // $urlRouterProvider.otherwise('/home');
       
        // // $stateProvider.state('rateshare',{
        // //     templateUrl:'views/main/main.html',
        // //     controller: 'IniCtrl'
        // // })
        // $stateProvider.state('rateshare', {
        //     'abstract': true,
        //     resolve: {
        //       authorize: ['authorization',
        //         function(authorization) {
        //           return authorization.authorize();
        //         }
        //       ]
        //     },
        //     templateUrl:'views/main/main.html',
        //     controller: 'IniCtrl'
        // })
        // .state('home', {
        //     parent: 'rateshare',
        //     url: '/home',
        //     data: {
        //       roles: []
        //     },
        //     views: {
        //       'content@': {
        //         templateUrl: 'views/home/home.html',
        //         controller: 'HomeCtrl'
        //       }
        //     }
        // })
        // .state('rateshare.gerencia-postagens',{
        //     templateUrl:'views/gerencia/posts.html',
        //     controller: 'GerenciaPostsCtrl',
        //     url:'/gerencia-postagens'
        // })
        // .state('rateshare.gerencia-users',{
        //     templateUrl:'views/gerencia/usuario.html',
        //     controller: 'GerenciaUsersCtrl',
        //     url:'/gerencia-users'
        // })
        // .state('rateshare.login',{
        //     templateUrl:'views/login/login.html',
        //     controller: 'LoginCtrl',
        //     url:'/login'
        // });
        $urlRouterProvider.otherwise('/home');

        $stateProvider.state('rateshare',{
          templateUrl:'views/main/main.html',
          controller: 'IniCtrl',
          resolve: {
                  authorize: ['authorization',
                    function(authorization) {
                      return authorization.authorize();
                    }
                  ]
                }
        })
        .state('rateshare.home',{
          templateUrl:'views/home/home.html',
          controller: 'HomeCtrl',
          url:'/home',
          data: {
            roles: []
          }
        })
        .state('rateshare.signin', {
          url: '/login',
          templateUrl: 'views/login/login.html',
          controller: 'LoginCtrl',
          data: {
            roles: []
          }
        }).state('rateshare.restricted', {
          url: '/restricted',
          templateUrl: 'views/login/restrict.html',
          data: {
            roles: ['Admin']
          }          
        }).state('rateshare.accessdenied', {
          url: '/denied',
          templateUrl: 'views/login/denied.html',
          data: {
            roles: []
          }

      });

    }]).run(['$rootScope', '$state', '$stateParams', 'authorization', 'principal',
    function($rootScope, $state, $stateParams, authorization, principal) {
      
      $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;

        if (principal.isIdentityResolved()) 
            authorization.authorize();
      });
      
    }
  ]);