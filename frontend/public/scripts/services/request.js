;(function() {

	'use strict';

	/**
	 * @ngdoc service
	 * @name gmrsModule.service:request
	 * @description
	 * # request
	 * Service que faz o controle das chamadas dos servidos no backend.
	 */
	angular.module('rateShareApp').service('request', function ($http) {
		var blockConfig = {
			message: '<i class="fa fa-spinner fa-spin fa-5x fa-fw margin-bottom load"></i>',
			css: {
				border:         '0',
				backgroundColor:'transparent'
			}
		};
        
        // username api
        var username  = 'solon'
        var password  = '1234';

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
		 * JSON Headers
		 */


		var baseURL = '/';

		/**
		 * Configuracação dos metodos de request
		 */
		var request = {};

	

		request.postFormEncoded = function (servico, parameter, callback) {
			var block = new BlockUtil();

			block.block();

			var _url = servico;
          
			var config = {
				headers: {
					'Content-type': 'application/x-www-form-urlencoded; charset=utf8',
                    'Authorization' : 'Basic ' + btoa(username + ":" + password)
				}
			};
			var http = $http.post(_url, $.param(parameter), config).then(
				function success(data){

					callback(data.data);
				}
				, function error(data, status){

					var _message = 'Erro ao chamar a url ' + _url + ' status ' + status;

					callback(data.data);

					console.log(_message);
				});



			http['finally'](function(){

				block.unBlock();

			});

		};

        

		request.get = function (url, successCallback, errorCallback) {

			var block = new BlockUtil();

			block.block();

			var http = $http.get('service/' + url, config.headers).then(
				function success(data){

					successCallback(data.data);
				}
				, function error(data, status){

					var _message = 'Erro ao chamar a url ' + url + ' status ' + status;

					console.log(_message);

					errorCallback(data.data);
				});


			http['finally'](function(){

				block.unBlock();

			});

		};

		return request;

	});//fim service

})();