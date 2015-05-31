App.config(function ($stateProvider) {
	$stateProvider.state('admin.metrics', {
		url : "/metrics",
		templateUrl : _contextPath+'scripts/app/admin/metrics/admin-metrics.html',
		controller : 'MetricsController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel: 'Metrics'
		  }
	})
});