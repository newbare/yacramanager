App.config(function ($stateProvider) {
	$stateProvider.state('home', {
		url : "/home",
		views:{
			'': {
				templateUrl : _contextPath+'scripts/app/home/home.html',
				controller : 'HomeController'
            },
			'taskWidget@home':{
				templateUrl: _contextPath+'scripts/components/widgets/tasks.widget.html',
                controller: 'TasksWidgetController'
			},
			'expensesWidget@home':{
				templateUrl:  _contextPath+'scripts/components/widgets/expenses.widget.html',
                controller: 'ExpensesWidgetController'
			},
			'activitiesWidget@home':{
				templateUrl:  _contextPath+'scripts/components/widgets/activities.widget.html',
                controller: 'ActivitiesWidgetController'
			}
		},
		data: {
	        pageTitle: 'Home',
	        ncyBreadcrumbLabel: 'Home'
	      }
	});
});