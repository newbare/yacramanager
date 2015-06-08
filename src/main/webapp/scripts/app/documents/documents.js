App.config(function ($stateProvider) {
	$stateProvider.state('files', {
		url : "/files",
		templateUrl : _contextPath+'scripts/app/documents/files.html',
		controller : 'FilesController',
		data: {
	        pageTitle: 'Files',
	        pageDesc: 'Manage your documents',
	        ncyBreadcrumbLabel: 'Files'
	      }
	});
});