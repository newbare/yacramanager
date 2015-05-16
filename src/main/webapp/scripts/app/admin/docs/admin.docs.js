App.config(function ($stateProvider) {
	$stateProvider.state('api-docs', {
		url : "/api-docs",
		templateUrl : _contextPath+'views/app/components/api-docs.html',
		controller : 'ApiDocsController',
		data: {
			roles: ['ROLE_ADMIN'],
	        ncyBreadcrumbLabel: 'Yacra API'
	      }
	})
});