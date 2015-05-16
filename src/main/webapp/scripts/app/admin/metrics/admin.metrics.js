App.config(function ($stateProvider) {
	$stateProvider.state('admin.metrics', {
		url : "/metrics",
		templateUrl : _contextPath+'views/app/admin/admin-metrics.html',
		controller : 'MetricsController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel: 'Metrics'
		  }
	})
});