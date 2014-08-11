var debugEnabled=true;
var debug=function(text){
		if(debugEnabled){
			console.log(text);
		}
	};
var getUserInitials=function(name){
	wordArray=name.split(" ");
	return wordArray[0]+"."+ wordArray[1].substr(0,1);
}
var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'mgcrea.ngStrap',
		'ng-criterias', 'ngHtmlCompile', 'ngRoute', 'ngAnimate', 'ngTable',
		'ui.router', 'angularFileUpload', 'ui.calendar',
		'http-auth-interceptor', 'timer', 'localytics.directives',
		'daterangepicker','pascalprecht.translate','angular-loading-bar','ngQuickDate','xeditable' ]);


App.config(['$httpProvider', function($httpProvider) {
	$httpProvider.interceptors.push('httpRequestServerErrorInterceptor');
	$httpProvider.interceptors.push(function($q) {
        return {
          responseError: function(rejection) {
            if(rejection.status == 0) {
              alert("Connection lost with server :(");
              return;
            }
           return $q.reject(rejection);
        }
      };
	});
	$httpProvider.defaults.headers.common["FROM-ANGULAR"] = "true";
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
}]);

App.config(['$translateProvider', function ($translateProvider) {
	$translateProvider.preferredLanguage('en');
    $translateProvider.useStaticFilesLoader({
      prefix: _contextPath+'/assets/i18n/messages_',
      suffix: '.json'
    });
}]);

App.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.parentSelector = '.main';
  }])

App.run(function($rootScope,$templateCache,UsersREST) {
	$rootScope.page = '';
//	$templateCache.removeAll();
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		$rootScope.pageTitle=event;
	});
	
	var loadUserInfo = function() {
		UsersREST.get({
			service : 'user-info'
		}, function(data) {
			$rootScope.userInfo = data;
			$rootScope.$broadcast('userInfo', data);
		});
	};
	loadUserInfo();
});

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
            	}
            	$rootScope.$broadcast('event:http-request-error', requestError);
            }
            return $q.reject(rejection);
         }
     }
});