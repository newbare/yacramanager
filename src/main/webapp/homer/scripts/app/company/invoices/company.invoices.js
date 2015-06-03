App.config(function ($stateProvider) {
	$stateProvider.state('company.invoices', {
		url : "/invoices",
		templateUrl : _contextPath+'scripts/templates/partials/panel-view.html',
		controller : 'CompanyInvoicesController',
		data : {
			ncyBreadcrumbLabel : 'Invoices'
		}
	}).state('company.invoices.view', {
		url : "/view",
		templateUrl : _contextPath+'scripts/app/company/invoices/company-invoices-view.html',
		abstract : true
	}).state('company.invoices.view.quick', {
		url : "/quickview",
		templateUrl : _contextPath+'scripts/app/company/invoices/company-invoices-quickview.html',
		controller : 'CompanyInvoicesQuickViewController',
		data : {
			ncyBreadcrumbSkip: true
		}
	}).state('company.invoices.view.quick.overview', {
		url : "/:id",
		templateUrl : _contextPath+'scripts/app/company/invoices/company-invoices-overview.html',
		controller : 'CompanyInvoicesOverviewController',
		resolve : {
			invoice :function(InvoicesREST,$stateParams,USERINFO) {
				return InvoicesREST.get(
						{id:$stateParams.id}).$promise;
			}
		},
		data : {
			ncyBreadcrumbLabel : '{{invoice.invoiceNumber}}'
		}
	})
	;
});