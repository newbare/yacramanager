var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'mgcrea.ngStrap',
		'ngRoute', 'ngAnimate', 'ngTable', 'ui.router', 'angularFileUpload' ]);

App.run(function($rootScope) {
	$rootScope.page = '';
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		$rootScope.pageTitle=event;
	})
});
