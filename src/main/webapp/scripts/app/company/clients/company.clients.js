App.config(function ($stateProvider) {
	$stateProvider.state('company.clients', {
		url : "/clients",
		templateUrl : _contextPath+'scripts/templates/partials/panel-view.html',
		data: {
			ncyBreadcrumbLabel : 'Clients'
		  }
	}).state('company.clients.details', {
		url : "/details/:id",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-overview.html',
		controller : 'CompanyClientsOverviewController',
		resolve : {
			client :function(ClientsREST,$stateParams,USERINFO) {
				return ClientsREST.get(
						{companyId : USERINFO.company.id,id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{client.name}}'
		}
	})
	.state('company.clients.view', {
		url : "/view",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-view.html',
		abstract : true,
		controller : 'CompanyClientsViewController'
	})
	.state('company.clients.view.list', {
		url : "/list",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-list.html',
		controller : 'CompanyClientsListController',
		data : {
			ncyBreadcrumbLabel : 'List View'
		}
	}).state('company.clients.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-quickview.html',
		controller : 'CompanyClientsQuickViewController',
		data : {
			ncyBreadcrumbSkip: true
		}
	})
	.state('company.clients.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-overview.html',
		controller : 'CompanyClientsOverviewController',
		resolve : {
			client :function(ClientsREST,$stateParams,USERINFO) {
				return ClientsREST.get(
						{companyId : USERINFO.company.id,id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{client.name}}'
		}
	})
	.state('company.clients.view.quick.overview.infos', {
		url : "/infos",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-infos.html',
		controller : 'CompanyClientsOverviewController',
		data : {
			ncyBreadcrumbLabel : 'Infos'
		}
	})
	.state('company.clients.view.quick.overview.projects', {
		url : "/projects",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-projects.html',
		controller : 'CompanyClientsOverviewController',
		data : {
			ncyBreadcrumbLabel : 'Projects'
		}
	})
	.state('company.clients.view.quick.overview.contacts', {
		url : "/contacts",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-contacts.html',
		controller : 'CompanyClientsOverviewController',
		data : {
			ncyBreadcrumbLabel : 'Contacts'
		}
	})
	.state('company.clients.view.quick.overview.activities', {
		url : "/activities",
		templateUrl : _contextPath+'scripts/app/company/clients/company-clients-activities.html',
		controller : 'CompanyClientsOverviewController',
		data : {
			ncyBreadcrumbLabel : 'Activities'
		}
	})
});
