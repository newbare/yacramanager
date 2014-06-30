'use strict';

var yaCRAApp = {};

var App = angular.module('yaCRAApp', []);

App.run(function($rootScope) {
    $rootScope.page = ''; 
 });

App.controller('NavCtrl', 
		['$scope', '$location', function ($scope, $location) {
		  $scope.navClass = function (page) {
		    var currentRoute = $location.path().substring(1) || 'home';
		    return page === currentRoute ? 'active' : '';
		  };
		  
		  $scope.loadHome = function () {
		        $location.url('/home');
		    };
		    
		      $scope.loadAbout = function () {
		        $location.url('/about');
		    };
		    
		      $scope.loadContact = function () {
		        $location.url('/contact');
		    };
		    
		}]);

App.config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider
			.when('/', {
				templateUrl : 'views/home.html',
				controller : HomeController
			})
			.when('/cra', {
				templateUrl : 'views/cra.html',
				controller : CraController
			}).when('/user-settings', {
				templateUrl : 'views/user-settings.html',
				controller : UserSettingsController
			}).when('/user-profile', {
				templateUrl : 'views/user-profile.html',
				controller : UserProfileController
			}).otherwise({
				redirectTo : '/'
			});
		} ]);
