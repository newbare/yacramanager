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

App.controller('AppCtrl', [ '$scope', '$location', 'UsersREST','$rootScope','$translate','$locale','LanguageService','$state','ENV','VERSION',
		function($scope, $location, UsersREST,$rootScope,$translate,$locale,LanguageService,$state,ENV,VERSION) {
			$scope.ENV=ENV;
			$scope.VERSION=VERSION;
			$scope.eventsToWait=['userInfo'];
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



App.controller('WorkLogCtrl',['$scope','$http','WorkLogREST','alertService',function($scope,$http,WorkLogREST,alertService){
	 $scope.timerRunning = false;
	 $scope.open=false;
	 $scope.project=undefined;
	 $scope.task=undefined;
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
        	 $scope.worklog.start=start;
        	 $scope.worklog.end=start.clone().add('minutes',data.minutes );
        	 //duration in minutes
        	 $scope.worklog.duration=0;//Math.round((data.millis/1000)/60);
        	 $scope.worklog.taskId= $scope.task.id;
        	 $scope.worklog.taskName=$scope.task.name;
        	 $scope.worklog.description=$scope.description;
        	 $scope.worklog.employeId=_userId;
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
 				_contextPath + "app/api/" + _userCompanyId + "/project/employe/"+ _userId, {
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
 				_contextPath + "app/api/" + _userCompanyId + "/task/"+$scope.project.id+"/"+ _userId, {
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

App.controller('FilesController',['$scope',function($scope){
	$scope.fileConnectorURL=_contextPath+'app/file-connector';
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
	    		  username: $scope.username,
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

var whenConfig=[ '$urlRouterProvider',	function($urlRouterProvider) {
	$urlRouterProvider
	.when('','/home')
	.when('/company', '/company/home')
	.when('/admin/company', '/admin/company/view/quickview')
	.when('/company/employees', '/company/employees/view/quickview')
	.when('/company/employees/view', '/company/employees/view/quickview')
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
}];


var stateConfig =[ '$stateProvider','$locationProvider','$translateProvider','tmhDynamicLocaleProvider',
		function($stateProvider,$locationProvider,$translateProvider,tmhDynamicLocaleProvider) {

			$stateProvider
			.state('error404', {
				url : "/error404",
				templateUrl : _contextPath+'views/app/components/templates/error-404.tpl.html',
				controller : HomeController,
				data: {
			        pageTitle: 'Error 404'
			      }
			})
			.state('home', {
				url : "/home",
				templateUrl : _contextPath+'views/app/components/home.html',
				controller : HomeController,
				data: {
			        pageTitle: 'Home',
			        ncyBreadcrumbLabel: 'Home'
			      }
			}).state('tasks', {
				url : "/tasks",
				templateUrl : _contextPath+'views/app/components/tasks.html',
				controller : TasksController,
				data: {
			        pageTitle: 'Tasks',
			        ncyBreadcrumbLabel: 'Tasks'
			      }
			})
			.state('frais', {
				url : "/frais",
				templateUrl : _contextPath+'views/app/components/frais.html',
				controller : FraisController,
				data: {
			        pageTitle: 'Frais',
			        ncyBreadcrumbLabel: 'Frais'
			      }
			}).state('activity-report', {
				url : "/activity-report",
				templateUrl : _contextPath+'views/app/components/activity-report.html',
				controller : ActivityReportController,
				data: {
			        pageTitle: 'Activity report',
			        ncyBreadcrumbLabel: 'Activity report'
			      }
			}).state('absences', {
				url : "/absences",
				templateUrl : _contextPath+'views/app/components/absences.html',
				controller : AbsencesController,
				data: {
			        pageTitle: 'Absences',
			        ncyBreadcrumbLabel: 'Absences'
			      }
			}).state('timesheet', {
				url : "/timesheet",
				templateUrl : _contextPath+'views/app/components/timesheet.html',
				controller : TimeSheetController,
				data: {
			        pageTitle: 'Timesheet',
			        ncyBreadcrumbLabel: 'Timesheet'
			      }
			}).state('files', {
				url : "/files",
				templateUrl : _contextPath+'views/app/components/files.html',
				controller : 'FilesController',
				data: {
			        pageTitle: 'Files',
			        ncyBreadcrumbLabel: 'Files'
			      }
			})
			
			.state('messages', {
				url : "/messages",
				templateUrl : _contextPath+'views/app/components/messages.html',
				controller : MessagesController,
				data: {
			        pageTitle: 'Messages',
			        ncyBreadcrumbLabel: 'Messages'
			      }
			})
			.state('notifications', {
				url : "/notifications",
				templateUrl : _contextPath+'views/app/components/notifications.html',
				controller : NotificationsController,
				data: {
			        pageTitle: 'Notifications'
			      }
			}).state('user-settings', {
				url : "/user-settings",
				templateUrl : _contextPath+'views/app/components/user-settings.html',
				controller : UserSettingsController
			}).state('user-profile', {
				url : "/user-profile",
				templateUrl : _contextPath+'views/app/components/user-profile.html',
				controller : UserProfileController,
				data: {
			        ncyBreadcrumbLabel: 'User profile'
			      }
			}).state('api-docs', {
				url : "/api-docs",
				templateUrl : _contextPath+'views/app/components/api-docs.html',
				controller : ApiDocsController,
				data: {
			        ncyBreadcrumbLabel: 'Yacra API'
			      }
			})
			.state('company', {
				url : "/company",
				templateUrl : _contextPath+'views/app/components/company.html',
				controller : CompanyController,
				data: {
			        pageTitle: 'Company management',
			        ncyBreadcrumbLabel: 'Company'
			      }
			}).state('company.home', {
				url : "/home",
				templateUrl : _contextPath+'views/app/components/company/company-home.html',
				data: {
				    ncyBreadcrumbSkip: true 
				  }
				//controller : EntrepriseController,
			}).state('company.employees', {
				url : "/employees",
				templateUrl : _contextPath+'views/app/components/templates/partials/panel-view.html',
				controller : CompanyEmployeesController,
				data: {
					pageTitle: 'Employe view',
					ncyBreadcrumbLabel : 'Employees'
				  }
			}).state('company.employees.details', {
				url : "/details/:id",
				templateUrl : _contextPath+'views/app/components/company/employees/company-employees-overview.html',
				controller : CompanyEmployeesOverviewController,
				resolve : {
					employe :function(EmployeesREST,$stateParams) {
						return EmployeesREST.get({id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{employe.firstName}} {{employe.lastName}}',
					 ncyBreadcrumbParent: 'company.employees.view'
				}
			})
			.state('company.employees.view', {
				url : "/view",
				templateUrl : _contextPath+'views/app/components/company/employees/company-employees-view.html',
				controller : CompanyEmployeesViewController,
				data: {ncyBreadcrumbSkip: true}
			})
			.state('company.employees.view.list', {
				url : "/list",
				templateUrl : _contextPath+'views/app/components/company/employees/company-employees-list.html',
				controller : CompanyEmployeesListController,
				data : {
					ncyBreadcrumbLabel : 'List View'
				}
			}).state('company.employees.view.quick', {
				url : "/quickview",
				templateUrl : _contextPath+'views/app/components/company/employees/company-employees-quickview.html',
				controller : CompanyEmployeesQuickViewController,
				data : {
					ncyBreadcrumbLabel : 'QuickView'
				}
			})
			.state('company.employees.view.quick.overview', {
				url : "/:id",
				templateUrl : _contextPath+'views/app/components/company/employees/company-employees-overview.html',
				controller : CompanyEmployeesOverviewController,
				resolve : {
					employe :function(EmployeesREST,$stateParams) {
						return EmployeesREST.get({id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{employe.firstName}} {{employe.lastName}}'
				}
			})
			.state('company.clients', {
				url : "/clients",
				templateUrl : _contextPath+'views/app/components/templates/partials/panel-view.html',
				//controller : CompanyEmployeesController
				data: {
					ncyBreadcrumbLabel : 'Clients'
				  }
			}).state('company.clients.details', {
				url : "/details/:id",
				templateUrl : _contextPath+'views/app/components/company/clients/company-clients-overview.html',
				controller : CompanyClientsOverviewController,
				resolve : {
					client :function(ClientsREST,$stateParams) {
						return ClientsREST.get(
								{companyId : _userCompanyId,id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{client.name}}'
				}
			})
			.state('company.clients.view', {
				url : "/view",
				templateUrl : _contextPath+'views/app/components/company/clients/company-clients-view.html',
				controller : CompanyClientsViewController,
				data: {ncyBreadcrumbSkip: true}
			})
			.state('company.clients.view.list', {
				url : "/list",
				templateUrl : _contextPath+'views/app/components/company/clients/company-clients-list.html',
				controller : CompanyClientsListController,
				data : {
					ncyBreadcrumbLabel : 'List View'
				}
			}).state('company.clients.view.quick', {
				url : "/quickview",
				templateUrl : _contextPath+'views/app/components/company/clients/company-clients-quickview.html',
				controller : CompanyClientsQuickViewController,
				data : {
					ncyBreadcrumbLabel : 'QuickView'
				}
			})
			.state('company.clients.view.quick.overview', {
				url : "/:id",
				templateUrl : _contextPath+'views/app/components/company/clients/company-clients-overview.html',
				controller : CompanyClientsOverviewController,
				resolve : {
					client :function(ClientsREST,$stateParams) {
						return ClientsREST.get(
								{companyId : _userCompanyId,id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{client.name}}'
				}
			})
			.state('company.projects', {
				url : "/projects",
				templateUrl : _contextPath+'views/app/components/templates/partials/panel-view.html',
				controller : CompanyProjectsController,
				data: {
					ncyBreadcrumbLabel : 'Projects'
				  }
			}).state('company.projects.details', {
				url : "/details/:id",
				templateUrl : _contextPath+'views/app/components/company/projects/company-projects-overview.html',
				controller : CompanyProjectsOverviewController,
				resolve : {
					project :function(ProjectsREST,$stateParams) {
						return ProjectsREST.get(
								{companyId : _userCompanyId,id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{project.name}}'
				}
			})
			.state('company.projects.view', {
				url : "/view",
				templateUrl : _contextPath+'views/app/components/company/projects/company-projects-view.html',
				controller : CompanyProjectsViewController,
				data: {ncyBreadcrumbSkip: true}
			})
			.state('company.projects.view.list', {
				url : "/list",
				templateUrl : _contextPath+'views/app/components/company/projects/company-projects-list.html',
				controller : CompanyProjectsListController,
				data : {
					ncyBreadcrumbLabel : 'List View'
				}
			}).state('company.projects.view.quick', {
				url : "/quickview",
				templateUrl : _contextPath+'views/app/components/company/projects/company-projects-quickview.html',
				controller : CompanyProjectsQuickViewController,
				data : {
					ncyBreadcrumbLabel : 'QuickView'
				}
			})
			.state('company.projects.view.quick.overview', {
				url : "/:id",
				templateUrl : _contextPath+'views/app/components/company/projects/company-projects-overview.html',
				controller : CompanyProjectsOverviewController,
				resolve : {
					project :function(ProjectsREST,$stateParams) {
						return ProjectsREST.get(
								{companyId : _userCompanyId,id:$stateParams.id});
					}
				},
				data : {
					ncyBreadcrumbLabel : '{{project.name}}'
				}
			})
			.state('company.organigram', {
				url : "/organigram",
				templateUrl : _contextPath+'views/app/components/company/company-organigram.html'
				//controller : EntrepriseController,
			})
			.state('company.messages', {
				url : "/messages",
				templateUrl : _contextPath+'views/app/components/company/company-messages.html'
				//controller : EntrepriseController,
			})
			.state('company.settings', {
				url : "/settings",
				templateUrl : _contextPath+'views/app/components/company/company-settings.html',
				controller : CompanySettingsController,
			}).state('admin', {
				url : "/admin",
				templateUrl : _contextPath+'views/app/components/admin.html',
				controller : AdminController,
				abstract : true,
				data: {
					ncyBreadcrumbLabel : 'Admin'
				  }
			}).state('admin.home', {
				url : "/home",
				templateUrl : _contextPath+'views/app/components/admin/admin-home.html',
				controller : AdminHomeController,
				data: {
					ncyBreadcrumbSkip: true
				  }
			})
			.state('admin.company', {
				url : "/company",
				templateUrl : _contextPath+'views/app/components/templates/partials/panel-view.html',
				controller : AdminCompaniesController,
				data: {
					ncyBreadcrumbLabel : 'Company'
				  }
			}).state('admin.company.details', {
				url : "/details/:id",
				templateUrl : _contextPath+'views/app/components/admin/company/admin-company-overview.html',
				controller : AdminCompanyOverviewController,
				resolve : {
					company :function(CompanyREST,$stateParams,$state) {
						return CompanyREST.get({
							companyId : _userCompanyId,
							id : $stateParams.id
						},function(){},function(error){
							 //404 company not found
						    if(response.status === 404) {
						    	$state.go('error404');
						    }
						});
					}
				}
			})
			.state('admin.company.view', {
				url : "/view",
				templateUrl : _contextPath+'views/app/components/admin/company/admin-company-view.html',
				controller : AdminCompanyViewController,
				data: {
				    ncyBreadcrumbSkip: true 
				  }
			})
			.state('admin.company.view.list', {
				url : "/list",
				templateUrl : _contextPath+'views/app/components/admin/company/admin-company-list.html',
				controller : AdminCompanyListController,
				data: {
					ncyBreadcrumbLabel : 'List view'
				  }
			}).state('admin.company.view.quick', {
				url : "/quickview",
				templateUrl : _contextPath+'views/app/components/admin/company/admin-company-quickview.html',
				controller : AdminCompanyQuickViewController,
				data: {
					ncyBreadcrumbLabel : 'Quick view'
				  }
				
			})
			.state('admin.company.view.quick.overview', {
				url : "/:id",
				templateUrl : _contextPath+'views/app/components/admin/company/admin-company-overview.html',
				controller : AdminCompanyOverviewController,
				resolve : {
					company :function(CompanyREST,$stateParams) {
						return CompanyREST.get({
							companyId : _userCompanyId,
							id : $stateParams.id
						});
					}
				},
				data: {
					 ncyBreadcrumbLabel: '{{company.name}}'
				  }
			})
			.state('admin.logs', {
				url : "/logs",
				templateUrl : _contextPath+'views/app/components/admin/admin-logs.html',
				controller : LogsController,
                 data: {
 					ncyBreadcrumbLabel: 'Logs'
 				  }
			})
			.state('admin.metrics', {
				url : "/metrics",
				templateUrl : _contextPath+'views/app/components/admin/admin-metrics.html',
				controller : MetricsController,
				data: {
					ncyBreadcrumbLabel: 'Metrics'
				  }
			})
			.state('admin.messages', {
				url : "/messages",
				templateUrl : _contextPath+'views/app/components/admin/admin-messages.html',
				//controller : AdminController
				data: {
					ncyBreadcrumbLabel: 'Messages'
				  }
			}).state('admin.settings', {
				url : "/settings",
				templateUrl : _contextPath+'views/app/components/admin/admin-settings.html',
				controller : AdminSettingsController
			});
			
//			$translateProvider.preferredLanguage('en');
			$translateProvider.determinePreferredLanguage();
			
			$translateProvider.useStaticFilesLoader({
			      prefix: _contextPath+'i18n/',
			      suffix: '.json'
			});

			$translateProvider.useCookieStorage();

			tmhDynamicLocaleProvider
					.localeLocationPattern(_contextPath+'bower_components/angular-i18n/angular-locale_{{locale}}.js');
			tmhDynamicLocaleProvider
					.useCookieStorage('NG_TRANSLATE_LANG_KEY');

		} ];

App.config(whenConfig).config(stateConfig)

function NotificationsController($scope,$rootScope) {
	$rootScope.page={"title":"Notification","description":"Stay aware..."};
}
