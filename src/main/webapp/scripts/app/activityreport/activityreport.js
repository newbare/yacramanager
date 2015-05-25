App.config(function ($stateProvider) {
	$stateProvider.state('activity-report', {
		url : "/activity-report",
		templateUrl : _contextPath+'scripts/app/activityreport/activity-report.html',
		controller : 'ActivityReportController',
		abstract:true
	}).state('activity-report.list', {
		templateUrl : _contextPath+'scripts/app/activityreport/activity-report-list.html',
		controller : 'ActivityReportListController',
		abstract : true,
	}).state('activity-report.list.mine', {
		url : "/mine",
		templateUrl : _contextPath+'scripts/app/activityreport/activity-report-list-mine.html',
		controller : 'ActivityReportListMineController',
		data: {
	        pageTitle: 'My Activity report',
	        ncyBreadcrumbLabel: 'My Activity report'
	      }
	}).state('activity-report.list.tobeapproved', {
		url : "/tobeapproved",
		templateUrl : _contextPath+'scripts/app/activityreport/activity-report-list-tobeapproved.html',
		controller : 'ActivityReportListToBeApprovedController',
		data: {
	        pageTitle: 'Activity report to approve',
	        ncyBreadcrumbLabel: 'To be approved'
	      }
	});
});