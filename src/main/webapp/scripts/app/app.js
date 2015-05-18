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
		'truncate','ncy-angular-breadcrumb','ngCookies','tmh.dynamicLocale','ngFinder','ngCacheBuster','LocalStorageModule']);


App.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.parentSelector = '.main';
  }]);

  
  
App.run(function($rootScope,$q, $templateCache, UsersREST,$state,ENV,VERSION,USERINFO) {
	$rootScope.page = '';
	$rootScope.$state = $state;
	$rootScope.appContextPath=_contextPath;
	$rootScope.ENV = ENV;
    $rootScope.VERSION = VERSION;
     var hasAnyOneOfRole=function(userRoles,definedRoles){
			result=false;
			definedRoles.forEach(function(entry) {
			    if(hasTheRole(userRoles,entry)) {
			    	result=true;
			    }
			});
			return result;
		};
		var hasTheRole=function(roles,role){
				result=false;
				roles.forEach(function(entry) {
				    if(entry.role==role) {
				    	result=true;
				    }
				});
				return result;
			};
	// $templateCache.removeAll();
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams,
			fromState, fromParams) {
		if (angular.isDefined(toState.data.roles) && ! hasAnyOneOfRole(USERINFO.roles,toState.data.roles)) {
			event.preventDefault();
			$state.go('error403',{pageTitle: 'Access denied'});
		}
	});
	 $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
		    event.preventDefault();
		    $state.go('error404',{pageTitle: 'Page not Found'});
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

App.config(function($urlRouterProvider) {
	$urlRouterProvider
	.when('','/home')
	.when('/absence', '/absence/list')
	.when('/frais', '/frais/list')
	.when('/company', '/company/home')
	.when('/admin/company', '/admin/company/view/quickview')
	.when('/company/employees', '/company/employees/view/quickview')
	.when('/company/employees/view', '/company/employees/view/quickview')
	.when('/company/employees/view/quickview/{id}', '/company/employees/view/quickview/{id}/basic')
	.when('/company/clients', '/company/clients/view/quickview')
	.when('/company/clients/view', '/company/clients/view/quickview')
	.when('/company/projects', '/company/projects/view/quickview')
	.when('/company/projects/view', '/company/projects/view/quickview')
	.when('/admin', '/admin/home')
	

	// The `when` method says if the url is ever the 1st param, then
	// redirect to the 2nd param
	// Here we are just setting up some convenience urls.
	//.when('/c?id', '/contacts/:id').when('/user/:id', '/contacts/:id')

	// If the url is ever invalid, e.g. '/asdf', then redirect to '/'
	// aka the home state
	.otherwise('/error404');
});


App.config(function ($stateProvider,$translateProvider,$httpProvider,tmhDynamicLocaleProvider,httpRequestInterceptorCacheBusterProvider,localStorageServiceProvider) {
	
	
	//enable CSRF
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
	
	//Cache everything except rest api requests
    httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);
	
	$translateProvider.preferredLanguage('en');
	
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
	
	localStorageServiceProvider.setPrefix('yacra.config');
});

//define custom handler
App.factory('translationMissingErrorHandlerFactory', function () {
  // has to return a function which gets a tranlation id
  return function (translationID) {
    console.log("Missing translation "+translationID);
  };
});


var serializeData=function(data) {
	// If this is not an object, defer to native stringification.
	if (!angular.isObject(data)) {
		return ((data === null) ? "" : data.toString());
	}
	var buffer = [];
	// Serialize each key in the object.
	for ( var name in data) {
		if (!data.hasOwnProperty(name)) {
			continue;
		}
		var value = data[name];
		buffer.push(encodeURIComponent(name) + "=" + encodeURIComponent((value === null) ? "" : value));
	}
	// Serialize the buffer and clean it up for transportation.
	var source = buffer.join("&").replace(/%20/g, "+");
	return (source);
};

App.config(function($datepickerProvider) {
	angular.extend($datepickerProvider.defaults, {
		dateFormat : 'dd/MM/yyyy',
		startWeek : 1,
		template : _contextPath+'templates/datepicker/datepicker.tpl.html',
		autoclose : true,
		modelDateFormat : 'dd/MM/yyyy',
		todayHighlight : true
	});
});

App.config(function($breadcrumbProvider) {
    $breadcrumbProvider.setOptions({
        prefixStateName: 'home',
        template: 'bootstrap3'
      });
    });

App.config(function(ngQuickDateDefaultsProvider) {
	  // Configure with icons from font-awesome
	  return ngQuickDateDefaultsProvider.set({
	    closeButtonHtml: "<i class='fa fa-times'></i>",
	    buttonIconHtml: "<i class='fa fa-clock-o'></i>",
	    nextLinkHtml: "<i class='fa fa-chevron-right'></i>",
	    prevLinkHtml: "<i class='fa fa-chevron-left'></i>"
	    // Take advantage of Sugar.js date parsing
//	    parseDateFunction: function(str) {
//	      d = Date.create(str);
//	      return d.isValid() ? d : null;
//	    }
	  });
	});

App.config(function($timepickerProvider) {
	angular.extend($timepickerProvider.defaults, {
		template : _contextPath+'templates/timepicker/timepicker.tpl.html'
	});
});

App.config(function($tooltipProvider) {
  angular.extend($tooltipProvider.defaults, {
    animation: 'am-flip-x',
    trigger: 'hover',
    template: _contextPath+'templates/tooltip/tooltip.tpl.html'
  });
});

