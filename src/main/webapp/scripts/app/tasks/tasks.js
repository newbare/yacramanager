App.config(function ($stateProvider) {
	$stateProvider.state('tasks', {
		url : "/tasks",
		templateUrl : _contextPath+'views/app/tasks.html',
		controller : 'TasksController',
		data: {
	        pageTitle: 'Tasks',
	        ncyBreadcrumbLabel: 'Tasks'
	      }
	});
});