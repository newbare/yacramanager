App.config(function ($stateProvider) {
	$stateProvider.state('admin.logs', {
		url : "/logs",
		templateUrl : _contextPath+'views/app/admin/admin-logs.html',
		controller : 'LogsController',
         data: {
        	roles: ['ROLE_ADMIN'],
				ncyBreadcrumbLabel: 'Logs'
			  }
	});
});