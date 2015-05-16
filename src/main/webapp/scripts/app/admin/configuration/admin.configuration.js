App.config(function ($stateProvider) {
	$stateProvider.state('admin.configuration', {
		url : "/configuration",
		templateUrl : _contextPath+'views/app/admin/admin-configuration.html',
		controller : 'AdminConfigurationController',
		data: {
			roles: ['ROLE_ADMIN'],
		    ncyBreadcrumbLabel: 'App configuration' 
		  }
		//controller : EntrepriseController,
	});
});