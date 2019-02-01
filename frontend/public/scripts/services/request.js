;(function() {

	'use strict';

	/**
	 * @ngdoc service
	 * @name gmrsModule.service:request
	 * @description
	 * # request
	 * Service que faz o controle das chamadas dos servidos no backend.
	 */
	angular.module('rateShareApp').service('request', function ($firebaseStorage,$firebaseArray,$firebaseObject) {
		var blockConfig = {
			message: '<i class="fa fa-spinner fa-spin fa-5x fa-fw margin-bottom load"></i>',
			css: {
				border:         '0',
				backgroundColor:'transparent'
			}
		};

		function BlockUtil(element){

			this.block = function(){
				if(element){
					element.block(blockConfig);
				}else{
					$.blockUI(blockConfig);
				}
			};

			this.unBlock = function(){
				if(element){
					element.unblock();
				}else{
					$.unblockUI();
				}
			};
		}



		/**
		 * Configuracação dos metodos de request
		 */
		var request = {};

		request.getPostFirebaseUrl = function (key, successCallback, errorCallback) {
			
			var block = new BlockUtil();

			block.block();
			var refFirebase = firebase.storage().ref("posts/"+key);
			$firebaseStorage(refFirebase).$getDownloadURL().then(function(url) {
				successCallback(url);
				block.unBlock();

			},
			function(err) {
				console.log('Erro ao chamar a url ' + err);
				errorCallback(err);
				block.unBlock();

			});

		};
		
		request.getByKey = function (key,servicePosts, successCallback, errorCallback) {
			
			var block = new BlockUtil();

			console.log("acessou service ")
			block.block();
			var ref = firebase.database().ref(servicePosts+key);
			var obj = $firebaseObject(ref);
			obj.$loaded().then(
			function() {	
				block.unBlock();
				successCallback(obj)
			},
			function(errr){
				block.unBlock();
				errorCallback(errr);
			});
		};
		

		return request;

	});//fim service

})();