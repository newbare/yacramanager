App.config(function ($stateProvider) {
	$stateProvider.state('admin.home', {
		url : "/home",
		templateUrl : _contextPath+'scripts/app/admin/home/admin-home.html',
		controller : 'AdminHomeController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel : 'Admin'
		  }
	})
});