App.config(function ($stateProvider) {
	$stateProvider.state('admin', {
		url : "/admin",
		templateUrl : _contextPath+'scripts/app/admin/admin.html',
		abstract : true,
		data: {
			ncyBreadcrumbLabel : 'Admin'
		  }
	})
});