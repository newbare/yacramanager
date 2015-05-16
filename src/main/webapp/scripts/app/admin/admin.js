App.config(function ($stateProvider) {
	$stateProvider.state('admin', {
		url : "/admin",
		templateUrl : _contextPath+'views/app/admin/admin.html',
		abstract : true,
		data: {
			ncyBreadcrumbLabel : 'Admin'
		  }
	})
});