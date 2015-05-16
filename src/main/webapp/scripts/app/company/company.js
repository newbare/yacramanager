App.config(function ($stateProvider) {
	$stateProvider.state('company', {
		url : "/company",
		templateUrl : _contextPath+'views/app/company/company.html',
		data: {
	        pageTitle: 'Company management',
	        ncyBreadcrumbLabel: 'Company'
	      }
	})
});