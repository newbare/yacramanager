App.config(function ($stateProvider) {
	$stateProvider.state('tasks', {
		url : "/tasks",
		templateUrl : _contextPath+'scripts/app/tasks/tasks.html',
		controller : 'TasksController',
		data: {
	        pageTitle: 'Tasks',
	        pageDesc: 'Manage your tasks',
	        ncyBreadcrumbLabel: 'Tasks'
	      }
	});
});