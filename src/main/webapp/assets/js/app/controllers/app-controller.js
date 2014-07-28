App.config(function($datepickerProvider) {
	angular.extend($datepickerProvider.defaults, {
		dateFormat : 'dd/MM/yyyy',
		startWeek : 1,
		template : _contextPath+'/assets/others/datepicker/datepicker.tpl.html',
		autoclose : true,
		modelDateFormat : 'dd/MM/yyyy',
		todayHighlight : true
	});
});

App.config(function($tooltipProvider) {
  angular.extend($tooltipProvider.defaults, {
    animation: 'am-flip-x',
    trigger: 'hover',
    template: _contextPath+'/assets/others/tooltip/tooltip.tpl.html'
  });
})

App.controller('AppCtrl', [ '$scope', '$location', 'UsersREST','$rootScope',
		function($scope, $location, UsersREST,$rootScope) {
			$scope.navClass = function(page) {
				var currentRoute = $location.path().substring(1) || 'home';
				return page === currentRoute ? 'active' : '';
			};
			
			$scope.containerClass=function(){
				return "container-fluid";
			}

			$scope.containerNavClass=function(){
				return "container-fluid";
			}
		} ]);

App.config([ '$stateProvider', '$urlRouterProvider',
		function($stateProvider, $urlRouterProvider) {

			// Use $urlRouterProvider to configure any redirects (when) and
			// invalid urls (otherwise).
			$urlRouterProvider
			.when('/company', '/company/home')
//			.when('/company/employees', '/company/employees/list')
			.when('/admin', '/admin/home')
			

			// The `when` method says if the url is ever the 1st param, then
			// redirect to the 2nd param
			// Here we are just setting up some convenience urls.
			//.when('/c?id', '/contacts/:id').when('/user/:id', '/contacts/:id')

			// If the url is ever invalid, e.g. '/asdf', then redirect to '/'
			// aka the home state
			.otherwise('/');
			$stateProvider.state('home', {
				url : "/",
				templateUrl : _contextPath+'/views/app/home.html',
				controller : HomeController
			}).state('frais', {
				url : "/frais",
				templateUrl : _contextPath+'/views/app/frais.html',
				controller : FraisController
			}).state('cra', {
				url : "/cra",
				templateUrl : _contextPath+'/views/app/cra.html',
				controller : CraController
			}).state('absences', {
				url : "/absences",
				templateUrl : _contextPath+'/views/app/absences.html',
				controller : AbsencesController
			}).state('timesheet', {
				url : "/timesheet",
				templateUrl : _contextPath+'/views/app/timesheet.html',
				controller : TimeSheetController
			})
			.state('messages', {
				url : "/messages",
				templateUrl : _contextPath+'/views/app/messages.html',
				controller : MessagesController
			})
			.state('notifications', {
				url : "/notifications",
				templateUrl : _contextPath+'/views/app/notifications.html',
				controller : NotificationsController
			}).state('user-settings', {
				url : "/user-settings",
				templateUrl : _contextPath+'/views/app/user-settings.html',
				controller : UserSettingsController
			}).state('user-profile', {
				url : "/user-profile",
				templateUrl : _contextPath+'/views/app/user-profile.html',
				controller : UserProfileController
			}).state('company', {
				url : "/company",
				templateUrl : _contextPath+'/views/app/company.html',
				controller : CompanyController
			}).state('company.home', {
				url : "/home",
				templateUrl : _contextPath+'/views/app/company/company-home.html'
				//controller : EntrepriseController,
			}).state('company.employees', {
				url : "/employees",
				templateUrl : _contextPath+'/views/app/company/company-employees.html',
				controller : CompanyEmployeesController
			})
			.state('company.employees.list', {
				url : "/list",
				templateUrl : _contextPath+'/views/app/company/company-employees-list.html',
				controller : CompanyEmployeesController
			}).state('company.employees.overview', {
				url : "/:id/overview",
				templateUrl : _contextPath+'/views/app/company/company-employees-overview.html',
				controller : CompanyEmployeesOverviewController
			})
			.state('company.clients', {
				url : "/clients",
				templateUrl : _contextPath+'/views/app/company/company-clients.html',
				controller : CompanyClientsController
			}).state('company.projects', {
				url : "/projects",
				templateUrl : _contextPath+'/views/app/company/company-projects.html',
				controller : CompanyProjectsController,
			})
			.state('company.messages', {
				url : "/messages",
				templateUrl : _contextPath+'/views/app/company/company-messages.html'
				//controller : EntrepriseController,
			})
			.state('company.settings', {
				url : "/settings",
				templateUrl : _contextPath+'/views/app/company/company-settings.html'
				//controller : EntrepriseController,
			}).state('admin', {
				url : "/admin",
				templateUrl : _contextPath+'/views/app/admin.html',
				controller : AdminController
			}).state('admin.home', {
				url : "/home",
				templateUrl : _contextPath+'/views/app/admin/admin-home.html',
				controller : AdminHomeController
			}).state('admin.companies', {
				url : "/companies",
				templateUrl : _contextPath+'/views/app/admin/admin-companies.html',
				controller : AdminCompaniesController
			})
			.state('admin.messages', {
				url : "/messages",
				templateUrl : _contextPath+'/views/app/admin/admin-messages.html',
				//controller : AdminController
			}).state('admin.settings', {
				url : "/settings",
				templateUrl : _contextPath+'/views/app/admin/admin-settings.html',
				controller : AdminSettingsController
			})

		} ]);

function NotificationsController($scope,$rootScope) {
	$rootScope.page={"title":"Notification","description":"Stay aware..."}
}
