App.config(function ($stateProvider) {
	$stateProvider.state('activity-report', {
		url : "/activity-report",
		templateUrl : _contextPath+'views/app/activity-report.html',
		controller : 'ActivityReportController',
		data: {
	        pageTitle: 'Activity report',
	        ncyBreadcrumbLabel: 'Activity report'
	      }
	})
});