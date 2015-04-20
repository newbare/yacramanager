var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource',
                               		'ui.router','pascalprecht.translate','ngCookies','tmh.dynamicLocale','mgcrea.ngStrap','vcRecaptcha']);

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

function RegisterController($scope, $location,$window,RegistrationRest,alertService,vcRecaptchaService) {
	$scope.loadLogin = function() {
		$location.url('/');
		$window.location.href='/app/view/';
	};
	$scope.response = null;
    $scope.widgetId = null;
	$scope.captcha={
			model : {
				key: '6LeHhQUTAAAAAO4m-IuE3KnLfGK061bzTBZhlxik'
				}
    };
	 
	$scope.setResponse = function (response) {
         console.info('Response available');
         $scope.response = response;
     };
     $scope.setWidgetId = function (widgetId) {
         console.info('Created widget ID: %s', widgetId);
         $scope.widgetId = widgetId;
     };

	$scope.user={};
	if(typeof preFillRegistrationDTO !== 'undefined'){
		$scope.preFillRegistrationDTO=preFillRegistrationDTO;
		$scope.user.username=preFillRegistrationDTO.username;
		$scope.user.password=preFillRegistrationDTO.password;
		$scope.user.companyName=preFillRegistrationDTO.companyName;
		$scope.user.firstName=preFillRegistrationDTO.firstName;
		$scope.user.lastName=preFillRegistrationDTO.lastName;
		$scope.user.email=preFillRegistrationDTO.email;
		$scope.user.birthDay=preFillRegistrationDTO.birthDay;
		$scope.user.socialUser=preFillRegistrationDTO.socialUser;
		$scope.user.socialUserId=preFillRegistrationDTO.socialUserId;
		$scope.user.profileImageUrl=preFillRegistrationDTO.profileImageUrl;
		$scope.user.profileUrl=preFillRegistrationDTO.profileUrl;
		$scope.user.gender=preFillRegistrationDTO.gender.$name;
		//$scope.user=preFillRegistrationDTO;
	}
	$scope.register=function(){
		$scope.user.captchaToken=$scope.response;
		RegistrationRest.save($scope.user)
			.$promise.then(function(result) {
				alertService.show('success','Saved','Account has been created !');
				$scope.loadLogin();
		},function(reason){
			console.log('Failed validation');
			$window.location.reload();
			// In case of a failed validation you need to reload the captcha
            // because each response can be checked just once
            //vcRecaptchaService.reload($scope.widgetId);
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