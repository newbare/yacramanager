App.config(function ($stateProvider) {
	$stateProvider.state('files', {
		url : "/files",
		templateUrl : _contextPath+'views/app/files.html',
		controller : 'FilesController',
		data: {
	        pageTitle: 'Files',
	        ncyBreadcrumbLabel: 'Files'
	      }
	});
});