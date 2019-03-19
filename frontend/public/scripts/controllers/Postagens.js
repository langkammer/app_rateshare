'use strict';
/**
 */

angular.module('rateShareApp')
  .controller('PostagensCtrl', function(
    $scope,
    $state,
    $stateParams,
    $firebaseAuth,
    $firebaseArray,
    Postagens,
    Notification,
    StorageService,
    GenericService,
    ngMeta
    ) {

    $scope.posts = [];

    function listarPosts(){
      var ref = firebase.database().ref("posts");

      var list = $firebaseArray(ref.orderByChild("aprovado").equalTo(true));

			list.$loaded().then(
			function(sucess) {	
        var array = []
        _.forEach(sucess,function(data){
            data.rate = rate(data)
            array.push(data);
        })
        $scope.posts = array;
			},
			function(errr){
				Notification.errr("Erro",errr);
			});
    }

    listarPosts();

    function rate(data){
      return _.sumBy(_.map(data.stars)) / _.map(data.stars).length
    }
    
    $scope.verPostagem = function(p){
      console.log(p)
      $state.go('ver-post', {key :  p.$id});
    };
    console.log("post ctrl...")
    

    // $scope.compartilhar = function(){
    //   Socialshare.share({
    //     'provider': 'facebook',
    //     'attrs': {
    //       'socialshareUrl': 'http://720kb.net'
    //     }
    //   });
    // }

   
   
    // function carregaMeta(post,urlImg){
    //   ngMeta.setTitle("Ver Postagens"); //Title = Eluvium | Playlist
    //   ngMeta.setTag('author', 'Rate Share');
    //   ngMeta.setTag('og:title',"Todas Postagens")
    //   ngMeta.setTag('og:type',"website")
    //   //ngMeta.resetMeta();
    // }  

});