var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource',
                               		'ui.router','pascalprecht.translate','ngCookies','tmh.dynamicLocale','mgcrea.ngStrap']);

App.run(function($rootScope) {
	$rootScope.appContextPath=_contextPath;
});

App.factory("RegistrationRest", function($resource) {
	return $resource(_contextPath+"/auth/api/register" , {}, {});
});

App.factory("AuthenticationREST", function($resource) {
	return $resource(_contextPath + "/app/api/company/:id", {}, {
		activateAccount : {
			url : _contextPath + "/auth/api/activate:key",
			method : 'GET',
			params : {
				key : '@key'
			}
		},
		recoverPassword : {
			url : _contextPath + "auth/api/password-recovery",
			method : 'POST'
		}
	});
});

App.service('alertService', function($alert) {
	this.show = function(type,title, content) {
		// Service usage
		var myAlert = $alert({
			title : title,
			content : content,
			type : type,
			keyboard : true,
			show : false,
			duration: 5,
			template:  _contextPath+'/templates/alert/alert.tpl.html',
			container: '#alerts-container'
		});
		myAlert.$promise.then(myAlert.show);
	};
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
	$scope.activationFailed=false;
	$scope.activationSuccess=false;
	$scope.activationMessage="";
	if(typeof error !== 'undefined'){
		$scope.error=error;
	}
	if (typeof errorMessage !== 'undefined'){
		$scope.errorMessage=errorMessage;
	}
	if(typeof activationFailed !== 'undefined'){
		$scope.activationFailed=activationFailed;
	}
	if(typeof activationSuccess !== 'undefined'){
		$scope.activationSuccess=activationSuccess;
	}
	if (typeof activationMessage !== 'undefined'){
		$scope.activationMessage=activationMessage;
	}
}

function RegisterController($scope, $location,RegistrationRest,alertService) {
	$scope.loadLogin = function() {
		$location.url('/');
	};
	$scope.user={};
	if(typeof preFillRegistrationDTO !== 'undefined'){
//		$scope.preFillRegistrationDTO=preFillRegistrationDTO;
//		$scope.user.username=preFillRegistrationDTO.username;
//		$scope.user.firstName=preFillRegistrationDTO.firstName;
//		$scope.user.lastName=preFillRegistrationDTO.lastName;
//		$scope.user.email=preFillRegistrationDTO.email;
//		$scope.user.socialUser=preFillRegistrationDTO.socialUser;
//		$scope.user.profileImageUrl=preFillRegistrationDTO.profileImageUrl;
		$scope.user=preFillRegistrationDTO;
	}
	$scope.register=function(){
		RegistrationRest.save($scope.user)
			.$promise.then(function(result) {
				alertService.show('success','Saved','Account has been created !');
				$scope.loadLogin();
		});
	};
}

function PasswordRecoveryController($scope, $location,AuthenticationREST) {
	$scope.loadLogin = function() {
		$location.url('/');
	};
	$scope.email="";
	$scope.recoverPassword=function(){
		if($scope.email!==undefined && $scope.email!==""){
			console.log($scope.email);
			AuthenticationREST.recoverPassword(JSON.stringify($scope.email));
		}
	};
}

App.config([ '$stateProvider', '$urlRouterProvider','$locationProvider','$translateProvider','tmhDynamicLocaleProvider',
     		function($stateProvider, $urlRouterProvider,$locationProvider,$translateProvider,tmhDynamicLocaleProvider) {

//	$locationProvider.html5Mode(true).hashPrefix('!');
//	$urlRouterProvider.when('','/login')
//	.otherwise('/login');
//	
//	$stateProvider
//	.state('login', {
//		url : "/login",
//		templateUrl : _contextPath+'/templates/login.html',
//		controller : LoginController,
//		data: {
//	        pageTitle: 'Login page'
//	      }
//	})
//	.state('register', {
//		url : "/register",
//		templateUrl : _contextPath+'/templates/register.html',
//		controller : RegisterController,
//		data: {
//	        pageTitle: 'Registration'
//	      }
//	})
//	.state('forgot-password', {
//		url : "/forgot-password",
//		templateUrl : _contextPath+'/templates/forgot-password.html',
//		controller : PasswordRecoveryController,
//		data: {
//	        pageTitle: 'Registration'
//	      }
//	});
	$translateProvider.determinePreferredLanguage();
	
	$translateProvider.useStaticFilesLoader({
	      prefix: _contextPath+'/i18n/',
	      suffix: '.json'
	});

	$translateProvider.useCookieStorage();

	tmhDynamicLocaleProvider
			.localeLocationPattern(_contextPath+'/bower_components/angular-i18n/angular-locale_{{locale}}.js');
	tmhDynamicLocaleProvider
			.useCookieStorage('NG_TRANSLATE_LANG_KEY');
}]);