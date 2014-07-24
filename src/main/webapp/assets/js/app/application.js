var getUserInitials=function(name){
	wordArray=name.split(" ");
	return wordArray[0]+"."+ wordArray[1].substr(0,1);
}
var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'mgcrea.ngStrap','ng-criterias','ngHtmlCompile',
		'ngRoute', 'ngAnimate', 'ngTable', 'ui.router', 'angularFileUpload','ui.calendar' ]);

App.run(function($rootScope,$templateCache,UsersREST) {
	$rootScope.page = '';
//	$templateCache.removeAll();
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		$rootScope.pageTitle=event;
	});
	
	$rootScope.userInfo={
			prenom:"loading..."
	};
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
