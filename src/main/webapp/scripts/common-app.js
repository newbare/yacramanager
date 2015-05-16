/**
 * 
 */

App.config(['$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('httpRequestServerErrorInterceptor');
	$httpProvider.interceptors.push('httpConnectionLostInterceptor');
	$httpProvider.defaults.headers.common["FROM-ANGULAR"] = "true";
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
}]);

App.factory('httpRequestServerErrorInterceptor',function ($q,$rootScope) {
    return {
        'responseError': function(rejection) {
            // do something on error
            if(rejection.status === 500){
            	var requestError={
            			status: rejection.status,
            			title: rejection.statusText,
            			data: rejection.data,
            			url: rejection.config.url,
            			method: rejection.config.method
            	};
            	$rootScope.$broadcast('event:http-request-error', requestError);
            }
            return $q.reject(rejection);
         }
     };
});
App.factory('httpConnectionLostInterceptor', function($q,$rootScope) {
	return {
		responseError : function(rejection) {
			if (rejection.status === 0) {
				$rootScope.$broadcast('event:http-connection-lost');
				//alert("Connection lost with server :(");
				return;
			}
			return $q.reject(rejection);
		}
	};
});