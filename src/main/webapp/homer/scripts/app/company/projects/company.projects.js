App.config(function ($stateProvider) {
	$stateProvider.state('company.projects', {
		url : "/projects",
		templateUrl : _contextPath+'scripts/templates/partials/panel-view.html',
		controller : 'CompanyProjectsController',
		data: {
			ncyBreadcrumbLabel : 'Projects'
		  }
	}).state('company.projects.details', {
		url : "/details/:id",
		templateUrl : _contextPath+'scripts/app/company/projects/company-projects-overview.html',
		controller : 'CompanyProjectsOverviewController',
		resolve : {
			project :function(ProjectsREST,$stateParams,USERINFO) {
				return ProjectsREST.get(
						{companyId : USERINFO.company.id,id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{project.name}}'
		}
	})
	.state('company.projects.view', {
		url : "/view",
		templateUrl : _contextPath+'scripts/app/company/projects/company-projects-view.html',
		controller : 'CompanyProjectsViewController',
		abstract : true
	})
	.state('company.projects.view.list', {
		url : "/list",
		templateUrl : _contextPath+'scripts/app/company/projects/company-projects-list.html',
		controller : 'CompanyProjectsListController',
		data : {
			ncyBreadcrumbLabel : 'List View'
		}
	}).state('company.projects.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'scripts/app/company/projects/company-projects-quickview.html',
		controller : 'CompanyProjectsQuickViewController',
		data : {
			ncyBreadcrumbSkip: true
		}
	})
	.state('company.projects.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'scripts/app/company/projects/company-projects-overview.html',
		controller : 'CompanyProjectsOverviewController',
		resolve : {
			project :function(ProjectsREST,$stateParams,USERINFO) {
				return ProjectsREST.get(
						{companyId : USERINFO.company.id,id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{project.name}}'
		}
	})
});