App.config(function ($stateProvider) {
	$stateProvider.state('timesheet', {
		url : "/timesheet",
		templateUrl : _contextPath+'views/app/timesheet.html',
		controller : 'TimeSheetController',
		data: {
	        pageTitle: 'Timesheet',
	        ncyBreadcrumbLabel: 'Timesheet'
	      }
	});
});