App.controller('AppCtrl', [ '$scope', '$location', 'UsersREST','$rootScope','$translate','$locale','LanguageService','$state','ENV','VERSION','USERINFO','localStorageService',
		function($scope, $location, UsersREST,$rootScope,$translate,$locale,LanguageService,$state,ENV,VERSION,USERINFO,localStorageService) {
			$scope.ENV=ENV;
			$scope.VERSION=VERSION;
			$scope.eventsToWait=['userInfo'];
			$rootScope.userLocalSettings={}
			if(localStorageService.get('userLocalSettings.container')!=undefined){
				$rootScope.userLocalSettings.container=	localStorageService.get('userLocalSettings.container');
				
			}else {
				localStorageService.set('userLocalSettings.container','container');
				$rootScope.userLocalSettings.container='container';
			}
			
			$scope.navClass = function(page) {
				var currentRoute = $location.path().substring(1) || 'home';
				return currentRoute.indexOf(page)===0 ? 'active' :'';
			};
			
			$scope.containerClass=function(){
				return "container";
			};

			$scope.containerNavClass=function(){
				return "container";
			};
			$scope.isValidated=function(data){
				return 'APPROVED'==data.validationStatus;
			};
			$scope.isWaiting=function(data){
				return 'WAIT_FOR_APPROVEMENT'==data.validationStatus;
			};
			$scope.isRejected=function(data){
				return 'REJECTED'==data.validationStatus;
			};
		} ]);

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



App.controller('WorkLogCtrl',['$scope','$rootScope','$http','WorkLogREST','alertService','USERINFO',function($scope,$rootScope,$http,WorkLogREST,alertService,USERINFO){
	 $scope.timerRunning = false;
	 $scope.open=false;
	 $scope.project=undefined;
	 $scope.task=undefined;
	 var worklogDateFormat="YYYY-MM-DDTHH:mm:ss.SSS";
	 var start;
     $scope.startTimer = function (){
         $scope.$broadcast('timer-start');
         $scope.timerRunning = true;
         $scope.open=false;
         start=moment();
         $scope.updateStartable();
//         $(".timer-widget-content.dropdown-menu").dropdown('toggle');
     };

     $scope.stopTimer = function (){
    	 $scope.timerRunning = false;
    	 $scope.$broadcast('timer-stop');
    	 $scope.updateStartable();
     };

     $scope.$on('timer-stopped', function (event, data){
    	 $scope.updateStartable();
    	 console.log('Timer Stopped - data = ', data);
    	 if(data.minutes>=1){
    		 $scope.worklog.title="";
        	 $scope.worklog.type="TIME";
        	 $scope.worklog.start=start.format(worklogDateFormat);
        	 $scope.worklog.end=start.clone().add('minutes',data.minutes).format(worklogDateFormat);
        	 //duration in minutes
        	 $scope.worklog.duration=0;//Math.round((data.millis/1000)/60);
        	 $scope.worklog.taskId= $scope.task.id;
        	 $scope.worklog.taskName=$scope.task.name;
        	 $scope.worklog.description=$scope.description;
        	 $scope.worklog.employeId=USERINFO.id;
        	 WorkLogREST.save($scope.worklog).$promise.then(function(result) {
        		 $scope.resetWorkLog();
        		 alertService.show('success','Confirmation', 'Data saved');
    		});
    	 }else {
    		 $scope.resetWorkLog();
    		 alertService.show('danger','Error', 'Can not save worklog less than 1 minute');
		}
    	 
     });
     
     var fetchProjects = function(queryParams) {
 		return $http.get(
 				_contextPath + "app/api/" + USERINFO.company.id + "/project/employe/"+ USERINFO.id, {
 					params : {}
 				}).then(function(response) {
 					$scope.projects=response.data.result;
 				});
 	}; 
 	
 	fetchProjects();
 	
 	$scope.selectProject=function(project){
 		$scope.project=project;
 		fetchTasks();
 		$scope.updateStartable();
 	};
 	$scope.selectTask=function(task){
 		$scope.task=task;
 		$scope.updateStartable();
 	};
 	
 	var fetchTasks = function(queryParams) {
 		return $http.get(
 				_contextPath + "app/api/" + USERINFO.company.id + "/task/"+$scope.project.id+"/"+ USERINFO.id, {
 					params : {}
 				}).then(function(response) {
 					$scope.tasks=response.data.result;
 				});
 	};
 	
 	$scope.updateStartable=function(){
 		$scope.startable= $scope.project!==undefined && $scope.task!==undefined && !$scope.timerRunning;
 	};
 	
 	$scope.resetWorkLog=function(){
 		$scope.worklog={};
 		$scope.project=undefined;
		$scope.task=undefined;
		$scope.description=undefined;
 	};
 	$scope.resetWorkLog();
}]);



App.controller('LoginCtrl', [ '$scope','$http','authService',function($scope,$http, authService) {
	$scope.submit = function() {
	      $http({
	    	  method: 'POST',
	    	  url: _contextPath+'auth/authentication',
	    	  transformRequest: function( data, getHeaders){
	    		  var headers = getHeaders();
	    		  headers["Content-Type"] = "application/x-www-form-urlencoded; charset=utf-8";
	    		  headers["AJAX-LOGIN"] = "true";
                  return( serializeData( data ) );
	    	  },
	    	  data: {
	    		  email: $scope.username,
	    		  password: $scope.password
	    	  }
	      })
	    	 .success(function() {
	    		 authService.loginConfirmed();
	      })
	      	.error(function(data, status){
	      		$scope.errorMessage=data;
	      	})
	      ;
	    };
}]);


