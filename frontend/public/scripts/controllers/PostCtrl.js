'use strict';
/**
 */

angular.module('rateShareApp')
  .controller('PostCtrl', function($scope,
    $state,
    $stateParams,
    $firebaseAuth,
    Postagens,
    Notification,
    StorageService,
    GenericService,
    ngMeta
    ) {


    
    

    console.log("post ctrl...")
    
    var key = "";
    if($stateParams.key)
      key = $stateParams.key;
    
    $scope.usuariosRating = [];  



    $scope.compartilhar = function(){
      Socialshare.share({
        'provider': 'facebook',
        'attrs': {
          'socialshareUrl': 'http://720kb.net'
        }
      });
    }

    GenericService.getByKey(key,"posts/",
      function(data) {
        $scope.post = data;
        $scope.rate = _.sumBy(_.map(data.stars)) / _.map(data.stars).length;
        $scope.usuariosRating = extrairUsuario(data);
        carregaImg(data);
        console.log($scope.usuariosRating);
      },
      function(err) {
        Notification.error("Erro ao buscar Img");
        console.log("err : " ,err);
      }
    );
    

    function carregaImg(post){
      StorageService.getPostImg(key,
        function(url) {
          console.log(url);
          $scope.urlImg = url;
          carregaMeta(post,url);

        },
        function(err) {
          Notification.error("Erro ao buscar Img");
          console.log("err : " ,err);
        }
      );
    }
    

    function getUserByKey(key){
      var dados = {};
      GenericService.getByKey(key,"users/",
        function(data) {
          console.log(data);
          dados = data;
        },
        function(err) {
          console.log("err : " ,err);
          Notification.error("Erro ao buscar Img");
        }
      );
      return dados;
    }

    function extrairUsuario(data){
      var arrayDevolvido = []
      _.forEach(data.stars, function(value,key) {
        console.log(key,value);
        arrayDevolvido.push({
          user : {
            key : key
          },
          nota : value
        });
      });
      return arrayDevolvido;
    }
    
   
    function carregaMeta(post,urlImg){
      ngMeta.setTitle("asdfasdfasdf"); //Title = Eluvium | Playlist
      ngMeta.setTag('author', 'Rate Share');
      ngMeta.setTag('image', "asdfasdfasdf");
      ngMeta.setTag('og:locale',"asdfasdfasdf")
      ngMeta.setTag('og:title',"asdfasdfasdf")
      ngMeta.setTag('og:description',"asdfasdfasdf")
      ngMeta.setTag('og:image',"ASDFasdfasdfasdf")
      ngMeta.setTag('og:type',"website")
      ngMeta.setTag('description',"asdfasdfasdf")
      ngMeta.setDefaultTag('author', "asdfasdfasdf");
      //ngMeta.resetMeta();
    }  

});