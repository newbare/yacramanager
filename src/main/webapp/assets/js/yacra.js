var yaCRAApp = {};

var App = angular.module('yaCRAApp', [ 'ngResource',
		'ui.router','pascalprecht.translate','ngCookies','tmh.dynamicLocale']);
App.controller('LanguageController', function ($scope, $translate, LanguageService) {
    $scope.changeLanguage = function (languageKey) {
        $translate.use(languageKey);

        LanguageService.getBy(languageKey).then(function(languages) {
            $scope.languages = languages;
        });
        $scope.currentLanguage=languageKey;
    };

    LanguageService.getBy().then(function (languages) {
        $scope.languages = languages;
        $scope.currentLanguage=$translate.use();
    });
    
});

App.run(function($rootScope) {
	$rootScope.appContextPath=_contextPath;
});

App
		.config([
				'$stateProvider',
				'$urlRouterProvider',
				'$locationProvider',
				'$translateProvider',
				'tmhDynamicLocaleProvider',
				function($stateProvider, $urlRouterProvider, $locationProvider,
						$translateProvider, tmhDynamicLocaleProvider) {
					$translateProvider.determinePreferredLanguage();
					$translateProvider.useStaticFilesLoader({
						prefix : _contextPath + '/assets/i18n/',
						suffix : '.json'
					});

					$translateProvider.useCookieStorage();

					tmhDynamicLocaleProvider
							.localeLocationPattern(_contextPath	+ '/assets/bower_components/angular-i18n/angular-locale_{{locale}}.js');
					tmhDynamicLocaleProvider
							.useCookieStorage('NG_TRANSLATE_LANG_KEY');

				} ]);


App.factory('LanguageService', function ($http, $translate, LANGUAGES) {
    return {
        getBy: function(language) {
            if (language === undefined) {
                language = $translate.storage().get('NG_TRANSLATE_LANG_KEY') || window.navigator.language;
            }

            var promise =  $http.get( _contextPath+'/assets/i18n/' + language + '.json').then(function(response) {
                return LANGUAGES;
            });
            return promise;
        }
    };
});