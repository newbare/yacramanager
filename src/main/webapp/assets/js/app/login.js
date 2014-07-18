var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'ngRoute', 'ngAnimate' ]);

App.factory("RegistrationRest", function($resource) {
	return $resource(_contextPath+"/auth/api/register" , {}, {});
});

function LoginController($scope, $location) {
	$scope.navClass = function(page) {
		var currentRoute = $location.path().substring(1) || 'home';
		return page === currentRoute ? 'active' : '';
	};
	

	$scope.loadRegister = function() {
		$location.url('/register');
	};

	$scope.loadForgotPassword = function() {
		$location.url('/forgot-password');
	};
	
	$scope.error=false;
	$scope.errorMessage="";
	if(typeof error !== 'undefined'){
		$scope.error=error;
	}
	if (typeof errorMessage !== 'undefined'){
		$scope.errorMessage=errorMessage;
	}
};

function RegisterController($scope, $location,RegistrationRest) {
	$scope.loadLogin = function() {
		$location.url('/');
	};
	$scope.user={};
	$scope.register=function(){
		RegistrationRest.save($scope.user)
			.$promise.then(function(result) {
			console.log(result);
		});
	}
}

function PasswordRecoveryController($scope, $location) {
	$scope.loadLogin = function() {
		$location.url('/');
	};
}
App.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : '../../assets/others/login.html',
		controller : LoginController
	})
	.when('/register', {
		templateUrl : '../../assets/others/register.html',
		controller : RegisterController
	}).when('/forgot-password', {
		templateUrl : '../../assets/others/forgot-password.html',
		controller : PasswordRecoveryController
	})
	.otherwise({
		redirectTo : '/'
	});
} ]);