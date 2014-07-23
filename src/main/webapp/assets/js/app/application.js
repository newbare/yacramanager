var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'mgcrea.ngStrap','ng-criterias','ngHtmlCompile',
		'ngRoute', 'ngAnimate', 'ngTable', 'ui.router', 'angularFileUpload','ui.calendar' ]);

App.run(function($rootScope,$templateCache) {
	$rootScope.page = '';
//	$templateCache.removeAll();
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		$rootScope.pageTitle=event;
	});
});
