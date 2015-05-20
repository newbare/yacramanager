App.config(function ($stateProvider) {
	$stateProvider.state('company.clients', {
		url : "/clients",
		templateUrl : _contextPath+'views/app/templates/partials/panel-view.html',
		data: {
			ncyBreadcrumbLabel : 'Clients'
		  }
	}).state('company.clients.details', {
		url : "/details/:id",
		templateUrl : _contextPath+'views/app/company/clients/company-clients-overview.html',
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
		templateUrl : _contextPath+'views/app/company/clients/company-clients-view.html',
		abstract : true,
		controller : 'CompanyClientsViewController'
	})
	.state('company.clients.view.list', {
		url : "/list",
		templateUrl : _contextPath+'views/app/company/clients/company-clients-list.html',
		controller : 'CompanyClientsListController',
		data : {
			ncyBreadcrumbLabel : 'List View'
		}
	}).state('company.clients.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'views/app/company/clients/company-clients-quickview.html',
		controller : 'CompanyClientsQuickViewController',
		data : {
			ncyBreadcrumbSkip: true
		}
	})
	.state('company.clients.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'views/app/company/clients/company-clients-overview.html',
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
});
