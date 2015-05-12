angular.module('truncate', [])
    .filter('characters', function () {
        return function (input, chars, breakOnWord) {
            if (isNaN(chars)) return input;
            if (chars <= 0) return '';
            if (input && input.length > chars) {
                input = input.substring(0, chars);

                if (!breakOnWord) {
                    var lastspace = input.lastIndexOf(' ');
                    //get last space
                    if (lastspace !== -1) {
                        input = input.substr(0, lastspace);
                    }
                }else{
                    while(input.charAt(input.length-1) === ' '){
                        input = input.substr(0, input.length -1);
                    }
                }
                return input + '...';
            }
            return input;
        };
    })
    .filter('words', function () {
        return function (input, words) {
            if (isNaN(words)) return input;
            if (words <= 0) return '';
            if (input) {
                var inputWords = input.split(/\s+/);
                if (inputWords.length > words) {
                    input = inputWords.slice(0, words).join(' ') + '...';
                }
            }
            return input;
        };
    });

var debugEnabled=true;
var debug=function(text){
		if(debugEnabled){
			console.log(text);
		}
	};
var getUserInitials=function(name){
	wordArray=name.split(" ");
	return wordArray[0]+"."+ wordArray[1].substr(0,1);
};

var clone=function(obj) {
	if (null === obj || "object" != typeof obj)
		return obj;
	var copy = obj.constructor();
	for ( var attr in obj) {
		if (obj.hasOwnProperty(attr))
			copy[attr] = obj[attr];
	}
	return copy;
};

var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource', 'mgcrea.ngStrap',
		'ng-criterias', 'ngHtmlCompile', 'ngRoute', 'ngAnimate', 'ngTable',
		'ui.router', 'angularFileUpload', 'ui.calendar',
		'http-auth-interceptor', 'timer', 'localytics.directives',
		'daterangepicker', 'pascalprecht.translate', 'angular-loading-bar',
		'ngQuickDate', 'xeditable', 'colorpicker.module', 'angular.filter',
		'truncate','ncy-angular-breadcrumb','ngCookies','tmh.dynamicLocale','ngFinder' ]);


App.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.parentSelector = '.main';
  }]);

  
  
App.run(function($rootScope,$q, $templateCache, UsersREST,$state,ENV,VERSION,USERINFO) {
	$rootScope.page = '';
	$rootScope.$state = $state;
	$rootScope.appContextPath=_contextPath;
	 $rootScope.ENV = ENV;
     $rootScope.VERSION = VERSION;
	// $templateCache.removeAll();
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		if (toState.data && toState.data.roles) {
			// User isn’t authenticated
//			$state.transitionTo("login");
			event.preventDefault();
		}
	});
	 $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
		    event.preventDefault();
		    $state.go('error404');
	});
	var loadUserInfo = function() {
		var deferred = $q.defer();
		UsersREST.get({
			service : 'user-info'
		}, function(data) {
			$rootScope.$broadcast('userInfo', data);
			deferred.resolve(data);
			$rootScope.userInfo=data;
			userInfo=data;
		});
	};
	loadUserInfo();
	$rootScope.$on('event:userInfo-Refresh', function() {
		loadUserInfo();
	});
});

App.run(function(editableOptions,editableThemes) {
	editableThemes.bs3.inputClass = 'input-sm';
	editableThemes.bs3.buttonsClass = 'btn-sm';
	editableOptions.theme = 'bs3';
	editableOptions.activate= 'select';
});
