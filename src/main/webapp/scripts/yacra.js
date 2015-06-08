var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource',
		'pascalprecht.translate','ngCookies','tmh.dynamicLocale','ncy-angular-breadcrumb']);
App.controller('LanguageController', function ($scope, $translate, LanguageService) {
LanguageService.getCurrent().then(function(langage){$scope.currentLanguage=langage}); 
	
	$scope.changeLanguage = function (languageKey) {
         $translate.use(languageKey);
         $scope.currentLanguage=languageKey;
     };

     LanguageService.getAll().then(function (languages) {
         $scope.languages = languages;
     });
    
});

App.run(function($rootScope) {
	$rootScope.appContextPath=_contextPath;
});

//App
//		.config([
//				'$stateProvider',
//				'$urlRouterProvider',
//				'$locationProvider',
//				'$translateProvider',
//				'tmhDynamicLocaleProvider',
//				function($stateProvider, $urlRouterProvider, $locationProvider,
//						$translateProvider, tmhDynamicLocaleProvider) {
//					
//					$translateProvider.determinePreferredLanguage();
//					$translateProvider.useStaticFilesLoader({
//						prefix : _contextPath + 'i18n/',
//						suffix : '.json'
//					});
//
//					$translateProvider.useCookieStorage();
//
//					tmhDynamicLocaleProvider
//							.localeLocationPattern(_contextPath	+ 'bower_components/angular-i18n/angular-locale_{{locale}}.js');
//					tmhDynamicLocaleProvider
//							.useCookieStorage('NG_TRANSLATE_LANG_KEY');
//
//				} ]);


App.factory('LanguageService', function ($http, $translate, LANGUAGES) {
    return {
        getBy: function(language) {
            if (language === undefined) {
                language = $translate.storage().get('NG_TRANSLATE_LANG_KEY') || window.navigator.language;
            }

            var promise =  $http.get( _contextPath+'i18n/' + language + '.json').then(function(response) {
                return LANGUAGES;
            });
            return promise;
        }
    };
});

App.factory('LanguageService', function ($q,$translate, LANGUAGES) {
    return {
        getCurrent: function () {
            var deferred = $q.defer();
            var language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

            if (angular.isUndefined(language)) {
                language = 'en';
            }

            deferred.resolve(language);
            return deferred.promise;
        },
        getAll: function () {
            var deferred = $q.defer();
            deferred.resolve(LANGUAGES);
            return deferred.promise;
        }
    };
});

App.controller('AppCtrl', [
		'$scope',
		'$location',
		'$rootScope',
		'$translate',
		'$locale',
		'$state',
		'ENV',
		'VERSION',
		function($scope, $location, $rootScope, $translate, $locale, $state,
				ENV, VERSION) {
			$scope.ENV = ENV;
			$scope.VERSION = VERSION;
			if((typeof(_userName)!='undefined')){
				$scope.userName=_userName;
			}
		} ]);


var stateConfig =[ '$stateProvider','$locationProvider','$translateProvider','tmhDynamicLocaleProvider',
		function($stateProvider,$locationProvider,$translateProvider,tmhDynamicLocaleProvider) {
			
			$translateProvider.preferredLanguage('en');
//			$translateProvider.determinePreferredLanguage();
			
			$translateProvider.useStaticFilesLoader({
			      prefix: _contextPath+'i18n/',
			      suffix: '.json'
			});

			// tell angular-translate to use your custom handler
			$translateProvider.useMissingTranslationHandler('translationMissingErrorHandlerFactory');
			$translateProvider.useCookieStorage();

			tmhDynamicLocaleProvider
					.localeLocationPattern(_contextPath+'bower_components/angular-i18n/angular-locale_{{locale}}.js');
			tmhDynamicLocaleProvider
					.useCookieStorage('NG_TRANSLATE_LANG_KEY');

		} ];

//define custom handler
App.factory('translationMissingErrorHandlerFactory', function () {
  // has to return a function which gets a tranlation id
  return function (translationID) {
    console.log("Missing translation "+translationID);
  };
});

App.config(stateConfig);

App.config(function($breadcrumbProvider) {
    $breadcrumbProvider.setOptions({
        prefixStateName: 'home',
        template: 'bootstrap3'
      });
    });

App.directive('updateTitle', function($rootScope) {
	  return {
	    link: function(scope, element) {

	      var listener = function(event, toState, toParams, fromState, fromParams) {
	        var title = 'YACRA Manager';
	        if (toState.data && toState.data.pageTitle) title = title+ ' | '+toState.data.pageTitle;
	        element.text(title);
	      };

	      $rootScope.$on('$stateChangeStart', listener);
	    }
	  };
	});
App.directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs, controller) {
            var language = attrs.activeMenu;

            scope.$watch(function() {
                return $translate.use();
            }, function(selectedLanguage) {
                if (language === selectedLanguage) {
                    tmhDynamicLocale.set(language);
                    element.addClass('active');
                } else {
                    element.removeClass('active');
                }
            });
        }
    };
});
function HomeController($scope, $rootScope,$http) {
	
};

function AboutController($scope, $rootScope,$http) {
	
};

function PricingController($scope, $rootScope,$http) {
	
};
function QuickTourController($scope, $rootScope,$http) {
	
};