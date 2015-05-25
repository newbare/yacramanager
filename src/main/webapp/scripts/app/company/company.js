App.config(function ($stateProvider) {
	$stateProvider.state('company', {
		url : "/company",
		templateUrl : _contextPath+'scripts/app/company/company.html',
		data: {
			roles:['ROLE_SSII_ADMIN'],
	        pageTitle: 'Company management',
	        ncyBreadcrumbLabel: 'Company'
	      }
	})
});