App.config(function ($stateProvider) {
	$stateProvider.state('admin.home', {
		url : "/home",
		templateUrl : _contextPath+'views/app/admin/admin-home.html',
		controller : 'AdminHomeController',
		data: {
			roles: ['ROLE_ADMIN'],
			ncyBreadcrumbLabel : 'Admin'
		  }
	})
});