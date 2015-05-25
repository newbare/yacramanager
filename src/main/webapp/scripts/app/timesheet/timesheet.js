App.config(function ($stateProvider) {
	$stateProvider.state('timesheet', {
		url : "/timesheet",
		templateUrl : _contextPath+'scripts/app/timesheet/timesheet.html',
		controller : 'TimeSheetController',
		data: {
	        pageTitle: 'Timesheet',
	        ncyBreadcrumbLabel: 'Timesheet'
	      }
	});
});