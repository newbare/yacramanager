App.config(function ($stateProvider) {
	$stateProvider.state('tasks', {
		url : "/tasks",
		templateUrl : _contextPath+'scripts/app/tasks/tasks.html',
		controller : 'TasksController',
		data: {
	        pageTitle: 'Tasks',
	        ncyBreadcrumbLabel: 'Tasks'
	      }
	});
});