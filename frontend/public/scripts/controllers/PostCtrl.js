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


    
    var keyUser = "";

    console.log("post ctrl...")
    $scope.urlImg = "";
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
        $scope.urlImg =  "https://firebasestorage.googleapis.com/v0/b/rateshareteste.appspot.com/o/posts%2F" + key + "?alt=media"
        console.log($scope.usuariosRating);
        keyUser = data.userKey;
        getUserByUid(data.userKey)
      },
      function(err) {
        Notification.error("Erro ao buscar Img");
        console.log("err : " ,err);
      }
    );
    



    function getUserByUid(uid){
      var dados = {};
      var keyUid = "";
      keyUid = uid;
      GenericService.getUid(uid,"users/",
        function(data) {
          console.log(data);
          if(data != undefined){
            $scope.usuarioPost = data;
            carregaMeta(data)
          }


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
        GenericService.getUid(key,"users/",
          function(data) {
            console.log(data);
            if(data != undefined){
              $scope.usuarioPost = data;
              arrayDevolvido.push({
                user : {
                  key : key,
                  nome : data.nome,
                  email : data.email 
                },
                nota : value
              });
            }
          },
          function(err) {
            console.log("err : " ,err);
          }
      );
        
      });
      return arrayDevolvido;
    }
    
   
    function carregaMeta(post){
      ngMeta.setTitle(post.titulo); //Title = Eluvium | Playlist
      ngMeta.setTag('author', $scope.usuarioPost.nome);
      ngMeta.setTag('image', $scope.urlImg);
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