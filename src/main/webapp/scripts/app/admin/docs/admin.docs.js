App.config(function ($stateProvider) {
	$stateProvider.state('api-docs', {
		url : "/api-docs",
		templateUrl : _contextPath+'scripts/app/admin/docs/api-docs.html',
		controller : 'ApiDocsController',
		data: {
			roles: ['ROLE_ADMIN'],
	        ncyBreadcrumbLabel: 'Yacra API'
	      }
	})
});