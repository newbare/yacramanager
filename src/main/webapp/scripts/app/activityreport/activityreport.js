App.config(function ($stateProvider) {
	$stateProvider.state('activity-report', {
		url : "/activity-report",
		templateUrl : _contextPath+'views/app/activity-report/activity-report.html',
		controller : 'ActivityReportController',
		data: {
	        pageTitle: 'Activity report',
	        ncyBreadcrumbLabel: 'Activity report'
	      }
	}).state('activity-report.list', {
		templateUrl : _contextPath+'views/app/activity-report/activity-report-list.html',
		controller : 'ActivityReportListController',
		abstract : true,
	}).state('activity-report.list.mine', {
		url : "/mine",
		templateUrl : _contextPath+'views/app/activity-report/activity-report-list-mine.html',
		controller : 'ActivityReportListMineController',
		data: {
	        pageTitle: 'My Activity report',
	        ncyBreadcrumbLabel: 'My Activity report'
	      }
	}).state('activity-report.list.tobeapproved', {
		url : "/tobeapproved",
		templateUrl : _contextPath+'views/app/activity-report/activity-report-list-tobeapproved.html',
		controller : 'ActivityReportListToBeApprovedController',
		data: {
	        pageTitle: 'Activity report to approve',
	        ncyBreadcrumbLabel: 'To be approved'
	      }
	});
});