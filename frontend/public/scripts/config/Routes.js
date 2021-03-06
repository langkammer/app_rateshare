angular.module('rateShareApp').config(['$locationProvider','$stateProvider','$urlRouterProvider','NotificationProvider','ngMetaProvider',
    function ($locationProvider,$stateProvider,$urlRouterProvider,NotificationProvider,ngMetaProvider) {
         // Initialize the Firebase SDK
        
        $locationProvider.html5Mode(true);
        $locationProvider.hashPrefix('');
        
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
            roles: [],
            meta: {
              'title': 'Home page',
              'description': 'This is the description shown in Google search results'
            }
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
        .state('gerencia-post', {
          parent: 'rateshare',
          url: '/gerencia-posts',
          data: {
            roles: ['User']
          },
          templateUrl: 'views/gerencia/post-gerencia.html',
          controller: 'AdminCtrl'
        })
        .state('gerencia-categoria', {
          parent: 'rateshare',
          url: '/gerencia-categoria',
          data: {
            roles: ['User']
          },
          templateUrl: 'views/gerencia/cad-categorias.html',
          controller: 'GerenciaCategoriasCtrl'
        })
        .state('gerencia-usuario', {
          parent: 'rateshare',
          url: '/gerencia-usuario',
          data: {
            roles: ['User']
          },
          templateUrl: 'views/gerencia/usuario.html',
          controller: 'GerenciaUsersCtrl'
        })
        .state('ver-post', {
          parent: 'rateshare',
          url: '/ver-postagem?key',
          data: {
            meta: {
              'title': 'Ver Post',
              'og:image': 'http://www.yourdomain.com/img/facebookimage.jpg',
              'author': 'Robson',
              'og:title': 'All You Need To Know About Pet Vaccinations',
              'og:description': 'Useful information about Routine Vaccines and Boosters for dogs and cats, including start vaccines for puppies and kittens.'
            }
          },
          params: {
            key: null
          },
          templateUrl: 'views/posts/ver-post.html',
          controller: 'PostCtrl'
        })
        .state('accessdenied', {
          parent: 'rateshare',
          url: '/denied',
          data: {
            roles: []
          },
          templateUrl: 'views/login/denied.html'
        })
        .state('posts', {
          parent: 'rateshare',
          url: '/postagens',
          data: {
            roles: []
          },
          templateUrl: 'views/posts/postagens.html',
          controller: 'PostagensCtrl'

        });
       
        ngMetaProvider.useTitleSuffix(true);
        ngMetaProvider.setDefaultTitle('Rate e Share');
        ngMetaProvider.setDefaultTitleSuffix(' | Rate e Shhare');
        ngMetaProvider.setDefaultTag('author', 'Robson Emilio Langkammer');

    }]).run(['$rootScope', '$state', '$stateParams', 'authorization', 'principal','ngMeta',
    function($rootScope, $state, $stateParams, authorization, principal,ngMeta) {
      ngMeta.init();

      $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;
       

        if (principal.isIdentityResolved()) authorization.authorize();
      });
    }
  ]);