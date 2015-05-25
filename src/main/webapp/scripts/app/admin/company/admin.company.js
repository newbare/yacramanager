App.config(function ($stateProvider) {
	$stateProvider.state('admin.company', {
		url : "/company",
		templateUrl : _contextPath+'scripts/templates/partials/panel-view.html',
		controller : 'AdminCompaniesController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel : 'Company'
		  }
	}).state('admin.company.details', {
		url : "/details/:id",
		templateUrl : _contextPath+'scripts/app/admin/company/admin-company-overview.html',
		controller : 'AdminCompanyOverviewController',
		resolve : {
			roles: ['ROLE_ADMIN'],
			company :function(CompanyREST,$stateParams,$state,USERINFO) {
				return CompanyREST.get({
					companyId : USERINFO.company.id,
					id : $stateParams.id
				}).$promise;
			}
		}
	})
	.state('admin.company.view', {
		url : "/view",
		templateUrl : _contextPath+'scripts/app/admin/company/admin-company-view.html',
		controller : 'AdminCompanyViewController',
		data: {
			roles: ['ROLE_ADMIN'],
		    ncyBreadcrumbSkip: true 
		  }
	})
	.state('admin.company.view.list', {
		url : "/list",
		templateUrl : _contextPath+'scripts/app/admin/company/admin-company-list.html',
		controller : 'AdminCompanyListController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel : 'List view'
		  }
	}).state('admin.company.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'scripts/app/admin/company/admin-company-quickview.html',
		controller : 'AdminCompanyQuickViewController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel : 'Quick view'
		  }
		
	})
	.state('admin.company.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'scripts/app/admin/company/admin-company-overview.html',
		controller : 'AdminCompanyOverviewController',
		resolve : {
			company :function(CompanyREST,$stateParams,USERINFO) {
				return CompanyREST.get({
					companyId : USERINFO.company.id,
					id : $stateParams.id
				}).$promise;
			}
		},
		data: {
			roles: ['ROLE_ADMIN'],
			 ncyBreadcrumbLabel: '{{company.name}}'
		  }
	})
});