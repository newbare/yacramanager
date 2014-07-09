var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'ngRoute', 'ngAnimate' ]);

function LoginController($scope, $location) {
			$scope.navClass = function(page) {
				var currentRoute = $location.path().substring(1) || 'home';
				return page === currentRoute ? 'active' : '';
			};
			$scope.loadLogin = function() {
				$location.url('/home');
			};

			$scope.loadRegister = function() {
				$location.url('/register');
			};

			$scope.loadForgotAccount = function() {
				$location.url('/forgot-account');
			};

		};

App.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '../../assets/others/login.html',
		controller : LoginController
	})
	/*
	 * .when('/register', { templateUrl : 'views/register.html', controller :
	 * CraController }) .when('/forgot-account', { templateUrl :
	 * 'views/forgot-account.html', controller : AbsencesController })
	 */
	.otherwise({
		redirectTo : '/'
	});
} ]);