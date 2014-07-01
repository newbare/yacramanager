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
		    
		      $scope.loadCRA = function () {
		        $location.url('/cra');
		    };
		    
		      $scope.loadAbsences= function () {
		        $location.url('/absences');
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
			})
			.when('/absences', {
				templateUrl : 'views/absences.html',
				controller : AbsencesController
			})
			.when('/user-settings', {
				templateUrl : 'views/user-settings.html',
				controller : UserSettingsController
			}).when('/user-profile', {
				templateUrl : 'views/user-profile.html',
				controller : UserProfileController
			}).otherwise({
				redirectTo : '/'
			});
		} ]);
