App.config(function ($stateProvider) {
        
	$stateProvider.state('company.employees', {
		url : "/employees",
		templateUrl : _contextPath+'scripts/templates/partials/panel-view.html',
		controller : 'CompanyEmployeesController',
		data: {
			pageTitle: 'Employe view',
			ncyBreadcrumbLabel : 'Employees'
		  }
	}).state('company.employees.details', {
		url : "/details/:id",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-overview.html',
		controller : 'CompanyEmployeesOverviewController',
		resolve : {
			employe :function(EmployeesREST,$stateParams) {
				return EmployeesREST.get({id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{employe.firstName}} {{employe.lastName}}',
			 ncyBreadcrumbParent: 'company.employees.view'
		}
	})
	.state('company.employees.view', {
		url : "/view",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-view.html',
		abstract : true,
		controller : 'CompanyEmployeesViewController'
	})
	.state('company.employees.view.list', {
		url : "/list",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-list.html',
		controller : 'CompanyEmployeesListController',
		data : {
			ncyBreadcrumbLabel : 'List View'
		}
	}).state('company.employees.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-quickview.html',
		controller : 'CompanyEmployeesQuickViewController',
		data : {
			ncyBreadcrumbSkip: true
		}
	})
	.state('company.employees.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-overview.html',
		controller : 'CompanyEmployeesOverviewController',
		resolve : {
			employe :function(EmployeesREST,$stateParams) {
				return EmployeesREST.get({id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{employe.firstName}} {{employe.lastName}}'
		}
	})
	.state('company.employees.view.quick.overview.basic', {
		url : "/basic",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-basics-infos.html',
		controller : 'CompanyEmployeesBasicInfosController',
		data : {
			ncyBreadcrumbLabel : 'Basic infos'
		}
	})
	.state('company.employees.view.quick.overview.administration', {
		url : "/administration",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-administration.html',
		controller : 'CompanyEmployeesAdministrationController',
		data : {
			ncyBreadcrumbLabel : 'Administration'
		}
	})
	.state('company.employees.view.quick.overview.salary', {
		url : "/salary",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-salary.html',
		controller : 'CompanyEmployeesSalaryController',
		data : {
			ncyBreadcrumbLabel : 'Salary'
		}
	})
	.state('company.employees.view.quick.overview.activities', {
		url : "/activities",
		templateUrl : _contextPath+'scripts/app/company/employees/company-employees-activities.html',
		controller : 'CompanyEmployeesActivitiesController',
		data : {
			ncyBreadcrumbLabel : 'Activities'
		}
	})
    });